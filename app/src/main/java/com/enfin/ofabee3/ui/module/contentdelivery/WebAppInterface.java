package com.enfin.ofabee3.ui.module.contentdelivery;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.enfin.ofabee3.utils.OBLogger;

public class WebAppInterface {

    private Context mContext;
    private ProgressDialogCallback progressDialogCallback;
    private ViewReportCallback viewReportCallback;

    // Instantiate the interface and set the context
    WebAppInterface(Context context) {
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
    public void isPlaying(boolean status) {
        if (progressDialogCallback != null) {
            if (status) {
                progressDialogCallback.isPlaying(true);
            } else {
                progressDialogCallback.isPlaying(false);
            }

        }
    }

    @JavascriptInterface
    public void progressDismiss() {
        Log.e("Dismiss ", "Dialog");
        if (progressDialogCallback != null)
            progressDialogCallback.onProgressDismissed();
    }

    public void setProgressDialogCallbackListener(ProgressDialogCallback listener) {
        this.progressDialogCallback = listener;
    }

    public void setViewReportListener(ViewReportCallback viewReportCallback) {
        this.viewReportCallback = viewReportCallback;
    }
}





