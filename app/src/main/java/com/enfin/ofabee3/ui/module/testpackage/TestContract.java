package com.enfin.ofabee3.ui.module.testpackage;

public interface TestContract {

    interface View {
        void initView();

        void setViewData(String data);
    }

    interface Model {
        void setData(String data);
        String getData();
    }

    interface Presenter {
        void test();
        void onClick(android.view.View view);
    }

}
