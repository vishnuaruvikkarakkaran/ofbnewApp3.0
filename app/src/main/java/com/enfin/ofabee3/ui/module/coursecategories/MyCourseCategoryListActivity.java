package com.enfin.ofabee3.ui.module.coursecategories;

import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.ui.base.BaseActivity;
import com.enfin.ofabee3.utils.AppUtils;
import com.enfin.ofabee3.utils.Constants;

//todo create BaseActivity and import to this class
public class MyCourseCategoryListActivity extends BaseActivity implements MyCourseCategoryListFragment.OnMyCourseCategoryListFragmentInteractionListener, AppUtils.DialogActionListener {

    MyCourseCategoryListContract.Presenter mPresenter;
    private String source;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        source = getIntent().getStringExtra(Constants.USER_SOURCE);
        MyCourseCategoryListFragment myCourseCategoryListFragment = (MyCourseCategoryListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frame_layout_content);
        if (myCourseCategoryListFragment == null) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.USER_SOURCE, source);
            myCourseCategoryListFragment = MyCourseCategoryListFragment.newInstance();
            myCourseCategoryListFragment.setArguments(bundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.frame_layout_content, myCourseCategoryListFragment);
            transaction.commit();
        }
        mPresenter = new MyCourseCategoryListPresenter(this, myCourseCategoryListFragment);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_my_course_category_list_layout;
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
    }

    @Override
    public void onBackPressed() {
        if (source.equals("REGISTRATION")){
            AppUtils.showExitDialog(this, "Do you want to exit application ?", this);        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void onMyCourseCategoryListFragmentInteraction() {
    }

    @Override
    public void onPositiveButtonClick() {
        super.onBackPressed();
    }

    @Override
    public void onNegativeButtonClick() {

    }
}
