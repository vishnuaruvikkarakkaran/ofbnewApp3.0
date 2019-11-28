package com.enfin.ofabee3.ui.module.userprofile;

import android.content.Context;
import android.widget.Toast;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.local.database.OBDBHelper;
import com.enfin.ofabee3.data.remote.ApiClient;
import com.enfin.ofabee3.data.remote.WebApiListener;
import com.enfin.ofabee3.data.remote.model.Request.RequestModel;
import com.enfin.ofabee3.data.remote.model.Response.LoginResponseModel;
import com.enfin.ofabee3.data.remote.model.editprofile.response.ImageUploadResponse;
import com.enfin.ofabee3.data.remote.model.editprofile.response.MyProfileResponse;
import com.enfin.ofabee3.data.remote.model.logout.response.LogoutBean;
import com.enfin.ofabee3.ui.base.mvp.BasePresenter;
import com.enfin.ofabee3.utils.Constants;
import com.enfin.ofabee3.utils.NetworkUtil;
import com.enfin.ofabee3.utils.OBLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.UnknownHostException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfilePresenter extends BasePresenter implements UserProfileContract.Presenter {

    private UserProfileContract.View mView;
    private Context mContext;
    private String token;

    public UserProfilePresenter(Context mContext, UserProfileContract.View mView) {
        this.mView = mView;
        this.mContext = mContext;
        this.mView.setPresenter(this);
    }

    @Override
    public void getUserDetails(Context context) {
        OBDBHelper obdbHelper = new OBDBHelper(context);
        LoginResponseModel.DataBean.UserBean user = obdbHelper.getUserDetails();
        mView.onSuccess(user);
    }

    @Override
    public void logout(Context context) {
        mView.onShowProgress();
        OBDBHelper obdbHelper = new OBDBHelper(context);
        token = obdbHelper.getAccessToken();
        WebApiListener apiListener = ApiClient.getRetrofitInstance().create(WebApiListener.class);
        Call<String> apiCall = apiListener.logout(Constants.BEARER + token);
        if (NetworkUtil.isConnected(context)) {
            apiCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        if (response.code() == Constants.SUCCESS) {
                            if (response.body() != null) {
                                Type type = new TypeToken<LogoutBean>() {
                                }.getType();
                                LogoutBean logoutBean = new GsonBuilder().create()
                                        .fromJson(response.body(), type);
                                mView.onSuccess(logoutBean);
                            } else {
                                mView.onFailure(context.getString(R.string.something_went_wrong_text));
                            }

                        } else if (response.code() == Constants.ERROR_404) {
                            mView.onFailure(context.getString(R.string.service_not_found_text));
                        }
                    } catch (JsonSyntaxException exception) {
                        mView.onFailure("Exception found!!");
                    } finally {
                        mView.onHideProgress();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    mView.onHideProgress();
                    if (t instanceof UnknownHostException)
                        mView.onShowAlertDialog("No Internet");
                    else
                        mView.onServerError(t.getMessage());
                }
            });
        } else {
            Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void clearLocalDB(Context context) {
        OBDBHelper dbHelper = new OBDBHelper(context);
        dbHelper.deleteUserData();
        mView.onSuccess(Constants.DB_CLEAR);
    }

    @Override
    public void getProfileDeatails(Context context) {
        //mView.onShowProgress();
        OBDBHelper obdbHelper = new OBDBHelper(context);
        token = obdbHelper.getAccessToken();
        if (NetworkUtil.isConnected(context)) {
            WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);
            Call<String> call = service.myprofile(Constants.BEARER + token);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    //mView.onHideProgress();
                    try {
                        if (response.code() == Constants.SUCCESS) {
                            if (response.body() != null) {
                                Type listType = new TypeToken<MyProfileResponse>() {
                                }.getType();
                                MyProfileResponse responseModel = new GsonBuilder().create().fromJson(response.body(), listType);
                                OBLogger.e(responseModel.getMetadata().getStatus_code());
                                if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.SUCCESS))) {
                                    mView.onSuccess(responseModel);
                                }
                                else if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.ERROR_401))) {
                                    mView.logoutAction(responseModel.getMetadata().getMessage());
                                }
                                else
                                    mView.onShowAlertDialog(responseModel.getMetadata().getMessage());
                            } else
                                mView.onShowAlertDialog(context.getString(R.string.something_went_wrong_text));

                        } else
                            mView.onShowAlertDialog(context.getString(R.string.something_went_wrong_text));

                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    //mView.onHideProgress();
                    mView.onShowAlertDialog(t.getMessage());
                }
            });
        }
        else {
            Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }
}
