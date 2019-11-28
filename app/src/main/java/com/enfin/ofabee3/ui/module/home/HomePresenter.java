package com.enfin.ofabee3.ui.module.home;

import android.content.Context;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.local.database.OBDBHelper;
import com.enfin.ofabee3.data.remote.ApiClient;
import com.enfin.ofabee3.data.remote.WebApiListener;
import com.enfin.ofabee3.data.remote.model.Request.LoginRequestModel;
import com.enfin.ofabee3.data.remote.model.Response.LoginResponseModel;
import com.enfin.ofabee3.data.remote.model.home.request.HomeRequest;
import com.enfin.ofabee3.data.remote.model.home.response.HomeResponse;
import com.enfin.ofabee3.data.remote.model.mycourses.response.MyCoursesResponseModel;
import com.enfin.ofabee3.data.remote.model.mycourses.response.NoDataFound;
import com.enfin.ofabee3.ui.base.mvp.BasePresenter;
import com.enfin.ofabee3.ui.module.mycourses.MyCoursesContract;
import com.enfin.ofabee3.utils.Constants;
import com.enfin.ofabee3.utils.OBLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.UnknownHostException;

import javax.annotation.Nonnull;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePresenter extends BasePresenter implements HomeContract.Presenter {

    private HomeContract.View mView;
    private Context mContext;

    public HomePresenter(@Nonnull Context context, @Nonnull HomeContract.View view) {
        this.mView = view;
        this.mContext = context;
        this.mView.setPresenter(this);
    }

    @Override
    public void getHomeData(Context context) {
        mView.onShowProgress();
        OBDBHelper obdbHelper = new OBDBHelper(context);
        String token = obdbHelper.getAccessToken();
        HomeRequest homeRequest = new HomeRequest();
        homeRequest.setCategory_ids("");
        homeRequest.setSearch_item("");
        WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(homeRequest));
        Call<String> call = service.home(Constants.BEARER + token, requestBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                mView.onHideProgress();
                try {
                    if (response.code() == Constants.SUCCESS) {
                        if (response.body() != null) {
                            Type listType = new TypeToken<HomeResponse>() {
                            }.getType();
                            HomeResponse responseModel = new GsonBuilder().create().fromJson(response.body(), listType);
                            if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.SUCCESS))) {
                                mView.onSuccees(responseModel);
                            } else if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.ERROR_401))) {
                                mView.logoutAction(responseModel.getMetadata().getMessage());
                            } else if (responseModel.getMetadata().getStatus_code().equals(Constants.NO_DATA)) {
                                mView.noDataFound();
                                //mView.onShowAlertDialog("No Data Found");
                            } else
                                mView.onServerError(responseModel.getMetadata().getStatus_code());

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
                else
                    mView.onServerError(t.getMessage());
            }
        });
    }

    @Override
    public void getmycourses(Context context) {
        mView.onShowProgress();
        OBDBHelper obdbHelper = new OBDBHelper(context);
        String token = obdbHelper.getAccessToken();
        /*Create handle for the RetrofitInstance interface*/
        WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);
        Call<String> call = service.mycourses(Constants.BEARER + token);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //mView.onShowProgress();
                try {
                    if (response.code() == Constants.SUCCESS) {
                        if (response.body() != null) {
                            Type listType = new TypeToken<MyCoursesResponseModel>() {
                            }.getType();
                            MyCoursesResponseModel responseModel = new GsonBuilder().create().fromJson(response.body(), listType);
                            if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.SUCCESS))) {
                                mView.onSuccees(responseModel);
                            } else if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.ERROR_401))) {
                                mView.logoutAction(responseModel.getMetadata().getMessage());
                            }
                        } else {
                            mView.onFailure(context.getString(R.string.something_went_wrong_text));
                        }
                    } else if (response.code() == Constants.ERROR_404) {
                        if (response.errorBody() != null) {
                            Type listType = new TypeToken<NoDataFound>() {
                            }.getType();
                            NoDataFound responseModel = null;
                            try {
                                responseModel = new GsonBuilder().create().fromJson(response.errorBody().string(), listType);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            mView.onSuccees(responseModel);
                        } else {
                            mView.onFailure(context.getString(R.string.something_went_wrong_text));
                        }
                    }
                    mView.onHideProgress();
                } catch (JsonSyntaxException e) {
                    mView.onHideProgress();
                    mView.onFailure(false);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mView.onHideProgress();
                mView.onShowAlertDialog(t.getMessage());
            }
        });
    }

    @Override
    public void clearLocalDB(Context context) {
        OBDBHelper dbHelper = new OBDBHelper(context);
        dbHelper.deleteUserData();
        mView.onSuccees(Constants.DB_CLEAR);
    }


    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
