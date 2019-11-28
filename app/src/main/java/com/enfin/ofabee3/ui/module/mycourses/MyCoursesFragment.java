package com.enfin.ofabee3.ui.module.mycourses;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.remote.model.home.response.HomeResponse;
import com.enfin.ofabee3.data.remote.model.mycourses.response.NoDataFound;
import com.enfin.ofabee3.ui.base.BaseFragment;
import com.enfin.ofabee3.data.remote.model.mycourses.response.MyCoursesResponseModel;
import com.enfin.ofabee3.ui.module.home.HomeActivity;
import com.enfin.ofabee3.ui.module.home.HomeFragment;
import com.enfin.ofabee3.ui.module.home.HomePresenter;
import com.enfin.ofabee3.ui.module.inactiveuser.InActiveUserActivity;
import com.enfin.ofabee3.ui.module.nointernet.NoInternetActivity;
import com.enfin.ofabee3.utils.AppUtils;
import com.enfin.ofabee3.utils.Constants;
import com.enfin.ofabee3.utils.OBLogger;
import com.enfin.ofabee3.utils.OpenSansTextView;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

public class MyCoursesFragment extends BaseFragment implements MyCoursesContract.View,
        SwipeRefreshLayout.OnRefreshListener {

    RecyclerView myCoursesRV;
    OpenSansTextView noDataTV;
    List<MyCoursesResponseModel.DataBean> myCoursesList = new ArrayList<>();
    private MyCoursesContract.Presenter mPresenter;
    private Dialog progressDialog;
    private MyCoursesResponseModel myCoursesResponseModel;
    private SwipeRefreshLayout swipeLayout;
    private ShimmerFrameLayout mShimmerViewContainer;

    // Your presenter is available using the mPresenter variable
    public MyCoursesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public static MyCoursesFragment newInstance() {
        return new MyCoursesFragment();
    }

    @Override
    public void setPresenter(MyCoursesContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_course, container, false);
        //((HomeActivity)getActivity()).setToolbarTitle(getString(R.string.nav_my_course));
        myCoursesRV = v.findViewById(R.id.my_courses_rv);
        noDataTV = v.findViewById(R.id.no_course_found_tv);
        mShimmerViewContainer = v.findViewById(R.id.shimmer_view_container);
        swipeLayout = v.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(getActivity().getResources().getColor(android.R.color.holo_green_dark),
                getActivity().getResources().getColor(android.R.color.holo_red_dark),
                getActivity().getResources().getColor(android.R.color.holo_blue_dark),
                getActivity().getResources().getColor(android.R.color.holo_orange_dark));

        progressDialog = AppUtils.showProgressDialog(getActivity());
        mPresenter = new MyCoursesPresenter(getActivity(), this);
        //myCoursesResponseModel = (MyCoursesResponseModel) getArguments().getSerializable(Constants.MYCOURSE_DATA);
        return v;
    }

    private boolean showNodata(String message) {
        myCoursesRV.setVisibility(View.GONE);
        noDataTV.setText(message);
        noDataTV.setVisibility(View.VISIBLE);
        return false;
    }

    private boolean initRecyclerViews() {
        myCoursesRV.setVisibility(View.VISIBLE);
        MycoursesAdapter viewAdapter = new MycoursesAdapter(getContext(), myCoursesList);
        myCoursesRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        myCoursesRV.setAdapter(viewAdapter);
        return true;
    }

    @Override
    public <T> void onSuccees(T type) {
        if (type instanceof MyCoursesResponseModel) {
            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.GONE);
            myCoursesList.clear();
            swipeLayout.setRefreshing(false);
            MyCoursesResponseModel responseModel = (MyCoursesResponseModel) type;
            if (responseModel.getData() != null) {
                myCoursesList.addAll(responseModel.getData());
                myCoursesResponseModel = (MyCoursesResponseModel) type;
                if (getActivity() != null) {
                    ((HomeActivity) getActivity()).updateMyCourseData(responseModel);
                    initRecyclerViews();
                }
            } else {
                myCoursesList.clear();
                swipeLayout.setRefreshing(false);
                showNodata("No courses found");
                if (getActivity() != null) {
                    ((HomeActivity) getActivity()).updateMyCourseData(new MyCoursesResponseModel());
                    //initRecyclerViews();
                }
            }
        } else if (type instanceof NoDataFound) {
            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.GONE);
            myCoursesList.clear();
            swipeLayout.setRefreshing(false);
            NoDataFound responseModel = (NoDataFound) type;
            showNodata(responseModel.getMetadata().getMessage());
            if (getActivity() != null) {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                ((HomeActivity) getActivity()).updateMyCourseData(new MyCoursesResponseModel());
                //initRecyclerViews();
            }
        } else {
            myCoursesList.clear();
            swipeLayout.setRefreshing(false);
            showNodata("No courses found");
            if (getActivity() != null) {
                ((HomeActivity) getActivity()).updateMyCourseData(new MyCoursesResponseModel());
                //initRecyclerViews();
            }
        }
    }

    @Override
    public <T> void onFailure(T type) {
        mShimmerViewContainer.stopShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.GONE);
        if (type instanceof String) {
            myCoursesList.clear();
            swipeLayout.setRefreshing(false);
            String responseError = (String) type;
            OBLogger.e("RESPONSE ERROR " + responseError);
        } else if (type instanceof Boolean) {
            myCoursesList.clear();
            swipeLayout.setRefreshing(false);
            showNodata("No courses found");
            if (getActivity() != null) {
                ((HomeActivity) getActivity()).updateMyCourseData(new MyCoursesResponseModel());
                //initRecyclerViews();
            }
        }
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
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.search);
        MenuItem filterItem = menu.findItem(R.id.filter);
        if (item != null)
            item.setVisible(false);
        if (filterItem != null)
            filterItem.setVisible(false);
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
    public void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
        myCoursesResponseModel = ((HomeActivity) getActivity()).getMyCourseData();
        if (myCoursesResponseModel != null) {
            onSuccees(myCoursesResponseModel);
        } else {
            mPresenter.getcourses(getActivity());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mShimmerViewContainer.stopShimmerAnimation();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.stop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        OBLogger.e("MY COURSES");
    }

    @Override
    public void logoutAction(String message) {
        Intent userDeactivated = new Intent(getActivity(), InActiveUserActivity.class);
        userDeactivated.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(userDeactivated);
        getActivity().finish();
    }

    @Override
    public void onRefresh() {
        swipeLayout.setRefreshing(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.getcourses(getActivity());
            }
        }, 100);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnCoursesFragmentInteractionListener {
        // TODO: Update argument type and name
        void onCoursesFragmentInteraction();
    }
}
