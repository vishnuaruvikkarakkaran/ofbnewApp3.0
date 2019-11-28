package com.enfin.ofabee3.ui.module.contentdelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.Presentation;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.JsPromptResult;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.enfin.ofabee3.BuildConfig;
import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.local.database.OBDBHelper;
import com.enfin.ofabee3.ui.base.BaseActivity;
import com.enfin.ofabee3.utils.AppUtils;
import com.enfin.ofabee3.utils.Constants;
import com.enfin.ofabee3.utils.OBLogger;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import butterknife.BindView;

public class ContentDeliveryActivity extends BaseActivity implements ProgressDialogCallback, ViewReportCallback {

    @BindView(R.id.contentDeliveryWebView)
    WebView contentDeliveryView;

    private String courseId;
    private Dialog progressDialog;
    private String mCM;
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    private final static int FCR = 1;
    private WebView newWebView;
    private WebView holderWebView;
    private static boolean doubleBackToExitPressedOnce = false;
    private WebAppInterface webAppInterface;
    private String userAgent = "Mozilla/5.0 (Linux; Android 4.4; Nexus 5 Build/_BuildID_) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36";
    public static MediaPlayer audio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courseId = getIntent().getStringExtra("courseId");
        progressDialog = AppUtils.showProgressDialog(this);
        progressDialog.show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
            } else {
                // continue with your code
            }
        }
        WebSettings webSettings = contentDeliveryView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        WebView.setWebContentsDebuggingEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        OBDBHelper obdbHelper = new OBDBHelper(this);
        contentDeliveryView.clearCache(true);
        contentDeliveryView.loadUrl("file:///android_asset/index.html?host=" + BuildConfig.BASE_CD_HOST + "&token=" + obdbHelper.getAccessToken() + "#/" + courseId + "/0/resume");
        //contentDeliveryView.loadUrl("file:///android_asset/index.html?host=testing-ofabee.enfinlabs.com&token=" + obdbHelper.getAccessToken() + "#/" + courseId);
        //index.html?host=testing-ofabee.enfinlabs.com&token=<token>#/3/0/resume
        //contentDeliveryView.loadUrl("file:///android_asset/index.html?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjYiLCJlbWFpbF9pZCI6InZpbmkrbmF0YW5pYUBlbmZpbnRlY2hub2xvZ2llcy5jb20iLCJtb2JpbGUiOiI5NzY1NDMyMTI0In0.nSyziTwfyjmhOc7QAlmV5rfxvV0CMFSIORRObBAotOE#/3");
        webAppInterface = new WebAppInterface(this);
        webAppInterface.setProgressDialogCallbackListener(this);
        webAppInterface.setViewReportListener(this);
        contentDeliveryView.addJavascriptInterface(webAppInterface, "AndroidInterface");
        contentDeliveryView.setWebViewClient(new CustomWebViewClient());
        contentDeliveryView.setWebChromeClient(new CustomChromeClient());
        //contentDeliveryView.getSettings().setUserAgentString(userAgent);

        /*contentDeliveryView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
                OBLogger.e("DOWNLOAD " + url);
                contentDeliveryView.loadUrl(JavaScriptInterface.getBase64StringFromBlobUrl(url));
            }
        });*/

        contentDeliveryView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, UUID.randomUUID().toString());
            DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            dm.enqueue(request);
            Toast.makeText(getApplicationContext(), "Downloading File", //To notify the Client that the file is being downloaded
                    Toast.LENGTH_LONG).show();
            //contentDeliveryView.loadUrl(JavaScriptInterface.getBase64StringFromBlobUrl(url));
        });
    }

    @Override
    public int getLayout() {
        //Remove title
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTheme(R.style.HomeScreenTheme);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.homeNavigationBarColor));
            getWindow().setStatusBarColor(getResources().getColor(R.color.homeNavigationBarColor));
        }
        return R.layout.activity_content_delivery;
    }

    @Override
    public void onBackPressed() {
        if (newWebView != null) {
            if (newWebView.isShown()) {
                if (doubleBackToExitPressedOnce) {
                    newWebView.destroy();
                    holderWebView.removeView(newWebView);
                } else {
                    doubleBackToExitPressedOnce = true;
                    final Toast toast = Toast.makeText(getApplicationContext(), "Press Back Again To Exit", Toast.LENGTH_SHORT);
                    toast.show();
                    Handler handler = new Handler();
                    handler.postDelayed(toast::cancel, 500);
                }

            } else {
                WebBackForwardList mWebBackForwardList = contentDeliveryView.copyBackForwardList();
                if (mWebBackForwardList.getCurrentIndex() > 0) {

                    for (int i = 0; i < mWebBackForwardList.getSize(); i++) {
                        Log.e("history : ", mWebBackForwardList.getItemAtIndex(i).getUrl());
                    }
                }
                if (contentDeliveryView.canGoBack()) {
                    contentDeliveryView.goBack();
                } else {
                    finish();
                }
            }
        } else {
            WebBackForwardList mWebBackForwardList = contentDeliveryView.copyBackForwardList();
            if (mWebBackForwardList.getCurrentIndex() > 0) {

                for (int i = 0; i < mWebBackForwardList.getSize(); i++) {
                    Log.e("history : ", mWebBackForwardList.getItemAtIndex(i).getUrl());
                }
            }
            if (contentDeliveryView.canGoBack()) {
                contentDeliveryView.goBack();
            } else {
                finish();
            }
        }
    }

    /* @Override
 public boolean onKeyDown(int keyCode, KeyEvent event) {
     if (event.getAction() == KeyEvent.ACTION_DOWN) {
         switch (keyCode) {
             case KeyEvent.KEYCODE_BACK:
                 return true;
         }

     }
     return super.onKeyDown(keyCode, event);
 }*/
    @Override
    public void onProgressDismissed() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void isPlaying(boolean status) {
        if (status) {
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        } else {
            //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onDestroy() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
        super.onDestroy();
    }

    @Override
    public void openNewWindow(boolean status) {
        OBLogger.e("WILL TRY");
    }

    private class CustomWebViewClient extends WebViewClient {
       /* @Override
        public void onPageFinished(WebView view, String url) {
            Log.e("url : ", url);
            //Can call a java script function in html page.

            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();

            super.onPageFinished(view, url);
        }*/
    }

    private class CustomChromeClient extends WebChromeClient {
        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }


        //For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            mUM = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            ContentDeliveryActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FCR);
        }

        // For Android 3.0+, above method not supported in some android 3+ versions, in such case we use this
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            mUM = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            ContentDeliveryActivity.this.startActivityForResult(
                    Intent.createChooser(i, "File Browser"),
                    FCR);
        }

        //For Android 4.1+
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUM = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            ContentDeliveryActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), ContentDeliveryActivity.FCR);
        }

        //For Android 5.0+
        public boolean onShowFileChooser(
                WebView webView, ValueCallback<Uri[]> filePathCallback,
                WebChromeClient.FileChooserParams fileChooserParams) {


            if (mUMA != null) {
                mUMA.onReceiveValue(null);
            }
            mUMA = filePathCallback;
            /*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(takePictureIntent.resolveActivity(ContentDeliveryActivity.this.getPackageManager()) != null){
                File photoFile = null;
                try{
                    photoFile = createImageFile();
                    takePictureIntent.putExtra("PhotoPathnewWebView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
    DownloadManager.Request request = new DownloadManager.Request(
            Uri.parse(url));
    request.allowScanningByMediaScanner();
    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, UUID.randomUUID().toString());
    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
    dm.enqueue(request);
    Toast.makeText(getApplicationContext(), "Downloading File", //To notify the Client that the file is being downloaded
            Toast.LENGTH_LONG).show();", mCM);
                }catch(IOException ex){
                    Log.e("Error", "Image file creation failed", ex);
                }
                if(photoFile != null){
                    mCM = "file:" + photoFile.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                }else{
                    takePictureIntent = null;
                }
            }*/
            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
            contentSelectionIntent.setType("*/*");
            /*Intent[] intentArray;
            if(takePictureIntent != null){
                intentArray = new Intent[]{takePictureIntent};
            }else{
                intentArray = new Intent[0];newWebView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
    DownloadManager.Request request = new DownloadManager.Request(
            Uri.parse(url));
    request.allowScanningByMediaScanner();
    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, UUID.randomUUID().toString());
    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
    dm.enqueue(request);
    Toast.makeText(getApplicationContext(), "Downloading File", //To notify the Client that the file is being downloaded
            Toast.LENGTH_LONG).show();
            }

            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);*/
            startActivityForResult(contentSelectionIntent, FCR);
            return true;
        }

        // Create an image file
        private File createImageFile() throws IOException {
            @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "img_" + timeStamp + "_";
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            return File.createTempFile(imageFileName, ".jpg", storageDir);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            doubleBackToExitPressedOnce = false;
            holderWebView = view;
            newWebView = new WebView(ContentDeliveryActivity.this);
            newWebView.getSettings().setJavaScriptEnabled(true);
            newWebView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            newWebView.getSettings().setSupportMultipleWindows(true);
            newWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            holderWebView.addView(newWebView);
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(newWebView);
            resultMsg.sendToTarget();

            newWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest request) {

                    //* #Do not change this lines of code without getting prior approval this has some serious affects */
                    /*try {
                        OBLogger.e("URL " + request.getUrl());
                        webView.loadUrl(request.getUrl().toString());
                    } catch (Exception e) {
                        OBLogger.e("Exception " + e.getMessage());
                    }*/
                    return false;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    if (progressDialog != null)
                        progressDialog.show();
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    super.onPageFinished(view, url);
                }
            });

            newWebView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onCloseWindow(WebView window) {
                    Log.e("Window.close ", "Executed");
                    holderWebView.removeView(newWebView);
                    super.onCloseWindow(window);
                }
            });

            newWebView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, UUID.randomUUID().toString());
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(), "Downloading File", //To notify the Client that the file is being downloaded
                        Toast.LENGTH_LONG).show();
                holderWebView.removeView(newWebView);
            });
            return true;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        OBLogger.e("CHANGED");
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (Build.VERSION.SDK_INT >= 21) {
            Uri[] results = null;
            //Check if response is positive
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == FCR) {
                    if (null == mUMA) {
                        return;
                    }
                    if (intent == null) {
                        //Capture Photo if no image available
                        if (mCM != null) {
                            results = new Uri[]{Uri.parse(mCM)};
                        }
                    } else {
                        String dataString = intent.getDataString();
                        if (dataString != null) {
                            results = new Uri[]{Uri.parse(dataString)};
                        }
                    }
                }
            }
            mUMA.onReceiveValue(results);
            mUMA = null;
        } else {
            if (requestCode == FCR) {
                if (null == mUM) return;
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                mUM.onReceiveValue(result);
                mUM = null;
            }
        }
    }

    @Override
    protected void onResume() {
        //if (contentDeliveryView != null)
        //contentDeliveryView.reload();
        super.onResume();
    }
}
