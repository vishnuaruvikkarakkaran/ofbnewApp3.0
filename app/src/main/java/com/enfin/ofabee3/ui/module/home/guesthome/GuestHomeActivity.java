package com.enfin.ofabee3.ui.module.home.guesthome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.remote.model.home.response.HomeResponse;
import com.enfin.ofabee3.ui.base.BaseActivity;
import com.enfin.ofabee3.ui.base.baserecyclerview.OnRecyclerViewClickListener;
import com.enfin.ofabee3.ui.module.bundlesubject.BundleSubjectActivity;
import com.enfin.ofabee3.ui.module.contentdelivery.ContentDeliveryActivity;
import com.enfin.ofabee3.ui.module.coursedetail.TabsHeaderActivity;
import com.enfin.ofabee3.ui.module.featuredcourses.FeaturedCoursesListActivity;
import com.enfin.ofabee3.ui.module.home.HomeContract;
import com.enfin.ofabee3.ui.module.home.HomeFragment;
import com.enfin.ofabee3.ui.module.home.HomePresenter;
import com.enfin.ofabee3.ui.module.home.banner.HomeBannerAdapter;
import com.enfin.ofabee3.ui.module.home.featuredcourses.FeaturedCourse;
import com.enfin.ofabee3.ui.module.home.featuredcourses.FeaturedCoursesAdapter;
import com.enfin.ofabee3.ui.module.home.popularcourses.PopularCourse;
import com.enfin.ofabee3.ui.module.home.popularcourses.PopularCoursesAdapter;
import com.enfin.ofabee3.ui.module.login.LoginActivity;
import com.enfin.ofabee3.ui.module.nointernet.NoInternetActivity;
import com.enfin.ofabee3.ui.module.popularcourseslist.PopularCoursesListActivity;
import com.enfin.ofabee3.ui.module.registration.RegistrationActivity;
import com.enfin.ofabee3.utils.AppUtils;
import com.enfin.ofabee3.utils.Constants;
import com.enfin.ofabee3.utils.OBLogger;

import java.util.ArrayList;
import java.util.List;

import hk.ids.gws.android.sclick.SClick;

