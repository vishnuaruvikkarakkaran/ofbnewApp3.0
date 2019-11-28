package com.enfin.ofabee3.ui.module.coursedetail;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.remote.model.coursedetail.response.CourseDetailResponseModel;
import com.enfin.ofabee3.data.remote.model.selfenroll.response.SelfEnrollResponse;
import com.enfin.ofabee3.ui.module.login.LoginActivity;
import com.enfin.ofabee3.ui.module.nointernet.NoInternetActivity;
import com.enfin.ofabee3.utils.AppUtils;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class CourseDetailFragment extends Fragment implements CourseCurriculamItemListener, CourseDetailContract.View {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private Dialog progressDialog;
    private CourseDetailContract.Presenter mPresenter;
    public static CourseDetailResponseModel courseDetailResponseModel;

    public CourseDetailFragment() {
        // Required empty public constructor
    }

    public CourseDetailFragment(CourseDetailResponseModel data) {
        this.courseDetailResponseModel = data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_course_detail, container, false);
        progressDialog = AppUtils.showProgressDialog(getActivity());
        mPresenter = new CourseDetailPresenter(this, getActivity());
        recyclerView = rootview.findViewById(R.id.curriculam_details_rv);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        CourseCurriculamAdapter adapter = new CourseCurriculamAdapter(getActivity(), this, courseDetailResponseModel);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //Snackbar.make(rootview, "", BaseTransientBottomBar.LENGTH_SHORT).show();
                }
            }
        });
        return rootview;
    }

    @Override
    public void itemClickListener(int position) {
        recyclerView.getLayoutManager().scrollToPosition(position);
    }

    @Override
    public <T> void onSuccess(T type) {
        if (type instanceof CourseDetailResponseModel) {
            Snackbar.make(recyclerView, "Success", BaseTransientBottomBar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSuccessSelfEnroll(SelfEnrollResponse response) {
    }

    @Override
    public <T> void onFailure(T type) {
        Snackbar.make(recyclerView, "Error", BaseTransientBottomBar.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(CourseDetailContract.Presenter presenter) {

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
    public void invokeLogin() {
        onHideProgress();
        //Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
        //loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //startActivityForResult(loginIntent, 1001);
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

    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.stop();
    }

}
