package com.enfin.ofabee3.ui.module.mycourses;

import android.content.Context;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.local.database.OBDBHelper;
import com.enfin.ofabee3.data.remote.model.home.response.NoCoursesResponse;
import com.enfin.ofabee3.data.remote.model.mycourses.response.MyCoursesResponseModel;
import com.enfin.ofabee3.data.remote.model.mycourses.response.NoDataFound;
import com.enfin.ofabee3.ui.base.mvp.BasePresenter;
import com.enfin.ofabee3.data.remote.model.Request.RequestModel;
import com.enfin.ofabee3.data.remote.ApiClient;
import com.enfin.ofabee3.utils.Constants;
import com.enfin.ofabee3.data.remote.WebApiListener;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.UnknownHostException;

import javax.annotation.Nonnull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCoursesPresenter extends BasePresenter implements MyCoursesContract.Presenter {

    private MyCoursesContract.View mView;

    private String token;
    private Context mContext;

    public MyCoursesPresenter(@Nonnull Context context, @Nonnull MyCoursesContract.View view) {

        this.mView = view;
        this.mContext = context;
        this.mView.setPresenter(this);
        //token = getItemFromDatabase(KEY_ACCESS_TOKEN);
    }

    @Override
    public void getcourses(Context context) {
        mView.onShowProgress();
        OBDBHelper obdbHelper = new OBDBHelper(context);
        token = obdbHelper.getAccessToken();
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
                            if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.SUCCESS)))
                                mView.onSuccees(responseModel);
                            else if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.ERROR_401))) {
                                mView.logoutAction(responseModel.getMetadata().getMessage());
                            }

                        } else {
                            mView.onFailure(context.getString(R.string.something_went_wrong_text));
                        }
                    } else if (response.code() == Constants.ERROR_404) {
                        if (response.errorBody() != null) {
                            Type listType = new TypeToken<NoDataFound>() {
                            }.getType();
                            NoCoursesResponse responseModel = new GsonBuilder().create().fromJson(response.body(), listType);
                            mView.onSuccees(responseModel);
                            /*try {
                                responseModel = new GsonBuilder().create().fromJson(response.errorBody().string(), listType);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }*/
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
                if (t instanceof UnknownHostException)
                    mView.onShowAlertDialog("No Internet");
                else
                    mView.onServerError(t.getMessage());
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
