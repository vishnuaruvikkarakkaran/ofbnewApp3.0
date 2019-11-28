package com.enfin.ofabee3.ui.module.featuredcourses;

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
import com.enfin.ofabee3.data.remote.model.seeallcourses.response.SeeAllResponse;
import com.enfin.ofabee3.ui.base.BaseActivity;
import com.enfin.ofabee3.ui.base.baserecyclerview.OnRecyclerViewClickListener;
import com.enfin.ofabee3.ui.module.bundlesubject.BundleSubjectActivity;
import com.enfin.ofabee3.ui.module.contentdelivery.ContentDeliveryActivity;
import com.enfin.ofabee3.ui.module.coursedetail.TabsHeaderActivity;
import com.enfin.ofabee3.ui.module.home.featuredcourses.FeaturedCourse;
import com.enfin.ofabee3.ui.module.home.popularcourses.PopularCourse;
import com.enfin.ofabee3.ui.module.home.popularcourses.PopularCoursesAdapter;
import com.enfin.ofabee3.ui.module.nointernet.NoInternetActivity;
import com.enfin.ofabee3.ui.module.popularcourseslist.PopularCoursesListActivity;
import com.enfin.ofabee3.ui.module.popularcourseslist.PopularCoursesListContract;
import com.enfin.ofabee3.ui.module.popularcourseslist.PopularCoursesListPresenter;
import com.enfin.ofabee3.ui.module.popularcourseslist.pagination.PaginationScrollListener;
import com.enfin.ofabee3.utils.AppUtils;
import com.enfin.ofabee3.utils.Constants;
import com.enfin.ofabee3.utils.NetworkUtil;
import com.enfin.ofabee3.utils.OBLogger;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

public class FeaturedCoursesListActivity extends BaseActivity implements FeaturedCoursesListContract.View,
        OnRecyclerViewClickListener, SwipeRefreshLayout.OnRefreshListener {

    private FeaturedCoursesListContract.Presenter mPresenter;
    private Dialog progressDialog;
    private RecyclerView featuredRecyclerView;
    private PopularCoursesAdapter adapterFeaturedCourse;
    private SwipeRefreshLayout swipeLayout;
    private Toolbar mToolbar;
    private AppCompatImageView toolbarBack;

    //pagination fields
    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private LinearLayoutManager layoutManager;
    private int TOTAL_PAGES = 3;
    ProgressBar progressBar;
    private int currentPage = PAGE_START;
    List<PopularCourse> popularCoursesList;
    private ShimmerFrameLayout mShimmerViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        featuredRecyclerView = findViewById(R.id.featured_courses_rv);
        progressDialog = AppUtils.showProgressDialog(this);
        mPresenter = new FeaturedCoursesListPresenter(getApplicationContext(), this);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        getSupportActionBar().setTitle("");
        popularCoursesList = new ArrayList<>();
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
        //mPresenter.getfeaturedcourses(this, "");
    }


    private void paginationInit(LinearLayoutManager layoutManager) {
        featuredRecyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
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
        mPresenter.getfeaturedcourses(this, "", currentPage);
    }

    private void loadNextPage() {
        isLoading = false;
        if (currentPage != TOTAL_PAGES)
            mPresenter.getfeaturedcourses(this, "", currentPage);
        else
            isLastPage = true;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_featured_courses_list;
    }


    @Override
    public <T> void onSuccees(T type) {
        if (type instanceof SeeAllResponse) {
            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.GONE);
            swipeLayout.setRefreshing(false);
            TOTAL_PAGES = ((SeeAllResponse) type).getData().getTotal_records() / 8;
            if (currentPage == 0)
                loadPopularCourseList((SeeAllResponse) type);
            else
                updatePopularCourseList((SeeAllResponse) type);
        }

    }

    private void initPopularCourseView() {
        adapterFeaturedCourse = new PopularCoursesAdapter(this);
        adapterFeaturedCourse.setListener(this);
        layoutManager = new LinearLayoutManager(this);
        featuredRecyclerView.setAdapter(adapterFeaturedCourse);
        featuredRecyclerView.setLayoutManager(layoutManager);
        paginationInit(layoutManager);
    }

    private void loadPopularCourseList(SeeAllResponse data) {
        popularCoursesList.clear();
        for (int i = 0; i < data.getData().getList().size(); i++) {
            PopularCourse popularCourse = new PopularCourse(data.getData().getList().get(i));
            popularCourse.setMorePopularCourse((data.getData().getList().get(i)));
            popularCoursesList.add(popularCourse);
        }
        adapterFeaturedCourse.setItems(popularCoursesList);
    }

    private void updatePopularCourseList(SeeAllResponse data) {
        popularCoursesList.clear();
        adapterFeaturedCourse.clear();
        for (int i = 0; i < data.getData().getList().size(); i++) {
            PopularCourse popularCourse = new PopularCourse(data.getData().getList().get(i));
            popularCourse.setMorePopularCourse((data.getData().getList().get(i)));
            popularCoursesList.add(popularCourse);
        }
        adapterFeaturedCourse.setItems(popularCoursesList);
    }

    @Override
    public <T> void onFailure(T type) {
    }

    @Override
    public void setPresenter(FeaturedCoursesListContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onShowProgress() {
       /* if (progressDialog != null)
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
        mShimmerViewContainer.startShimmerAnimation();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
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
        if (NetworkUtil.isConnected(FeaturedCoursesListActivity.this)) {
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
            Toast.makeText(FeaturedCoursesListActivity.this, "Please check your internet connection!!", Toast.LENGTH_SHORT).show();
        }/*progressDialog = new ProgressDialog(this, R.style.ProgressDialogStyle);
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
        swipeLayout.setRefreshing(false);
        popularCoursesList.clear();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.getfeaturedcourses(FeaturedCoursesListActivity.this, "", currentPage);
            }
        }, 100);
    }

}
