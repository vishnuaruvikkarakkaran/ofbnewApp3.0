package com.enfin.ofabee3.ui.module.editprofile;

import android.content.Context;
import android.net.Uri;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.local.database.OBDBHelper;
import com.enfin.ofabee3.data.remote.ApiClient;
import com.enfin.ofabee3.data.remote.WebApiListener;
import com.enfin.ofabee3.data.remote.model.Request.LoginRequestModel;
import com.enfin.ofabee3.data.remote.model.Response.LoginResponseModel;
import com.enfin.ofabee3.data.remote.model.editprofile.request.EditprofileRequest;
import com.enfin.ofabee3.data.remote.model.editprofile.response.EditProfileResponse;
import com.enfin.ofabee3.data.remote.model.editprofile.response.ImageUploadResponse;
import com.enfin.ofabee3.ui.base.mvp.BasePresenter;
import com.enfin.ofabee3.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.net.UnknownHostException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfilePresenter extends BasePresenter implements EditProfileContract.Presenter {

    private Context mContext;
    private EditProfileContract.View mView;
    private LoginResponseModel.DataBean userBodyBean = new LoginResponseModel.DataBean();


    public EditProfilePresenter(Context mContext, EditProfileContract.View mView) {
        this.mContext = mContext;
        this.mView = mView;
    }

    @Override
    public void getUserDetails(Context context) {
        OBDBHelper obdbHelper = new OBDBHelper(context);
        LoginResponseModel.DataBean.UserBean user = obdbHelper.getUserDetails();
        userBodyBean.setUser(user);
        mView.onSuccess(user);
    }

    @Override
    public void updateuserdetails(Context context, LoginResponseModel.DataBean.UserBean user) {
        OBDBHelper obdbHelper = new OBDBHelper(context);
        obdbHelper.updateUserDetails(user);
    }

    @Override
    public void updateuserdetailsRemote(Context context, LoginResponseModel.DataBean.UserBean user) {
        mView.onShowProgress();

        EditprofileRequest editprofileRequest = new EditprofileRequest();
        editprofileRequest.setName(user.getUs_name());
        OBDBHelper obdbHelper = new OBDBHelper(context);
        String token = obdbHelper.getAccessToken();

        WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(editprofileRequest));
        Call<String> call = service.updateUserProfile(Constants.BEARER + token, requestBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                mView.onHideProgress();
                try {
                    if (response.code() == Constants.SUCCESS) {
                        if (response.body() != null) {
                            Type listType = new TypeToken<EditProfileResponse>() {
                            }.getType();
                            EditProfileResponse responseModel = new GsonBuilder().create().fromJson(response.body(), listType);

                            if (responseModel.getMetadata().getStatus_code() == Constants.SUCCESS) {
                                mView.onSuccess(responseModel);
                            } else
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
                mView.onHideProgress();
                if (t instanceof UnknownHostException)
                    mView.onShowAlertDialog("No Internet");
                else
                    mView.onServerError(t.getMessage());
            }
        });

    }

    @Override
    public void imageUpload(Context context, File file) {
        mView.onShowProgress();
        OBDBHelper obdbHelper = new OBDBHelper(context);
        String token = obdbHelper.getAccessToken();
        /*Create handle for the RetrofitInstance interface*/
        WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        Call<String> call = service.imageupload(Constants.BEARER + token, body);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                mView.onHideProgress();
                try {
                    if (response.code() == Constants.SUCCESS) {
                        if (response.body() != null) {
                            Type listType = new TypeToken<ImageUploadResponse>() {
                            }.getType();
                            ImageUploadResponse responseModel = new GsonBuilder().create().fromJson(response.body(), listType);

                            if (responseModel.getMetadata().getStatus_code() == (Constants.SUCCESS_FILE_UPLOAD)) {
                                mView.onSuccess(responseModel);
                            } else if (responseModel.getMetadata().getStatus_code() == (Constants.SUCCESS_FILE_UPLOAD)) {
                                mView.onShowAlertDialog(responseModel.getMetadata().getMessage());
                            } else
                                mView.onShowAlertDialog(responseModel.getMetadata().getMessage());

                        } else
                            mView.onShowAlertDialog(context.getString(R.string.something_went_wrong_text));

                    } else
                        mView.onShowAlertDialog(context.getString(R.string.something_went_wrong_text));

                            /*else if (response.code() == Constants.ERROR_401) {
                            if (response.errorBody() != null) {
                                Type listType = new TypeToken<LoginResponseModel>() {
                                }.getType();
                                LoginResponseModel responseModel = new GsonBuilder().create().fromJson(response.errorBody().string(), listType);
                                mView.onShowAlertDialog(responseModel.getHeader().getMessage());
                            } else
                                mView.onShowToast(context.getString(R.string.something_went_wrong_text));
                        }*/
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mView.onHideProgress();
                if (t instanceof UnknownHostException)
                    mView.onShowAlertDialog("No Internet");
                else {
                    mView.onServerError(t.getMessage());
                    //mView.onShowAlertDialog(t.getMessage());
                }
            }
        });

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

}
