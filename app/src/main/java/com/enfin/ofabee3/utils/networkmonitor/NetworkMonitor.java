package com.enfin.ofabee3.utils.networkmonitor;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.enfin.ofabee3.ui.module.nointernet.NoInternetActivity;
import com.enfin.ofabee3.utils.NoInternetDialog;
import com.enfin.ofabee3.utils.OBLogger;
import com.google.android.material.snackbar.Snackbar;
import com.shreyaspatil.MaterialDialog.MaterialDialog;

public class NetworkMonitor extends BroadcastReceiver {

    NetworkObserver networkObserver;
    private MaterialDialog mAnimatedDialog;
    private boolean isShown = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (isOnline(context)) {
                OBLogger.e("Online Connect Intenet ");
                //isShown = false;
            } else {
                /*if (!isShown)
                    showDialog();*/

                Intent intentone = new Intent(context.getApplicationContext(), NoInternetActivity.class);
                intentone.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentone);

                //networkObserver.connectivityStatusChanged();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void showDialog() {
        OBLogger.e("Conectivity Failure !!! ");
        networkObserver.connectivityStatusChanged();
        isShown = true;
    }

    private boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void recieverListener(NetworkObserver networkObserver) {
        this.networkObserver = networkObserver;
    }

}
