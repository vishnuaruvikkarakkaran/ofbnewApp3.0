package com.enfin.ofabee3.ui.module.explore;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.remote.model.coursecategory.response.CourseCategoryResponse;
import com.enfin.ofabee3.data.remote.model.explorecourse.response.ExploreCoursesResponse;
import com.enfin.ofabee3.data.remote.model.seeallcourses.response.SeeAllResponse;
import com.enfin.ofabee3.ui.base.BaseFragment;
import com.enfin.ofabee3.ui.base.baserecyclerview.OnRecyclerViewClickListener;
import com.enfin.ofabee3.ui.module.bundlesubject.BundleSubjectActivity;
import com.enfin.ofabee3.ui.module.contentdelivery.ContentDeliveryActivity;
import com.enfin.ofabee3.ui.module.coursecategories.CourseCategoryAdapter;
import com.enfin.ofabee3.ui.module.coursedetail.TabsHeaderActivity;
import com.enfin.ofabee3.ui.module.home.HomeActivity;
import com.enfin.ofabee3.ui.module.home.featuredcourses.FeaturedCourse;
import com.enfin.ofabee3.ui.module.home.popularcourses.PopularCourse;
import com.enfin.ofabee3.ui.module.home.popularcourses.PopularCoursesAdapter;
import com.enfin.ofabee3.ui.module.inactiveuser.InActiveUserActivity;
import com.enfin.ofabee3.ui.module.nointernet.NoInternetActivity;
import com.enfin.ofabee3.ui.module.userprofile.UserProfileContract;
import com.enfin.ofabee3.utils.AppUtils;
import com.enfin.ofabee3.utils.Constants;
import com.enfin.ofabee3.utils.NetworkUtil;
import com.enfin.ofabee3.utils.OBLogger;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

import hk.ids.gws.android.sclick.SClick;

