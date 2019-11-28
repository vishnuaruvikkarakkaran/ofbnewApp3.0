package com.enfin.ofabee3.ui.module.home;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.remote.model.home.response.HomeResponse;
import com.enfin.ofabee3.ui.base.BaseFragment;
import com.enfin.ofabee3.ui.base.baserecyclerview.OnRecyclerViewClickListener;
import com.enfin.ofabee3.ui.module.bundlesubject.BundleSubjectActivity;
import com.enfin.ofabee3.ui.module.contentdelivery.ContentDeliveryActivity;
import com.enfin.ofabee3.ui.module.coursedetail.TabsHeaderActivity;
import com.enfin.ofabee3.ui.module.coursedetail.bundledetails.BundleDetailActivity;
import com.enfin.ofabee3.ui.module.explore.ExploreCourse;
import com.enfin.ofabee3.ui.module.featuredcourses.FeaturedCoursesListActivity;
import com.enfin.ofabee3.ui.module.home.banner.HomeBannerAdapter;
import com.enfin.ofabee3.ui.module.home.featuredcourses.FeaturedCourse;
import com.enfin.ofabee3.ui.module.home.featuredcourses.FeaturedCoursesAdapter;
import com.enfin.ofabee3.ui.module.home.popularcourses.PopularCourse;
import com.enfin.ofabee3.ui.module.home.popularcourses.PopularCoursesAdapter;
import com.enfin.ofabee3.ui.module.inactiveuser.InActiveUserActivity;
import com.enfin.ofabee3.ui.module.login.LoginActivity;
import com.enfin.ofabee3.ui.module.nointernet.NoInternetActivity;
import com.enfin.ofabee3.ui.module.popularcourseslist.PopularCoursesListActivity;
import com.enfin.ofabee3.utils.AppUtils;
import com.enfin.ofabee3.utils.Constants;
import com.enfin.ofabee3.utils.NetworkUtil;
import com.enfin.ofabee3.utils.OBLogger;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")

