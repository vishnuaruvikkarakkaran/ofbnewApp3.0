package com.enfin.ofabee3.ui.module.splash;

import android.content.Context;

import com.enfin.ofabee3.ui.base.mvp.MvpBase;

/**
 * Created By Athul on 14-07-2019.
 * Defines the contract between the view {@link SplashActivity} and the Presenter
 * {@link SplashPresenter}.
 */
public interface SplashContract {
    interface MvpView extends MvpBase {
        void openNextActivity();

        void reloadPage();
    }

    interface Presenter {
        void getUserCountry(Context context);

        void showNoInternetDialog(Context context);
    }
}
