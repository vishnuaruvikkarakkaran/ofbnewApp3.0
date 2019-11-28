package com.enfin.ofabee3.ui.module.contentdelivery;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.enfin.ofabee3.utils.OBLogger;

public class AndroidAppInterface {

    private Context mContext;
    private ProgressDialogCallback progressDialogCallback;

    // Instantiate the interface and set the context
    AndroidAppInterface(Context context) {
        this.mContext = context;
    }


    // Finish activity from the web page
    @JavascriptInterface
    public void finish() {
        OBLogger.e("CLOSRE WINDOW");
        ((Activity) mContext).finish();
    }

    @JavascriptInterface
    public void openWindow() {
        OBLogger.e("NEW WINDOW");
    }

    @JavascriptInterface
    public void progressDismiss() {
        Log.e("Dismiss ", "Dialog");
        if (progressDialogCallback != null)
            progressDialogCallback.onProgressDismissed();
    }

    @JavascriptInterface
    public void isPlaying(boolean status) {
        OBLogger.e("Video Playing");
    }

    public void setProgressDialogCallbackListener() {
    }
}





