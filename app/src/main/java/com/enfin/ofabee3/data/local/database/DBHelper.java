package com.enfin.ofabee3.data.local.database;

import com.enfin.ofabee3.data.remote.model.Response.LoginResponseModel;

/**
 * Created by SARATH on 21/8/19.
 */
public interface DBHelper {

    boolean insertUserData(LoginResponseModel.DataBean userModel);

    boolean isUserLoggedIn();

    String getAccessToken();

    String getUserID();

    LoginResponseModel.DataBean.UserBean getUserDetails();

    void deleteUserData();

    void updateUserDetails(LoginResponseModel.DataBean.UserBean user);

    void updateUserAvatar(String userId, String userImage);

    void close();

}
