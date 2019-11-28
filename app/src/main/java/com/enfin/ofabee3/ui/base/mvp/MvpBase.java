package com.enfin.ofabee3.ui.base.mvp;

public interface MvpBase {

    void onShowProgress();
    void onHideProgress();
    void onShowToast(String message);
    void onShowSnackBar(String message);
    void onShowAlertDialog(String message);
    void onConnectivityError();
    void onRetry();

}