public class HomeFragment extends BaseFragment implements View.OnClickListener, OnRecyclerViewClickListener,
        HomeContract.View, SwipeRefreshLayout.OnRefreshListener {

    //SliderView homeBannerView;
    ViewPager viewPagerSlider;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    private ArrayList<String> images;
    private TextView popularCourseTV, featuredCourseTV, popularCourseSeeAll, featuredCourseSeeAll;
    private RecyclerView popularRecyclerView;
    private PopularCoursesAdapter adapterPopularCourse;
    private List<PopularCourse> popularCoursesList;
    private List<FeaturedCourse> featuredCourseList;
    private RecyclerView featuredRecyclerView;
    private FeaturedCoursesAdapter adapterFeaturedCourse;
    private HomeContract.Presenter mPresenter;
    private Dialog progressDialog;
    private HomeResponse homeResponseData;
    private SwipeRefreshLayout swipeLayout;
    private TextView noPopularCourseTV, noFeaturedCourseTV;
    private ShimmerFrameLayout mShimmerViewContainer;
    private boolean noPopularCourse = true, noFeaturedCourse = true;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        OBLogger.e("onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OBLogger.e("onCreate home frgamnet");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        OBLogger.e("onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_home, null);
        viewPagerSlider = rootView.findViewById(R.id.pager);
        sliderDotspanel = rootView.findViewById(R.id.slider_dots);
        popularRecyclerView = rootView.findViewById(R.id.popular_courses_rv);
        featuredRecyclerView = rootView.findViewById(R.id.featured_courses_rv);
        popularCourseTV = rootView.findViewById(R.id.text_popular_course_tv);
        featuredCourseTV = rootView.findViewById(R.id.text_featured_course_tv);
        popularCourseSeeAll = rootView.findViewById(R.id.popular_courses_see_all_tv);
        featuredCourseSeeAll = rootView.findViewById(R.id.featured_courses_see_all_tv);
        progressDialog = AppUtils.showProgressDialog(getActivity());
        noPopularCourseTV = rootView.findViewById(R.id.no_popular_course);
        noFeaturedCourseTV = rootView.findViewById(R.id.no_featured_course);
        mShimmerViewContainer = rootView.findViewById(R.id.shimmer_view_container);
        swipeLayout = rootView.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(getActivity().getResources().getColor(android.R.color.holo_green_dark),
                getActivity().getResources().getColor(android.R.color.holo_red_dark),
                getActivity().getResources().getColor(android.R.color.holo_blue_dark),
                getActivity().getResources().getColor(android.R.color.holo_orange_dark));
        initPopularCourseView();
        initFeaturedCourseView();
        mPresenter = new HomePresenter(getActivity(), this);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        OBLogger.e("onViewCreated");
        //mPresenter.getHomeData(getActivity());
    }

    @Override
    public void onStart() {
        OBLogger.e("onStart");
        super.onStart();
        mPresenter.start();
    }

    @Override
    public void onResume() {
        mShimmerViewContainer.startShimmerAnimation();
        homeResponseData = ((HomeActivity) getActivity()).getHomeData();
        if (homeResponseData != null) {
            onSuccees(homeResponseData);
            popularCourseSeeAll.setEnabled(true);
            featuredCourseSeeAll.setEnabled(true);
        } else {
            mPresenter.getHomeData(getActivity());
        }
        super.onResume();
    }

    @Override
    public void onStop() {
        OBLogger.e("onStop");
        super.onStop();
        mPresenter.stop();
        onHideProgress();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        OBLogger.e("On Pause");
        super.onPause();
    }


    private void initPopularCourseView() {
        adapterPopularCourse = new PopularCoursesAdapter(getActivity());
        adapterPopularCourse.setListener(this);
        popularRecyclerView.setAdapter(adapterPopularCourse);
        popularRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        popularCourseSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (homeResponseData.getData().getCourses().get(0).getIdentifier().equals("1")) {
                    popularCourseSeeAll.setEnabled(false);
                    Intent popularCoursesList = new Intent(getActivity(), PopularCoursesListActivity.class);
                    startActivity(popularCoursesList);
                }
                if (homeResponseData.getData().getCourses().get(0).getIdentifier().equals("2")) {
                    featuredCourseSeeAll.setEnabled(false);
                    Intent featuredCourseList = new Intent(getActivity(), FeaturedCoursesListActivity.class);
                    startActivity(featuredCourseList);
                }
            }
        });
    }

    private void initFeaturedCourseView() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        adapterFeaturedCourse = new FeaturedCoursesAdapter(getActivity());
        adapterFeaturedCourse.setListener(this);
        featuredRecyclerView.setAdapter(adapterFeaturedCourse);
        featuredRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        featuredCourseSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                featuredCourseSeeAll.setEnabled(false);
                Intent featuredCourseList = new Intent(getActivity(), FeaturedCoursesListActivity.class);
                startActivity(featuredCourseList);
            }
        });
    }

    //Temporary images list
    public void updateBanner() {
        HomeBannerAdapter viewPagerAdapter = new HomeBannerAdapter(getActivity(), images);
        dotscount = images.size();
        dots = new ImageView[dotscount];
        sliderDotspanel.removeAllViews();
        viewPagerSlider.setAdapter(viewPagerAdapter);

        for (int i = 0; i < dotscount; i++) {
            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.home_slider_indicator_non_active));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            sliderDotspanel.addView(dots[i], params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.home_slider_indicator_active));

        viewPagerSlider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.home_slider_indicator_non_active));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.home_slider_indicator_active));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    public void onSearchQuery(String searchString) {
        super.onSearchQuery(searchString);
        OBLogger.e("Search String in Home fragment is " + searchString);
        if (popularCoursesList != null) {
            List<PopularCourse> result = new ArrayList<PopularCourse>();
            for (PopularCourse popularCourse : popularCoursesList) {
                if (popularCourse.getCourseName().toLowerCase().contains(searchString.toLowerCase())) {
                    result.add(popularCourse);
                }
            }
            if (result.size() == 0) {
                popularCourseSeeAll.setVisibility(View.INVISIBLE);
                popularRecyclerView.setVisibility(View.GONE);
                noPopularCourseTV.setVisibility(View.VISIBLE);
            } else {
                popularCourseSeeAll.setVisibility(View.VISIBLE);
                popularRecyclerView.setVisibility(View.VISIBLE);
                noPopularCourseTV.setVisibility(View.GONE);
            }
            adapterPopularCourse.setItems(result);
            adapterPopularCourse.notifyDataSetChanged();
        }
        if (featuredCourseList != null) {
            List<FeaturedCourse> result = new ArrayList<FeaturedCourse>();
            for (FeaturedCourse featuredCourse : featuredCourseList) {
                if (featuredCourse.getCourseName().toLowerCase().contains(searchString.toLowerCase())) {
                    result.add(featuredCourse);
                }
            }
            if (result.size() == 0) {
                featuredCourseSeeAll.setVisibility(View.INVISIBLE);
                featuredRecyclerView.setVisibility(View.GONE);
                noFeaturedCourseTV.setVisibility(View.VISIBLE);
            } else {
                featuredCourseSeeAll.setVisibility(View.VISIBLE);
                featuredRecyclerView.setVisibility(View.VISIBLE);
                noFeaturedCourseTV.setVisibility(View.GONE);
            }
            adapterFeaturedCourse.setItems(result);
            adapterFeaturedCourse.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClicked(Object item) {
        if (NetworkUtil.isConnected(getActivity())) {
            if (item instanceof PopularCourse) {
                if (((PopularCourse) item).isEnrolled()) {
                    OBLogger.e("TYPE " + ((PopularCourse) item).getCourseItemType());
                    if (((PopularCourse) item).getCourseItemType().equals("bundle")) {
                        Intent i = new Intent(getActivity(), BundleSubjectActivity.class);
                        i.putExtra(Constants.COURSE_ID, ((PopularCourse) item).getID());
                        getActivity().startActivity(i);
                    } else {
                        Intent contentDeliveryIntent = new Intent(getActivity(), ContentDeliveryActivity.class);
                        contentDeliveryIntent.putExtra(Constants.COURSE_ID, ((PopularCourse) item).getID());
                        getActivity().startActivity(contentDeliveryIntent);
                    }
                } else {
                    progressDialog = AppUtils.showProgressDialog(getActivity());
                    progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    //progressDialog.show();
                    Intent i = new Intent(getActivity(), TabsHeaderActivity.class);
                    i.putExtra(Constants.COURSE_ID, ((PopularCourse) item).getID());
                    i.putExtra(Constants.TYPE, ((PopularCourse) item).getCourseItemType());
                    getActivity().startActivity(i);
                    //Toast.makeText(getActivity(), "Please contact admin for subscription", Toast.LENGTH_SHORT).show();
                }
            } else if (item instanceof FeaturedCourse) {
                if (((FeaturedCourse) item).isEnrolled()) {
                    if (((FeaturedCourse) item).getCourseItemType().equals("bundle")) {
                        Intent i = new Intent(getActivity(), BundleSubjectActivity.class);
                        i.putExtra(Constants.COURSE_ID, ((FeaturedCourse) item).getID());
                        getActivity().startActivity(i);
                    } else {
                        Intent contentDeliveryIntent = new Intent(getActivity(), ContentDeliveryActivity.class);
                        contentDeliveryIntent.putExtra(Constants.COURSE_ID, ((FeaturedCourse) item).getID());
                        getActivity().startActivity(contentDeliveryIntent);
                    }


                } else {
                    progressDialog = AppUtils.showProgressDialog(getActivity());
                    progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    //progressDialog.show();
                    Intent i = new Intent(getActivity(), TabsHeaderActivity.class);
                    i.putExtra(Constants.COURSE_ID, ((FeaturedCourse) item).getID());
                    i.putExtra(Constants.TYPE, ((FeaturedCourse) item).getCourseItemType());
                    getActivity().startActivity(i);
                    //Toast.makeText(getActivity(), "Please contact admin for subscription", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(getActivity(), "Please check your internet connection!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public <T> void onSuccees(T type) {
        if (type instanceof HomeResponse) {
            if (getActivity() != null) {
                homeResponseData = (HomeResponse) type;
                ((HomeActivity) getActivity()).updateHomeData(homeResponseData);
                swipeLayout.setRefreshing(false);
                HomeResponse homeResponse = homeResponseData;
                noPopularCourseTV.setVisibility(View.GONE);
                if (homeResponse.getData() != null) {
                    if (homeResponse.getData().getCourses().size() > 1) {
                        if (homeResponse.getData().getCourses().get(0).getTitle() != null) {
                            popularCourseTV.setVisibility(View.VISIBLE);
                            popularCourseSeeAll.setVisibility(View.VISIBLE);
                            popularCourseSeeAll.setText("See all");
                            popularCourseTV.setText(homeResponse.getData().getCourses().get(0).getTitle());
                            updatePopularCourseList(homeResponse);
                        }

                        if (homeResponse.getData().getCourses().get(1).getTitle() != null) {
                            featuredCourseTV.setVisibility(View.VISIBLE);
                            featuredCourseSeeAll.setVisibility(View.VISIBLE);
                            featuredCourseTV.setText(homeResponse.getData().getCourses().get(1).getTitle());
                            featuredCourseSeeAll.setText("See all");
                            updateFeaturedCourseList(homeResponse);
                        }
                    } else if (homeResponse.getData().getCourses().size() == 1) {
                        if (homeResponse.getData().getCourses().get(0).getTitle() != null) {
                            popularCourseTV.setVisibility(View.VISIBLE);
                            popularCourseSeeAll.setVisibility(View.VISIBLE);
                            popularCourseSeeAll.setText("See all");
                            popularCourseTV.setText(homeResponse.getData().getCourses().get(0).getTitle());
                            updatePopularCourseList(homeResponse);
                            adapterFeaturedCourse.clear();
                            featuredCourseSeeAll.setVisibility(View.GONE);
                            featuredCourseTV.setVisibility(View.GONE);
                        }

                    } else if (homeResponse.getData().getCourses().size() == 0) {
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        noPopularCourseTV.setVisibility(View.VISIBLE);
                        popularCourseSeeAll.setVisibility(View.GONE);
                        popularCourseTV.setVisibility(View.GONE);
                        featuredCourseSeeAll.setVisibility(View.GONE);
                        featuredCourseTV.setVisibility(View.GONE);
                        adapterPopularCourse.clear();
                        adapterFeaturedCourse.clear();
                    }

                    if (homeResponse.getData().getBanners() != null) {
                        updateBannerImageList(homeResponse);
                    }
                }
            }
        } else if (type instanceof String) {
            if (((String) type).equalsIgnoreCase(Constants.DB_CLEAR)) {
                Toast.makeText(getActivity(), "Logout successfully", Toast.LENGTH_SHORT).show();
                Intent login = new Intent(getActivity(), LoginActivity.class);
                login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(login);
                getActivity().finish();
            }
        } else {
            popularCourseSeeAll.setVisibility(View.GONE);
            popularCourseTV.setVisibility(View.GONE);
            featuredCourseSeeAll.setVisibility(View.GONE);
            featuredCourseTV.setVisibility(View.GONE);
            adapterPopularCourse.clear();
            adapterFeaturedCourse.clear();
            popularCourseTV.setText("No popular courses found");
            featuredCourseTV.setText("No featured courses found");
            featuredCourseSeeAll.setVisibility(View.GONE);
            popularCourseSeeAll.setVisibility(View.GONE);
        }
    }

    private void updatePopularCourseList(HomeResponse data) {
        mShimmerViewContainer.stopShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.GONE);
        popularCoursesList = new ArrayList<>();
        noPopularCourse = false;
        for (int i = 0; i < data.getData().getCourses().get(0).getList().size(); i++) {
            PopularCourse popularCourse = new PopularCourse(data.getData().getCourses().get(0).getList().get(i));
            popularCourse.setPopularCourse((data.getData().getCourses().get(0).getList().get(i)));
            popularCoursesList.add(popularCourse);
        }
        adapterPopularCourse.setItems(popularCoursesList);
        adapterPopularCourse.notifyDataSetChanged();
    }

    private void updateFeaturedCourseList(HomeResponse data) {
        featuredCourseList = new ArrayList<>();
        noFeaturedCourse = false;
        for (int i = 0; i < data.getData().getCourses().get(1).getList().size(); i++) {
            FeaturedCourse featuredCourse = new FeaturedCourse();
            featuredCourse.setFeaturedCourse(data.getData().getCourses().get(1).getList().get(i));
            featuredCourseList.add(featuredCourse);
        }
        adapterFeaturedCourse.setItems(featuredCourseList);
        adapterFeaturedCourse.notifyDataSetChanged();
    }

    private void updateBannerImageList(HomeResponse data) {
        images = new ArrayList<>();
        for (int i = 0; i < data.getData().getBanners().getList().size(); i++) {
            images.add(data.getData().getBanners().getList().get(i).getMb_title());
        }
        OBLogger.e("size array " + images.size());
        if (images.size() > 0)
            updateBanner();
    }

    @Override
    public <T> void onFailure(T type) {
        if (swipeLayout != null)
            swipeLayout.setRefreshing(false);
        OBLogger.e("FAILURE ");
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onShowProgress() {
        if (progressDialog != null)
            progressDialog.show();
    }

    @Override
    public void onHideProgress() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onShowAlertDialog(String message) {
        swipeLayout.setRefreshing(false);
        if (message.equalsIgnoreCase("No Internet")) {
            Intent noInternet = new Intent(getActivity(), NoInternetActivity.class);
            startActivity(noInternet);
        } else
            AppUtils.onShowAlertDialog(getActivity(), message);
    }

    @Override
    public void onServerError(String message) {
    }

    @Override
    public void onRefresh() {
        swipeLayout.setRefreshing(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.getHomeData(getActivity());
            }
        }, 100);
    }

    @Override
    public void noDataFound() {
    }

    @Override
    public void logoutAction(String message) {
       Intent userDeactivated = new Intent(getActivity(), InActiveUserActivity.class);
       userDeactivated.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
       startActivity(userDeactivated);
       getActivity().finish();
    }
}
