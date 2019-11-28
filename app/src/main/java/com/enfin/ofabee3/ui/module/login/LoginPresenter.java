package com.enfin.ofabee3.ui.module.login;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.util.Log;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.local.database.DatabaseHandler;
import com.enfin.ofabee3.data.local.database.OBDBHelper;
import com.enfin.ofabee3.data.local.sharedpreferences.OBPreferencesHelper;
import com.enfin.ofabee3.data.remote.model.Request.LoginRequestModel;
import com.enfin.ofabee3.data.remote.model.Response.LoginResponseModel;
import com.enfin.ofabee3.data.remote.ApiClient;
import com.enfin.ofabee3.utils.AppUtils;
import com.enfin.ofabee3.utils.Constants;
import com.enfin.ofabee3.utils.NetworkUtil;
import com.enfin.ofabee3.utils.NoInternetDialog;
import com.enfin.ofabee3.data.local.sharedpreferences.SharedPreferenceData;
import com.enfin.ofabee3.data.remote.WebApiListener;
import com.enfin.ofabee3.utils.OBLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.UnknownHostException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.enfin.ofabee3.utils.Constants.KEY_ACCESS_TOKEN;
import static com.enfin.ofabee3.utils.Constants.KEY_EMAIL_VERIFIED;
import static com.enfin.ofabee3.utils.Constants.KEY_USER_EMAIL;
import static com.enfin.ofabee3.utils.Constants.KEY_USER_ID;
import static com.enfin.ofabee3.utils.Constants.KEY_USER_IMAGE;
import static com.enfin.ofabee3.utils.Constants.KEY_USER_NAME;
import static com.enfin.ofabee3.utils.Constants.KEY_USER_PHONE_NUMBER;
import static com.enfin.ofabee3.utils.Constants.KEY_USER_STATUS;
import static com.enfin.ofabee3.utils.Constants.TABLE_USER;

/**
 * Responsible for handling actions from the view and updating the UI
 * as required.
 */

public class LoginPresenter implements LoginContract.Presenter, NoInternetDialog.RetryListener {

    private LoginContract.MvpView mView;


    LoginPresenter(LoginContract.MvpView view) {
        this.mView = view;
    }

    @Override
    public void checkUserCountry(Context context) {
        mView.onUserCountry(new OBPreferencesHelper(context).getCurrentUserCountry());
    }

    @Override
    public void showNoConnectivityDialog(Context context) {
        NoInternetDialog dialog = new NoInternetDialog(context);
        dialog.setRetryListener(this);
        dialog.show();
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean validateEmailFormat(String email) {
        return AppUtils.isValidEmail(email);
    }

    @Override
    public void validateEmailAndPasswordForLogin(Context context, String email, String password, boolean isValidEmail) {

        if (email.length() == 0) {
            mView.onShowErrorEmailAlert(context.getString(R.string.email_empty_text));
        } else if (!isValidEmail) {
            mView.onShowErrorEmailAlert(context.getString(R.string.email_invalid_text));
        } else if (password.length() == 0) {
            mView.onShowErrorPasswordAlert(context.getString(R.string.password_empty_text));
        } else if (password.length() < 6) {
            mView.onShowErrorPasswordAlert(context.getString(R.string.password_format_error_text));
        } else {
            //mView.onShowToast("valid email and password");
            loginByEmail(context, email, password);
        }
    }

    @Override
    public void validateNumberAndPasswordForLogin(Context mContext, String countryCode, String number, String password) {
        if (countryCode.length() == 0) {
            mView.onShowAlertDialog(mContext.getString(R.string.something_went_wrong_text));
        } else if (number.length() == 0) {
            mView.onShowErrorNumberAlert(mContext.getString(R.string.number_empty_text));
        }  if (password.length() == 0) {
            mView.onShowErrorPasswordAlert(mContext.getString(R.string.password_empty_text));
        }  if (password.length() < 6) {
            mView.onShowErrorPasswordAlert(mContext.getString(R.string.password_format_error_text));
        } else {
            loginByPhoneNumber(mContext, countryCode, number, password);
        }
    }

    @Override
    public void startActivity(Context context, String country, Class<?> intentClass) {
        Intent intent = new Intent(context, intentClass);
        intent.putExtra(Constants.COUNTRY, country);
        context.startActivity(intent);
    }

    //No internet dialog retry button click call back method.
    @Override
    public void onRetry() {
        mView.onRetry();
    }

    private void loginByEmail(Context context, String email, String password) {
        if (NetworkUtil.isConnected(context)) {
            mView.onShowProgress();

            LoginRequestModel loginRequestModel = new LoginRequestModel();
            loginRequestModel.setPhone(null);
            loginRequestModel.setEmail(email);
            loginRequestModel.setMode("2");
            loginRequestModel.setPassword(password);

            WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(loginRequestModel));
            Call<String> call = service.userLogin(requestBody);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    mView.onHideProgress();
                    try {
                        if (response.code() == Constants.SUCCESS) {
                            if (response.body() != null) {
                                Type listType = new TypeToken<LoginResponseModel>() {
                                }.getType();
                                LoginResponseModel responseModel = new GsonBuilder().create().fromJson(response.body(), listType);
                                if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.SUCCESS))) {
                                    mView.onLoginSuccess(responseModel);
                                } else if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.ERROR_401))) {
                                    mView.onShowAlertDialog(responseModel.getMetadata().getMessage());
                                } else
                                    mView.onShowAlertDialog(responseModel.getMetadata().getMessage());
                            } else
                                mView.onShowToast(context.getString(R.string.something_went_wrong_text));

                        } else
                            mView.onShowToast(context.getString(R.string.something_went_wrong_text));

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
                    else
                        mView.onShowAlertDialog(t.getMessage());
                }
            });
        } else {
            mView.onShowSnackBar(context.getString(R.string.check_internet_text));
        }

    }

    private void loginByPhoneNumber(Context context, String countryCode, String phoneNumber, String password) {
        if (NetworkUtil.isConnected(context)) {
            mView.onShowProgress();

            LoginRequestModel loginRequestModel = new LoginRequestModel();
            loginRequestModel.setPhone(phoneNumber);
            loginRequestModel.setEmail(null);
            loginRequestModel.setMode("1");
            loginRequestModel.setPassword(password);

            WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(loginRequestModel));
            Call<String> call = service.userLogin(requestBody);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    mView.onHideProgress();
                    try {
                        if (response.code() == Constants.SUCCESS) {
                            if (response.body() != null) {
                                Type listType = new TypeToken<LoginResponseModel>() {
                                }.getType();
                                LoginResponseModel responseModel = new GsonBuilder().create().fromJson(response.body(), listType);

                                if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.SUCCESS))) {
                                    mView.onLoginSuccess(responseModel);
                                } else if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.ERROR_401))) {
                                    mView.onShowAlertDialog(responseModel.getMetadata().getMessage());
                                } else
                                    mView.onShowAlertDialog(responseModel.getMetadata().getMessage());

                            } else
                                mView.onShowToast(context.getString(R.string.something_went_wrong_text));

                        } else
                            mView.onShowToast(context.getString(R.string.something_went_wrong_text));

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
                    else
                        mView.onShowAlertDialog(t.getMessage());
                }
            });
        } else {
            mView.onShowSnackBar(context.getString(R.string.check_internet_text));
        }
    }

    public void insertUserData(Context context, LoginResponseModel.DataBean userModel) {
        OBDBHelper dbHelper = new OBDBHelper(context);
        if (dbHelper.insertUserData(userModel)) {
            mView.isSuccessfullyInserted();
        } else {
            mView.insertionFailed();
        }
    }
}
