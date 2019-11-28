package com.enfin.ofabee3.ui.module.coursecategories;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.remote.model.coursecategory.response.CourseCategoryResponse;
import com.enfin.ofabee3.data.remote.model.mycourses.response.NoDataFound;
import com.enfin.ofabee3.data.remote.model.savecategory.response.SaveCategoryResponse;
import com.enfin.ofabee3.ui.base.BaseFragment;
import com.enfin.ofabee3.ui.module.home.HomeActivity;
import com.enfin.ofabee3.ui.module.home.guesthome.GuestHomeActivity;
import com.enfin.ofabee3.ui.module.nointernet.NoInternetActivity;
import com.enfin.ofabee3.utils.AppUtils;
import com.enfin.ofabee3.utils.Constants;
import com.enfin.ofabee3.utils.OBLogger;
import com.enfin.ofabee3.utils.RecyclerViewItemClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hk.ids.gws.android.sclick.SClick;

public final class MyCourseCategoryListFragment extends BaseFragment implements MyCourseCategoryListContract.View, RecyclerViewItemClickListener {

    private MyCourseCategoryListContract.Presenter mPresenter;
    private RecyclerView mCoursesCategoryRV;
    private TextView noDataTV;
    private List<CourseCategoryResponse.DataBean> mCoursesCategoryList = new ArrayList<>();
    private Dialog progressDialog;
    private Button continueButton;
    private ArrayList<String> categoryIDList;
    private String categoryIDString;
    private String source;

    // Your presenter is available using the mPresenter variable
    public MyCourseCategoryListFragment() {
        // Required empty public constructor
    }

    public static MyCourseCategoryListFragment newInstance() {
        return new MyCourseCategoryListFragment();
    }

    @Override
    public void setPresenter(MyCourseCategoryListContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        source = getArguments().getString(Constants.USER_SOURCE);
        //if (source.equals("PROFILE"))
        //getActivity().getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        int layout;

        layout = R.layout.fragment_my_course_category_list_layout;

        /*if (source.equals("PROFILE"))
            layout = R.layout.fragment_my_course_category_list_layout_profile;
        else
            layout = R.layout.fragment_my_course_category_list_layout;*/

        View view = inflater.inflate(layout, container, false);
        categoryIDList = new ArrayList<>();
        /*Remove this after adding databinding*/
        mCoursesCategoryRV = view.findViewById(R.id.my_courses_rv);
        noDataTV = view.findViewById(R.id.no_course_found_tv);
        continueButton = view.findViewById(R.id.continueButton);
        progressDialog = AppUtils.showProgressDialog(getActivity());
        if (source.equals("LOGIN"))
            mPresenter.getCoursesCategoryWithoutHeader(getActivity());
        else
            mPresenter.getCoursesCategory(getActivity());
        /*Remove this after adding databinding*/
        clickActions();

        return view;
    }

    private void clickActions() {
        continueButton.setOnClickListener(view -> {
            if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return;
            if (categoryIDList.size() > 0) {
                categoryIDString = "";
                for (int i = 0; i < categoryIDList.size(); i++) {
                    if (!TextUtils.isEmpty(categoryIDList.get(i))) {
                        if (i == 0)
                            categoryIDString = categoryIDList.get(i);
                        else
                            categoryIDString = categoryIDString + "," + categoryIDList.get(i);
                    }
                }
                if (source.equals("LOGIN")) {
                    Intent guestHome = new Intent(getActivity(), GuestHomeActivity.class);
                    startActivity(guestHome);
                    getActivity().finish();
                } else
                    mPresenter.saveCoursesCategory(getActivity(), categoryIDString);
            } else {
                if (source.equals("LOGIN")) {
                    Intent guestHome = new Intent(getActivity(), GuestHomeActivity.class);
                    startActivity(guestHome);
                    getActivity().finish();
                } else
                    Toast.makeText(getActivity(), "Please select any of the category to continue", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean initRecyclerViews() {
        mCoursesCategoryRV.setVisibility(View.VISIBLE);
        CourseCategoryAdapter viewAdapter = new CourseCategoryAdapter(getContext(), mCoursesCategoryList, this::onItemSelected);
        mCoursesCategoryRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCoursesCategoryRV.setAdapter(viewAdapter);
        return true;
    }


    @Override
    public <T> void onSuccees(T type) {
        if (type instanceof CourseCategoryResponse) {
            CourseCategoryResponse responseModel = (CourseCategoryResponse) type;
            mCoursesCategoryList.addAll(responseModel.getData());
            initRecyclerViews();
        } else if (type instanceof NoDataFound) {
            NoDataFound responseModel = (NoDataFound) type;
            Toast.makeText(getActivity(), "Something went wrong !!", Toast.LENGTH_SHORT).show();
        } else if (type instanceof SaveCategoryResponse) {
            if (getActivity() != null) {
                Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
                //homeIntent.putStringArrayListExtra(Constants.CATEGORY_ID_LIST, categoryIDList);
                startActivity(homeIntent);
                getActivity().finish();
            }
        }
    }

    @Override
    public <T> void onFailure(T type) {
        if (type instanceof String) {
            String responseError = (String) type;
            OBLogger.e("RESPONSE ERROR " + responseError);
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
    public void onItemSelected(int position, boolean isSelected) {
        if (isSelected) {
            categoryIDList.add(mCoursesCategoryList.get(position).getId());
        } else {
            categoryIDList.removeAll(Arrays.asList(mCoursesCategoryList.get(position).getId()));
        }
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
    public interface OnMyCourseCategoryListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onMyCourseCategoryListFragmentInteraction();
    }

    @Override
    public void onDestroyView() {
        onHideProgress();
        super.onDestroyView();
    }

    @Override
    public void onStop() {
        onHideProgress();
        super.onStop();
    }
}
