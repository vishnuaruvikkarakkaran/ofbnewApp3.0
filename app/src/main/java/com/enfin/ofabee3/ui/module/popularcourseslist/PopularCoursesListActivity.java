package com.enfin.ofabee3.ui.module.popularcourseslist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.remote.model.home.response.HomeResponse;
import com.enfin.ofabee3.data.remote.model.seeallcourses.response.SeeAllResponse;
import com.enfin.ofabee3.ui.base.BaseActivity;
import com.enfin.ofabee3.ui.base.baserecyclerview.OnRecyclerViewClickListener;
import com.enfin.ofabee3.ui.module.bundlesubject.BundleSubjectActivity;
import com.enfin.ofabee3.ui.module.contentdelivery.ContentDeliveryActivity;
import com.enfin.ofabee3.ui.module.coursedetail.TabsHeaderActivity;
import com.enfin.ofabee3.ui.module.explore.ExploreCourse;
import com.enfin.ofabee3.ui.module.home.featuredcourses.FeaturedCourse;
import com.enfin.ofabee3.ui.module.home.popularcourses.PopularCourse;
import com.enfin.ofabee3.ui.module.home.popularcourses.PopularCoursesAdapter;
import com.enfin.ofabee3.ui.module.nointernet.NoInternetActivity;
import com.enfin.ofabee3.ui.module.popularcourseslist.pagination.PaginationScrollListener;
import com.enfin.ofabee3.utils.AppUtils;
import com.enfin.ofabee3.utils.Constants;
import com.enfin.ofabee3.utils.NetworkUtil;
import com.enfin.ofabee3.utils.OBLogger;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