public class GuestHomeActivity extends BaseActivity implements View.OnClickListener, OnRecyclerViewClickListener,
        GuestHomeContract.View, SwipeRefreshLayout.OnRefreshListener {

    //SliderView homeBannerView;
    ViewPager viewPagerSlider;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    private ArrayList<String> images;
    private TextView popularCourseTV, featuredCourseTV, popularCourseSeeAll, featuredCourseSeeAll;
    private RecyclerView popularRecyclerView;
    private PopularCoursesAdapter adapterPopularCourse;

    private RecyclerView featuredRecyclerView;
    private FeaturedCoursesAdapter adapterFeaturedCourse;
    private GuestHomeContract.Presenter mPresenter;
    private Dialog progressDialog;
    private HomeResponse homeResponseData;
    private SwipeRefreshLayout swipeLayout;
    private TextView registerTV, signInTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_home);
        viewPagerSlider = findViewById(R.id.pager);
        sliderDotspanel = findViewById(R.id.slider_dots);
        popularRecyclerView = findViewById(R.id.popular_courses_rv);
        featuredRecyclerView = findViewById(R.id.featured_courses_rv);
        popularCourseTV = findViewById(R.id.text_popular_course_tv);
        featuredCourseTV = findViewById(R.id.text_featured_course_tv);
        popularCourseSeeAll = findViewById(R.id.popular_courses_see_all_tv);
        featuredCourseSeeAll = findViewById(R.id.featured_courses_see_all_tv);
        signInTV = findViewById(R.id.signInTextView);
        registerTV = findViewById(R.id.registerTextView);
        progressDialog = AppUtils.showProgressDialog(this);

        swipeLayout = findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_red_dark),
                getResources().getColor(android.R.color.holo_blue_dark),
                getResources().getColor(android.R.color.holo_orange_dark));

        initPopularCourseView();
        initFeaturedCourseView();
        mPresenter = new GuestHomePresenter(GuestHomeActivity.this, this);
        mPresenter.getHomeData(getApplicationContext());

        clickActions();
    }

    private void clickActions() {
        signInTV.setOnClickListener(view -> {
            if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return;
            Intent signIn = new Intent(GuestHomeActivity.this, LoginActivity.class);
            signIn.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(signIn);
            finish();
        });
        registerTV.setOnClickListener(view -> {
            if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return;
            Intent signUp = new Intent(GuestHomeActivity.this, RegistrationActivity.class);
            signUp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(signUp);
            finish();
        });
    }

    @Override
    public int getLayout() {
        setTheme(R.style.HomeScreenTheme);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.homeNavigationBarColor));
            getWindow().setStatusBarColor(getResources().getColor(R.color.homeNavigationBarColor));
        }
        return R.layout.activity_guest_home;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onRefresh() {
        swipeLayout.setRefreshing(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.getHomeData(GuestHomeActivity.this);
            }
        }, 100);
    }

    @Override
    public void onItemClicked(Object item) {
        if (item instanceof PopularCourse) {
            if (((PopularCourse) item).isEnrolled()) {
                OBLogger.e("TYPE " + ((PopularCourse) item).getCourseItemType());
                if (((PopularCourse) item).getCourseItemType().equals("bundle")) {
                    Intent i = new Intent(this, BundleSubjectActivity.class);
                    i.putExtra(Constants.COURSE_ID, ((PopularCourse) item).getID());
                    startActivity(i);
                } else {
                    Intent contentDeliveryIntent = new Intent(this, ContentDeliveryActivity.class);
                    contentDeliveryIntent.putExtra(Constants.COURSE_ID, ((PopularCourse) item).getID());
                    this.startActivity(contentDeliveryIntent);
                }
            } else {
                progressDialog = AppUtils.showProgressDialog(this);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                //progressDialog.show();
                Intent i = new Intent(this, TabsHeaderActivity.class);
                i.putExtra(Constants.COURSE_ID, ((PopularCourse) item).getID());
                i.putExtra(Constants.TYPE, ((PopularCourse) item).getCourseItemType());
                startActivity(i);
                //Toast.makeText(getActivity(), "Please contact admin for subscription", Toast.LENGTH_SHORT).show();
            }
        } else if (item instanceof FeaturedCourse) {
            if (((FeaturedCourse) item).isEnrolled()) {
                if (((FeaturedCourse) item).getCourseItemType().equals("bundle")) {
                    Intent i = new Intent(this, BundleSubjectActivity.class);
                    i.putExtra(Constants.COURSE_ID, ((FeaturedCourse) item).getID());
                    startActivity(i);
                } else {
                    Intent contentDeliveryIntent = new Intent(this, ContentDeliveryActivity.class);
                    contentDeliveryIntent.putExtra(Constants.COURSE_ID, ((FeaturedCourse) item).getID());
                    startActivity(contentDeliveryIntent);
                }


            } else {
                progressDialog = AppUtils.showProgressDialog(this);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                //progressDialog.show();
                Intent i = new Intent(this, TabsHeaderActivity.class);
                i.putExtra(Constants.COURSE_ID, ((FeaturedCourse) item).getID());
                i.putExtra(Constants.TYPE, ((FeaturedCourse) item).getCourseItemType());
                startActivity(i);
                //Toast.makeText(getActivity(), "Please contact admin for subscription", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public <T> void onSuccees(T type) {
        if (type instanceof HomeResponse) {
            swipeLayout.setRefreshing(false);
            HomeResponse homeResponse = (HomeResponse) type;
            if (homeResponse.getData() != null) {
                popularCourseTV.setText(homeResponse.getData().getCourses().get(0).getTitle());
                featuredCourseTV.setText(homeResponse.getData().getCourses().get(1).getTitle());
                featuredCourseSeeAll.setText("See all");
                popularCourseSeeAll.setText("See all");
                updatePopularCourseList(homeResponse);
                updateFeaturedCourseList(homeResponse);
                updateBannerImageList(homeResponse);
            } else {
                popularCourseTV.setText("No popular courses found");
                featuredCourseTV.setText("No featured courses found");
            }
        }
    }

    private void updatePopularCourseList(HomeResponse data) {
        List<PopularCourse> popularCoursesList = new ArrayList<>();
        for (int i = 0; i < data.getData().getCourses().get(0).getList().size(); i++) {
            PopularCourse popularCourse = new PopularCourse(data.getData().getCourses().get(0).getList().get(i));
            popularCourse.setPopularCourse((data.getData().getCourses().get(0).getList().get(i)));
            popularCoursesList.add(popularCourse);
        }
        adapterPopularCourse.setItems(popularCoursesList);
    }

    private void updateFeaturedCourseList(HomeResponse data) {
        List<FeaturedCourse> featuredCourseList = new ArrayList<>();
        for (int i = 0; i < data.getData().getCourses().get(1).getList().size(); i++) {
            FeaturedCourse featuredCourse = new FeaturedCourse();
            featuredCourse.setFeaturedCourse(data.getData().getCourses().get(1).getList().get(i));
            featuredCourseList.add(featuredCourse);
        }
        adapterFeaturedCourse.setItems(featuredCourseList);
    }

    private void updateBannerImageList(HomeResponse data) {
        images = new ArrayList<>();
        for (int i = 0; i < data.getData().getBanners().getList().size(); i++) {
            images.add(data.getData().getBanners().getList().get(i).getMb_title());
        }
        if (images.size()>0)
            updateBanner();
    }

    @Override
    public <T> void onFailure(T type) {
        swipeLayout.setRefreshing(false);
        OBLogger.e("FAILURE ");
    }

    @Override
    public void setPresenter(GuestHomeContract.Presenter presenter) {
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
        if (message.equalsIgnoreCase("No Internet")) {
            Intent noInternet = new Intent(this, NoInternetActivity.class);
            startActivity(noInternet);
        } else
            AppUtils.onShowAlertDialog(this, message);
    }

    @Override
    public void onServerError(String message) {
    }

    @Override
    public void noDataFound() {
        homeResponseData = new HomeResponse();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.stop();
        onHideProgress();
    }

    private void initPopularCourseView() {
        adapterPopularCourse = new PopularCoursesAdapter(this);
        adapterPopularCourse.setListener(this);
        popularRecyclerView.setAdapter(adapterPopularCourse);
        popularRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        popularCourseSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent popularCoursesList = new Intent(GuestHomeActivity.this, PopularCoursesListActivity.class);
                startActivity(popularCoursesList);
            }
        });
    }

    private void initFeaturedCourseView() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        adapterFeaturedCourse = new FeaturedCoursesAdapter(this);
        adapterFeaturedCourse.setListener(this);
        featuredRecyclerView.setAdapter(adapterFeaturedCourse);
        featuredRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        featuredCourseSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent featuredCourseList = new Intent(GuestHomeActivity.this, FeaturedCoursesListActivity.class);
                startActivity(featuredCourseList);
            }
        });
    }

    //Temporary images list
    public void updateBanner() {
        HomeBannerAdapter viewPagerAdapter = new HomeBannerAdapter(this, images);
        dotscount = images.size();
        dots = new ImageView[dotscount];
        sliderDotspanel.removeAllViews();
        viewPagerSlider.setAdapter(viewPagerAdapter);

        for (int i = 0; i < dotscount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.home_slider_indicator_non_active));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            sliderDotspanel.addView(dots[i], params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.home_slider_indicator_active));

        viewPagerSlider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(GuestHomeActivity.this, R.drawable.home_slider_indicator_non_active));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(GuestHomeActivity.this, R.drawable.home_slider_indicator_active));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

}
