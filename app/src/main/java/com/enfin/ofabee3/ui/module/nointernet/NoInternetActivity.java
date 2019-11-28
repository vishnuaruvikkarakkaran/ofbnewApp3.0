package com.enfin.ofabee3.ui.module.nointernet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.ui.base.BaseActivity;
import com.shreyaspatil.MaterialDialog.AbstractDialog;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;
import com.shreyaspatil.MaterialDialog.MaterialDialog;

public class NoInternetActivity extends BaseActivity {

    private BottomSheetMaterialDialog mAnimatedDialog;
    public static boolean isActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showAnimatedDialog();
        isActive = true;
        //hideSystemUI();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_no_internet;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void showAnimatedDialog() {
        // Animated Simple Material Dialog
        mAnimatedDialog = new BottomSheetMaterialDialog.Builder(this)
                .setTitle("No Internet Connection")
                .setMessage("Please check your internet connection!")
                .setCancelable(false)
                .setPositiveButton("Retry", (MaterialDialog.OnClickListener) (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    BaseActivity.dialogDismissed = true;
                    Toast.makeText(getApplicationContext(), "Trying to Connect to the Internet...", Toast.LENGTH_SHORT).show();
                    //if (isOnline(getApplicationContext())) {
                    finish();
                    // }
                })
                .setNegativeButton("Dismiss", (dialogInterface, which) -> {
                    dialogInterface.dismiss();
                    BaseActivity.dialogDismissed = true;
                    finish();
                })
                //.setAnimation("no_internet.json")
                .build();
        mAnimatedDialog.show();
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


    @Override
    protected void onDestroy() {
        mAnimatedDialog.dismiss();
        super.onDestroy();
        isActive = false;
    }
}
