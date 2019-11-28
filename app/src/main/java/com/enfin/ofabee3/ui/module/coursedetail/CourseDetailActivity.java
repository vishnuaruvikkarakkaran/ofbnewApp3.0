package com.enfin.ofabee3.ui.module.coursedetail;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.remote.model.selfenroll.response.SelfEnrollResponse;
import com.enfin.ofabee3.ui.base.BaseActivity;

public class CourseDetailActivity extends BaseActivity implements CourseDetailContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_course_detail;
    }

    @Override
    public void setPresenter(CourseDetailContract.Presenter presenter) {
    }

    @Override
    public void onShowProgress() {
    }

    @Override
    public void onHideProgress() {
    }

    @Override
    public void onShowAlertDialog(String message) {
    }

    @Override
    public void onServerError(String message) {

    }

    @Override
    public <T> void onSuccess(T type) {
    }

    @Override
    public void invokeLogin() {

    }

    @Override
    public void onSuccessSelfEnroll(SelfEnrollResponse response) {
    }

    @Override
    public <T> void onFailure(T type) {
    }
}
