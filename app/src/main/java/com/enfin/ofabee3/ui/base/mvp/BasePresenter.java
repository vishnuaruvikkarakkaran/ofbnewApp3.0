package com.enfin.ofabee3.ui.base.mvp;

import android.content.Context;

import com.enfin.ofabee3.utils.OBLogger;

import javax.annotation.Nonnull;

public abstract class BasePresenter {

    protected Context mContext;

    public void subscribe(@Nonnull Context context) {
        this.mContext = context;
    }

    public boolean isSubscribed () {
        return mContext != null;
    }

}
