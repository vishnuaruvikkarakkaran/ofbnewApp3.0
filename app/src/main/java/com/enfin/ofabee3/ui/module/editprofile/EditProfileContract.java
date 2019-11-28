package com.enfin.ofabee3.ui.module.editprofile;

import android.content.Context;

import com.enfin.ofabee3.data.remote.model.Response.LoginResponseModel;
import com.enfin.ofabee3.ui.base.mvp.BaseContract;

import java.io.File;

public interface EditProfileContract {

    interface View extends BaseContract.View<Presenter> {

        <T> void onSuccess(T type);

        <T> void onFailure(T type);

    }

    interface Presenter extends BaseContract.Presenter {
        void getUserDetails(Context context);

        void imageUpload(Context context, File file);

        void updateuserdetails(Context context, LoginResponseModel.DataBean.UserBean user);

        void updateuserdetailsRemote(Context context, LoginResponseModel.DataBean.UserBean user);
    }

}
