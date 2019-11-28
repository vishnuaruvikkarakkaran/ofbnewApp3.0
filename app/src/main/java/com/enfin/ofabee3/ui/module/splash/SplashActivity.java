package com.enfin.ofabee3.ui.module.splash;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.local.database.DatabaseContract;
import com.enfin.ofabee3.data.local.database.DatabasePresenter;
import com.enfin.ofabee3.data.local.database.OBDBHelper;
import com.enfin.ofabee3.data.local.sharedpreferences.OBPreferencesHelper;
import com.enfin.ofabee3.ui.base.BaseActivity;
import com.enfin.ofabee3.ui.module.home.HomeActivity;
import com.enfin.ofabee3.ui.module.login.LoginActivity;
import com.enfin.ofabee3.ui.module.nointernet.NoInternetActivity;
import com.enfin.ofabee3.utils.AppUtils;
import com.enfin.ofabee3.utils.Constants;
import com.enfin.ofabee3.utils.NetworkUtil;
import com.enfin.ofabee3.utils.OBLogger;
import com.enfin.ofabee3.data.local.sharedpreferences.SharedPreferenceData;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;

/**
 * Displays the splash screen.
 */

public class SplashActivity extends BaseActivity implements SplashContract.MvpView, DatabaseContract.Iview {

    private static final int RC_APP_UPDATE = 1001;
    private SplashPresenter splashPresenter;
    private Context mContext;
    private OBDBHelper dbHelper;
    private int mInterval = 5000;
    private Handler mHandler;
    private boolean isConnected = false;
    private AppUpdateManager mAppUpdateManager;
    private InstallStateUpdatedListener installStateUpdatedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAppDetails();
    }

   /* private void getAppDetails() {
        PackageInfo pinfo = null;
        try {
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            int versionNumber = pinfo.versionCode;
            String versionName = pinfo.versionName;
            OBLogger.e("VERSION " + versionName + " " + versionNumber);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }*/

    private void getAppDetails() {
        mAppUpdateManager = AppUpdateManagerFactory.create(this);

        mAppUpdateManager.registerListener(installStateUpdatedListener);

        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                try {
                    mAppUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.FLEXIBLE, SplashActivity.this, RC_APP_UPDATE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdate();
            } else {
                OBLogger.e("checkForAppUpdateAvailability: something else");
            }
        });

        installStateUpdatedListener = new
                InstallStateUpdatedListener() {
                    @Override
                    public void onStateUpdate(InstallState state) {
                        if (state.installStatus() == InstallStatus.DOWNLOADED) {
                            popupSnackbarForCompleteUpdate();
                        } else if (state.installStatus() == InstallStatus.INSTALLED) {
                            if (mAppUpdateManager != null) {
                                mAppUpdateManager.unregisterListener(installStateUpdatedListener);
                            }

                        } else {
                            OBLogger.e("InstallStateUpdatedListener: state: " + state.installStatus());
                        }
                    }
                };

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_APP_UPDATE) {
            if (resultCode != RESULT_OK) {
                OBLogger.e("onActivityResult: app download failed");
            }
        }
    }

    private void popupSnackbarForCompleteUpdate() {

        Snackbar snackbar =
                Snackbar.make(
                        findViewById(R.id.logo_splash_iv),
                        "New app is ready!",
                        Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction("Install", view -> {
            if (mAppUpdateManager != null) {
                mAppUpdateManager.completeUpdate();
            }
        });

        snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimaryDark));
        snackbar.show();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.bottom_bg_splash_iv).setVisibility(View.VISIBLE);
        findViewById(R.id.logo_splash_iv).setVisibility(View.VISIBLE);
        mContext = SplashActivity.this;
        splashPresenter = new SplashPresenter(this);
        dbHelper = new OBDBHelper(this);
        //Commented temporary because login api is not working so override to home
        checkConnectivity();
        //goToHome();
        mHandler = new Handler();
        startRepeatingTask();
        hideSystemUI();
    }

    /**
     * Method for checking internet connectivity is present or not.
     */
    private void checkConnectivity() {
        if (NetworkUtil.isConnected(this)) {
            isConnected = true;
            splashPresenter.getUserCountry(this);
        }
        //else
        //splashPresenter.showNoInternetDialog(this);
    }

    @Override
    public void openNextActivity() {
        if (dbHelper.isUserLoggedIn()) {
            goToHome();
        } else {
            goToLogin();
        }

        /*if (databasePresenter.isUserLoggedIn())
            splashPresenter.startHomePage(mContext);
        else
            splashPresenter.startActivityByLoginStatus(mContext);*/
        /*SharedPreferenceData preferenceData = new SharedPreferenceData(SplashActivity.this);
        OBLogger.d("USERID " + preferenceData.getString(Constants.USER_ID));
        if (TextUtils.isEmpty(preferenceData.getString(Constants.USER_ID)))
            splashPresenter.startHomePage(mContext);
        else
            splashPresenter.startActivityByLoginStatus(mContext);*/
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }


    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                if (!isConnected) {
                    checkConnectivity();
                }
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    private void goToHome() {
        if (NoInternetActivity.isActive) {
            Intent homeIntent = new Intent(SplashActivity.this, HomeActivity.class);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeIntent);
            finish();
        } else {
            Intent homeIntent = new Intent(SplashActivity.this, HomeActivity.class);
            //homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeIntent);
            finish();
        }
    }

    private void goToLogin() {
        if (NoInternetActivity.isActive) {
            Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(loginIntent);
            finish();
        } else {
            Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
            //loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(loginIntent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    @Override
    public void reloadPage() {
        checkConnectivity();
    }

    @Override
    public void onShowProgress() {
    }

    @Override
    public void onHideProgress() {
    }

    @Override
    public void onShowToast(String message) {
    }

    @Override
    public void onShowSnackBar(String message) {
    }

    @Override
    public void onShowAlertDialog(String message) {
        if (message.equalsIgnoreCase("No Internet")) {
            Intent noInternet = new Intent(this, NoInternetActivity.class);
            startActivity(noInternet);
        } else
            AppUtils.onShowAlertDialog(this, getString(R.string.something_went_wrong_text));
    }

    @Override
    public void onConnectivityError() {
        AppUtils.showToast(mContext, getString(R.string.check_internet_text));
    }

    @Override
    public void onRetry() {
    }

    @Override
    public void isSuccessfullyInserted(boolean status) {
    }

    @Override
    public void onDatabaseSuccessfullyDeleted() {
    }
}
