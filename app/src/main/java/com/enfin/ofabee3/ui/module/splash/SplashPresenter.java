package com.enfin.ofabee3.ui.module.splash;

import android.content.Context;

import com.enfin.ofabee3.data.local.sharedpreferences.OBPreferencesHelper;
import com.enfin.ofabee3.data.remote.model.Response.UserLocationModel;
import com.enfin.ofabee3.data.remote.ApiClient;
import com.enfin.ofabee3.utils.Constants;
import com.enfin.ofabee3.utils.NoInternetDialog;
import com.enfin.ofabee3.data.remote.WebApiListener;

import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Responsible for handling actions from the view and updating the UI
 * as required.
 */

public class SplashPresenter implements SplashContract.Presenter, NoInternetDialog.RetryListener {

    private SplashContract.MvpView mvpView;
    private OBPreferencesHelper obPreferencesHelper;

    public SplashPresenter(SplashContract.MvpView view) {
        this.mvpView = view;
    }

    /////      Presenter Methods      /////

    @Override
    public void getUserCountry(Context context) {
        /*Create handle for the RetrofitInstance interface*/
        obPreferencesHelper = new OBPreferencesHelper(context);
        WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);
        Call<UserLocationModel> call = service.getLocation();
        call.enqueue(new Callback<UserLocationModel>() {
            @Override
            public void onResponse(Call<UserLocationModel> call, Response<UserLocationModel> response) {
                if (response.code() == Constants.SUCCESS) {
                    if (!response.body().getMetadata().isError()) {
                        obPreferencesHelper.setCurrentUserCountry(response.body().getData().getLocation_name());
                        mvpView.openNextActivity();
                    } else
                        mvpView.onShowAlertDialog(response.body().getMetadata().getMessage());
                } else if (response.code() == Constants.ERROR_401 || response.code() == Constants.ERROR_404)
                    mvpView.onShowAlertDialog(response.body().getMetadata().getMessage());
            }

            @Override
            public void onFailure(Call<UserLocationModel> call, Throwable t) {
                if (t instanceof UnknownHostException)
                    mvpView.onShowAlertDialog("No Internet");
                else
                    mvpView.onShowAlertDialog(t.getMessage());
            }
        });
    }

    @Override
    public void showNoInternetDialog(Context context) {
        NoInternetDialog dialog = new NoInternetDialog(context);
        dialog.setRetryListener(this);
        dialog.show();
    }

    //No internet dialog retry button click call back method.
    @Override
    public void onRetry() {
        mvpView.reloadPage();
    }
}
