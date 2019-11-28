package com.enfin.ofabee3.ui.base.mvp;

/**
 * Created by SARATH on 20/8/19.
 */
public interface BaseContract {

    interface View<T extends Presenter> {
        void setPresenter(T presenter);

        void onShowProgress();

        void onHideProgress();

        void onShowAlertDialog(String message);

        abstract void onServerError(String message);
    }

    interface Presenter {
        void start();

        void stop();
    }
}
