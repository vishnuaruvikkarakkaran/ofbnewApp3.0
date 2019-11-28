package com.enfin.ofabee3.ui.module.testpackage;

import android.view.View;

import com.enfin.ofabee3.utils.OBLogger;

public class TestPresenter implements TestContract.Presenter {

    private TestContract.View mView;
    private TestContract.Model model;

    public TestPresenter(TestContract.View mView, TestContract.Model model) {
        this.mView = mView;
        this.model = model;
    }

    @Override
    public void test() {
        OBLogger.e("PRINTED");
        try {
            model.setData("HELLO ");
            Thread.sleep(1000);
            mView.setViewData("JO");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

    }
}
