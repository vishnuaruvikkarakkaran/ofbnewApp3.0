package com.enfin.ofabee3.ui.module.bundlesubject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.remote.model.coursedetail.response.BundleDetailResponseModel;
import com.enfin.ofabee3.ui.module.coursedetail.CourseDetailContract;
import com.enfin.ofabee3.ui.module.coursedetail.CourseDetailPresenter;
import com.enfin.ofabee3.ui.module.coursedetail.subject.SubjectFragment;
import com.enfin.ofabee3.ui.module.nointernet.NoInternetActivity;
import com.enfin.ofabee3.utils.AppUtils;
import com.enfin.ofabee3.utils.Constants;
import com.enfin.ofabee3.utils.OBLogger;

public class BundleSubjectActivity extends AppCompatActivity implements BundleSubjectContract.View {

    private BundleSubjectContract.Presenter mPresenter;
    private Dialog progressDialog;
    private String courseId;
    private AppCompatImageView toolbarBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bundle_subject);
        courseId = getIntent().getStringExtra("courseId");
        mPresenter = new BundleSubjectPresenter(this, BundleSubjectActivity.this);
        progressDialog = AppUtils.showProgressDialog(this);
        mPresenter.getCourseDetail(this, courseId, "bundle");
        toolbarBack = findViewById(R.id.toolbar_back);
        toolbarBack.setOnClickListener(v -> finish());
    }

    @Override
    public <T> void onSuccess(T type) {
        onHideProgress();
        if (type instanceof BundleDetailResponseModel) {
            if (((BundleDetailResponseModel) type).getData().getCourses().size() > 0)
                loadFragment((BundleDetailResponseModel) type);
            else
                Toast.makeText(this, "No subjects found in this bundle", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public <T> void onFailure(T type) {
    }

    @Override
    public void setPresenter(BundleSubjectContract.Presenter presenter) {
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
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
        mPresenter.stop();
    }

    private boolean loadFragment(BundleDetailResponseModel data) {
        OBLogger.e("loadFragment: " + data.getData().getCourses().get(0).getCb_title());
        runOnUiThread(() -> getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new SubjectFragment(data, Constants.PURCHASED), "subject")
                .commitAllowingStateLoss());
        return true;
    }
}
