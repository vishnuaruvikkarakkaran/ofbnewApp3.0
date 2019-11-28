package com.enfin.ofabee3.ui.module.coursedetail.bundledetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Html;
import android.widget.TextView;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.remote.model.coursedetail.response.BundleDetailResponseModel;
import com.enfin.ofabee3.data.remote.model.coursedetail.response.CourseDetailResponseModel;
import com.enfin.ofabee3.ui.base.BaseActivity;
import com.enfin.ofabee3.ui.module.coursedetail.CourseCurriculamAdapter;
import com.enfin.ofabee3.ui.module.coursedetail.CourseCurriculamItemListener;
import com.enfin.ofabee3.ui.module.coursedetail.CourseDetailFragment;
import com.enfin.ofabee3.ui.module.coursedetail.CourseDetailPresenter;
import com.enfin.ofabee3.ui.module.nointernet.NoInternetActivity;
import com.enfin.ofabee3.utils.AppUtils;
import com.enfin.ofabee3.utils.Constants;
import com.enfin.ofabee3.utils.OBLogger;

public class BundleDetailActivity extends BaseActivity implements CourseCurriculamItemListener, BundleDetailContract.View {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private Dialog progressDialog;
    private CourseDetailResponseModel bundleDetailResponseModel;
    private BundleDetailContract.Presenter mPresenter;
    private String courseID, itemType, title;
    private Toolbar mToolbar;
    private TextView toolbarTitle;
    private AppCompatImageView toolbarMenu;
    private AppCompatImageView toolbarBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = AppUtils.showProgressDialog(this);
        mPresenter = new BundleDetailPresenter(this, this);
        courseID = getIntent().getStringExtra(Constants.COURSE_ID);
        itemType = getIntent().getStringExtra(Constants.TYPE);
        title = getIntent().getStringExtra(Constants.SUBJECT_NAME);
        mToolbar = findViewById(R.id.toolbar);
        toolbarTitle = mToolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(Html.fromHtml(title));
        toolbarBack = findViewById(R.id.toolbar_back);
        toolbarBack.setOnClickListener(v -> finish());
        mPresenter.getBundleDetail(this, courseID, itemType);
        /*progressDialog = AppUtils.showProgressDialog(this);
        recyclerView = findViewById(R.id.curriculam_details_rv);
        courseDetailResponseModel = CourseDetailFragment.courseDetailResponseModel;
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        CourseCurriculamAdapter adapter = new CourseCurriculamAdapter(BundleDetailActivity.this, this, courseDetailResponseModel);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //Snackbar.make(rootview, "", BaseTransientBottomBar.LENGTH_SHORT).show();
                }
            }
        });*/
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_bundle_detail;
    }

    @Override
    public void itemClickListener(int position) {
    }

    @Override
    public <T> void onSuccess(T type) {
        onHideProgress();
        if (type instanceof CourseDetailResponseModel) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new CourseDetailFragment((CourseDetailResponseModel) type), "Topics")
                    .disallowAddToBackStack()
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public <T> void onFailure(T type) {
    }

    @Override
    public void setPresenter(BundleDetailContract.Presenter presenter) {

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
    public void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.stop();
    }
}
