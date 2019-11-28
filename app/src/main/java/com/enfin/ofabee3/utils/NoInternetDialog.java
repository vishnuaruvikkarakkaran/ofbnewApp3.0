package com.enfin.ofabee3.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.enfin.ofabee3.R;

public class NoInternetDialog extends Dialog implements View.OnClickListener {

    private Button retry;
    private RetryListener retryListener;
    private Context mContext;

    public NoInternetDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.no_internet_dialog);
        setCancelable(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        retry = findViewById(R.id.btnRetry);
        retry.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRetry) {
            if (NetworkUtil.isConnected(mContext))
                retryListener.onRetry();
            else
                AppUtils.showToast(mContext,mContext.getString(R.string.check_internet_text));
        }
    }

    public void setRetryListener(RetryListener listener) {
        this.retryListener = listener;
    }

    public interface RetryListener {
        void onRetry();
    }
}