public class PopularCoursesListActivity extends BaseActivity implements PopularCoursesListContract.View,
        OnRecyclerViewClickListener, SwipeRefreshLayout.OnRefreshListener {

    private PopularCoursesListContract.Presenter mPresenter;
    private Dialog progressDialog;
    private RecyclerView popularRecyclerView;
    private PopularCoursesAdapter adapterPopularCourse;
    private SwipeRefreshLayout swipeLayout;
    private Toolbar mToolbar;
    private AppCompatImageView toolbarBack;

    //pagination fields
    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private LinearLayoutManager layoutManager;
    private int TOTAL_PAGES = 1;
    ProgressBar progressBar;
    private int currentPage = PAGE_START;
    List<PopularCourse> popularCoursesList;
    LinkedHashSet<PopularCourse> popularCourseHashSet;
    private int scrollLimit;
    private ShimmerFrameLayout mShimmerViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        popularRecyclerView = findViewById(R.id.popular_courses_rv);
        progressDialog = AppUtils.showProgressDialog(this);
        mPresenter = new PopularCoursesListPresenter(getApplicationContext(), this);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        popularCoursesList = new ArrayList<>();
        popularCourseHashSet = new LinkedHashSet<>();
        toolbarBack = findViewById(R.id.toolbar_back);
        toolbarBack.setOnClickListener(v -> finish());
        swipeLayout = findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_red_dark),
                getResources().getColor(android.R.color.holo_blue_dark),
                getResources().getColor(android.R.color.holo_orange_dark));
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initPopularCourseView();
        //mPresenter.getpopularcourses(this, "", currentPage);
    }

    private void paginationInit(LinearLayoutManager layoutManager) {
        popularRecyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadmoreItems() {
                isLoading = true;
                currentPage += 1;
                OBLogger.e("TOTAL " + TOTAL_PAGES);
                OBLogger.e("current " + currentPage);
                if (currentPage <= TOTAL_PAGES)
                    loadNextPage();
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        loadFirstPage();
    }

    private void loadFirstPage() {
        OBLogger.e("firstpage");
        mPresenter.getpopularcourses(this, "", currentPage);
    }

    private void loadNextPage() {
        OBLogger.e("nextpage");
        isLoading = false;
        if (currentPage != TOTAL_PAGES)
            mPresenter.getpopularcourses(this, "", currentPage);
        else
            isLastPage = true;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_popular_courses_list;
    }

    @Override
    public <T> void onSuccees(T type) {
        if (type instanceof SeeAllResponse) {
            swipeLayout.setRefreshing(false);
            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.GONE);
            TOTAL_PAGES = ((SeeAllResponse) type).getData().getTotal_records() / 8;
            if (currentPage == 0)
                loadPopularCourseList((SeeAllResponse) type);
            else
                updatePopularCourseList((SeeAllResponse) type);
        }
    }

    private void initPopularCourseView() {
        adapterPopularCourse = new PopularCoursesAdapter(this);
        adapterPopularCourse.setListener(this);
        layoutManager = new LinearLayoutManager(this);
        popularRecyclerView.setAdapter(adapterPopularCourse);
        popularRecyclerView.setLayoutManager(layoutManager);
        paginationInit(layoutManager);
    }

    private void loadPopularCourseList(SeeAllResponse data) {
        popularCoursesList.clear();
        OBLogger.e("loading");
        for (int i = 0; i < data.getData().getList().size(); i++) {
            PopularCourse popularCourse = new PopularCourse(data.getData().getList().get(i));
            popularCourse.setMorePopularCourse((data.getData().getList().get(i)));
            popularCoursesList.add(popularCourse);
        }
        popularCourseHashSet.addAll(popularCoursesList);
        adapterPopularCourse.setItems(popularCoursesList);
    }

    private void updatePopularCourseList(SeeAllResponse data) {
        OBLogger.e("updating");
        //popularCoursesList.clear();
        for (int i = 0; i < data.getData().getList().size(); i++) {
            PopularCourse popularCourse = new PopularCourse(data.getData().getList().get(i));
            popularCourse.setMorePopularCourse((data.getData().getList().get(i)));
            popularCoursesList.add(popularCourse);
        }
        popularCourseHashSet.addAll(popularCoursesList);
        adapterPopularCourse.setItems(popularCoursesList);
    }

    @Override
    public <T> void onFailure(T type) {
        mShimmerViewContainer.stopShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.GONE);
    }

    @Override
    public void setPresenter(PopularCoursesListContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onShowProgress() {
        /*if (progressDialog != null)
            progressDialog.show();*/
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
    protected void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
        mPresenter.stop();
    }

    @Override
    public void onItemClicked(Object item) {
        if (NetworkUtil.isConnected(PopularCoursesListActivity.this)) {
            if (((PopularCourse) item).isEnrolled()) {
                if (((PopularCourse) item).getCourseItemType().equals("bundle")) {
                    Intent i = new Intent(this, BundleSubjectActivity.class);
                    i.putExtra(Constants.COURSE_ID, ((PopularCourse) item).getID());
                    startActivity(i);
                } else {
                    Intent contentDeliveryIntent = new Intent(this, ContentDeliveryActivity.class);
                    contentDeliveryIntent.putExtra(Constants.COURSE_ID, ((PopularCourse) item).getID());
                    startActivity(contentDeliveryIntent);
                }
            } else {
                //onShowProgress();
                Intent i = new Intent(this, TabsHeaderActivity.class);
                i.putExtra(Constants.COURSE_ID, ((PopularCourse) item).getID());
                i.putExtra(Constants.TYPE, ((PopularCourse) item).getCourseItemType());
                startActivity(i);
            }
        } else {
            Toast.makeText(PopularCoursesListActivity.this, "Please check your internet connection!!", Toast.LENGTH_SHORT).show();
        }
        /*progressDialog = new ProgressDialog(this, R.style.ProgressDialogStyle);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();
        Intent i = new Intent(this, TabsHeaderActivity.class);
        i.putExtra(Constants.COURSE_ID, ((PopularCourse) item).getID());
        i.putExtra(Constants.TYPE, ((PopularCourse) item).getCourseItemType());
        startActivity(i);*/
        //Toast.makeText(this, "Please contact admin for subscription", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        currentPage = 0;
        popularCoursesList.clear();
        popularCourseHashSet.clear();
        swipeLayout.setRefreshing(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.getpopularcourses(PopularCoursesListActivity.this, "", currentPage);
            }
        }, 100);
    }

    @Override
    protected void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }

}