public class ExploreFragment extends BaseFragment implements ExploreContract.View,
        OnRecyclerViewClickListener, SwipeRefreshLayout.OnRefreshListener {

    public static ExploreContract.Presenter mPresenter;
    private Dialog progressDialog;
    private RecyclerView coursesRecyclerView;
    private ExploreCoursesAdapter adapterCourses;
    private LinearLayoutManager layoutManager;
    private List<ExploreCourse> coursesList;
    private SwipeRefreshLayout swipeLayout;
    private TextView noCourseTextView;
    private ExploreCoursesResponse response;
    private CourseCategoryResponse responseCategory;
    private View anchorView;
    public static List<CourseCategoryResponse.DataBean> mCoursesCategoryList = new ArrayList<>();
    private MenuItem searchItem, filterItem;
    private CategoryPopupWindow popUpClass;
    private ArrayList<String> categoryNames = new ArrayList<>();
    private String category_ID;
    private ShimmerFrameLayout mShimmerViewContainer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_explore, null);
        progressDialog = AppUtils.showProgressDialog(getActivity());
        //progressDialog = new ProgressDialog(getActivity(), R.style.ProgressDialogStyle);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        coursesRecyclerView = rootview.findViewById(R.id.courses_rv);
        noCourseTextView = rootview.findViewById(R.id.no_course_found_tv);
        swipeLayout = rootview.findViewById(R.id.swipe_container);
        mShimmerViewContainer = rootview.findViewById(R.id.shimmer_view_container);
        anchorView = rootview;
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_red_dark),
                getResources().getColor(android.R.color.holo_blue_dark),
                getResources().getColor(android.R.color.holo_orange_dark));
        coursesList = new ArrayList<>();
        mPresenter = new ExplorePresenter(this, getActivity());
        initCoursesList();
        category_ID = Prefs.getString(Constants.CATEGORY_IDS, "");
        if (response == null) {
            mPresenter.getCourses(getActivity(), category_ID, 0);
            mPresenter.getCoursesCategoryWithoutHeader(getActivity());
        } else {
            if (responseCategory == null)
                mPresenter.getCoursesCategoryWithoutHeader(getActivity());
            onSuccess(response);
            mPresenter.getCourses(getActivity(), category_ID, 0);
            mPresenter.getCoursesCategoryWithoutHeader(getActivity());
        }
        return rootview;
    }

    private void initCoursesList() {
        adapterCourses = new ExploreCoursesAdapter(getActivity());
        adapterCourses.setListener(this);
        layoutManager = new LinearLayoutManager(getActivity());
        coursesRecyclerView.setAdapter(adapterCourses);
        coursesRecyclerView.setLayoutManager(layoutManager);
        if (getActivity() != null) {
            response = ((HomeActivity) getActivity()).getExploreCourseData();
        }
    }

    @Override
    public <T> void onSuccess(T type) {
        if (type instanceof ExploreCoursesResponse) {
            if (searchItem != null)
                searchItem.setVisible(true);
            response = (ExploreCoursesResponse) type;
            swipeLayout.setRefreshing(false);
            loadPopularCourseList((ExploreCoursesResponse) type);
        } else if (type instanceof CourseCategoryResponse) {
            //mShimmerViewContainer.stopShimmerAnimation();
            //mShimmerViewContainer.setVisibility(View.GONE);
            mCoursesCategoryList.clear();
            responseCategory = (CourseCategoryResponse) type;
            mCoursesCategoryList.addAll(responseCategory.getData());
        }
    }

    private void loadPopularCourseList(ExploreCoursesResponse data) {
        coursesList.clear();
        mShimmerViewContainer.stopShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.GONE);
        if (getActivity() != null) {
            ((HomeActivity) getActivity()).updateExploreCourseData(data);
        }
        for (int i = 0; i < data.getData().getAll_course().size(); i++) {
            ExploreCourse exploreCourse = new ExploreCourse();
            exploreCourse.setExploreCourse((data.getData().getAll_course().get(i)));
            coursesList.add(exploreCourse);
        }
        adapterCourses.setItems(coursesList);
        if (adapterCourses != null && coursesRecyclerView != null) {
            coursesRecyclerView.getLayoutManager().scrollToPosition(0);
            adapterCourses.notifyDataSetChanged();
        }
    }

    @Override
    public <T> void onFailure(T type) {
        if (swipeLayout != null)
            swipeLayout.setRefreshing(false);
        onShowAlertDialog(type.toString());
    }

    @Override
    public void onSearchQuery(String searchString) {
        super.onSearchQuery(searchString);
        if (coursesList != null) {
            List<ExploreCourse> result = new ArrayList<>();
            for (ExploreCourse course : coursesList) {
                if (course.getCourseName().toLowerCase().contains(searchString.toLowerCase())) {
                    if (!result.contains(course))
                        result.add(course);
                }
            }

            for (ExploreCourse course : coursesList) {
                if (course.getExploreCourse().getItem_category().size() > 0) {
                    for (int i = 0; i < course.getExploreCourse().getItem_category().size(); i++) {
                        if (course.getExploreCourse().getItem_category().get(i).getCt_name().toLowerCase().contains(searchString.toLowerCase())) {
                            if (!result.contains(course)) {
                                result.add(course);
                                course.getExploreCourse().getItem_category().set(0, course.getExploreCourse().getItem_category().get(i));
                            }
                        }
                    }
                }

                if (course.getExploreCourse().getItem_type().toLowerCase().contains(searchString.toLowerCase())) {
                    if (!result.contains(course))
                        result.add(course);
                }
            }

            adapterCourses.setItems(result);
            adapterCourses.notifyDataSetChanged();
            if (result.size() == 0) {
                noCourseTextView.setVisibility(View.VISIBLE);
            } else
                noCourseTextView.setVisibility(View.GONE);
        }
    }
    /* @Override
    public void onSearchQueryTextSubmit(String searchString) {
        super.onSearchQueryTextSubmit(searchString);
    }*/

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        searchItem = menu.findItem(R.id.search);
        filterItem = menu.findItem(R.id.filter);
        if (searchItem != null) {
            searchItem.setVisible(false);
        }
        if (filterItem != null)
            filterItem.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter:
                if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return true;
                //searchItem.collapseActionView();
                ((SearchView) searchItem.getActionView()).setQuery("", true);
                hideKeyboard();
                showpopup();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void hideKeyboard() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    private void showpopup() {
        if (mCoursesCategoryList.size() > 0) {
            popUpClass = new CategoryPopupWindow(getActivity(), mCoursesCategoryList);
            popUpClass.showPopupWindow(anchorView);
        }
    }

    @Override
    public void setPresenter(ExploreContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onShowProgress() {
        if (progressDialog != null) {
            progressDialog.show();
        }
    }

    @Override
    public void onHideProgress() {
        swipeLayout.setRefreshing(false);
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onShowAlertDialog(String message) {
        if (message.equalsIgnoreCase("No Internet")) {
            Intent noInternet = new Intent(getActivity(), NoInternetActivity.class);
            startActivity(noInternet);
        } else
            AppUtils.onShowAlertDialog(getActivity(), message);
    }

    @Override
    public void onServerError(String message) {
        AppUtils.onShowAlertDialog(getActivity(), getString(R.string.something_went_wrong_text));
    }

    @Override
    public void logoutAction(String message) {
        Intent userDeactivated = new Intent(getActivity(), InActiveUserActivity.class);
        userDeactivated.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(userDeactivated);
        getActivity().finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (response == null) {
            mShimmerViewContainer.startShimmerAnimation();
            OBLogger.e("IDS " + Prefs.getString(Constants.CATEGORY_IDS, ""));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mShimmerViewContainer.stopShimmerAnimation();
        mPresenter.stop();
        onHideProgress();
    }

    @Override
    public void onRefresh() {
        swipeLayout.setRefreshing(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (NetworkUtil.isConnected(getActivity())) {
                    fetchcourses();
                } else {
                    Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    //ExploreFragment.super.connectivityStatusChanged();
                }
            }
        }, 100);
    }

    private void fetchcourses() {
        String categoryIDS = "";
        if (popUpClass != null) {
            categoryIDS = popUpClass.getCategoryData();
        } else {
            popUpClass = new CategoryPopupWindow(getActivity(), mCoursesCategoryList);
            categoryIDS = popUpClass.getCategoryData();
        }
        searchItem.collapseActionView();
        response = null;
        //swipeLayout.setRefreshing(false);
        String finalCategoryIDS = categoryIDS;
        mPresenter.getCourses(getActivity(), finalCategoryIDS, 0);
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.getCourses(getActivity(), finalCategoryIDS, 0);
            }
        }, 100);*/
    }

    @Override
    public void onItemClicked(Object item) {
        if (NetworkUtil.isConnected(getActivity())) {
            if (((ExploreCourse) item).isEnroll()) {
                if (((ExploreCourse) item).getCourseItemType().equals("bundle")) {
                    Intent i = new Intent(getActivity(), BundleSubjectActivity.class);
                    i.putExtra(Constants.COURSE_ID, ((ExploreCourse) item).getID());
                    getActivity().startActivity(i);
                } else {
                    Intent contentDeliveryIntent = new Intent(getActivity(), ContentDeliveryActivity.class);
                    contentDeliveryIntent.putExtra(Constants.COURSE_ID, ((ExploreCourse) item).getID());
                    getActivity().startActivity(contentDeliveryIntent);
                }
            } else {
                //onShowProgress();
                Intent i = new Intent(getActivity(), TabsHeaderActivity.class);
                i.putExtra(Constants.COURSE_ID, ((ExploreCourse) item).getID());
                i.putExtra(Constants.TYPE, ((ExploreCourse) item).getCourseItemType());
                getActivity().startActivity(i);
            }
        } else {
            Toast.makeText(getActivity(), "Please check your internet connection!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
