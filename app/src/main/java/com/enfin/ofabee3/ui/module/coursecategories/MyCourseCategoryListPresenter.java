package com.enfin.ofabee3.ui.module.coursecategories;

import android.content.Context;

import androidx.annotation.NonNull;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.local.database.OBDBHelper;
import com.enfin.ofabee3.data.remote.ApiClient;
import com.enfin.ofabee3.data.remote.WebApiListener;
import com.enfin.ofabee3.data.remote.model.Response.LoginResponseModel;
import com.enfin.ofabee3.data.remote.model.coursecategory.request.CourseCategoryRequest;
import com.enfin.ofabee3.data.remote.model.coursecategory.response.CourseCategoryResponse;
import com.enfin.ofabee3.data.remote.model.savecategory.request.SaveCategoryRequest;
import com.enfin.ofabee3.data.remote.model.savecategory.response.SaveCategoryResponse;
import com.enfin.ofabee3.ui.base.mvp.BasePresenter;
import com.enfin.ofabee3.utils.Constants;
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

public class MyCourseCategoryListPresenter extends BasePresenter implements MyCourseCategoryListContract.Presenter {

    private MyCourseCategoryListContract.View mView;
    private String token;
    private Context mContext;

    public MyCourseCategoryListPresenter(@NonNull Context context, @NonNull MyCourseCategoryListContract.View view) {
        this.mView = view;
        this.mContext = context;
        this.mView.setPresenter(this);
    }

    @Override
    public void getCoursesCategory(Context context) {
        mView.onShowProgress();
        OBDBHelper obdbHelper = new OBDBHelper(context);
        token = obdbHelper.getAccessToken();
        if (token == null)
            token = "";


        /*Create handle for the RetrofitInstance interface*/
        WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);
        Call<String> call = service.courseCategory(Constants.BEARER + token);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.code() == Constants.SUCCESS) {
                        if (response.body() != null) {
                            Type listType = new TypeToken<CourseCategoryResponse>() {
                            }.getType();
                            CourseCategoryResponse responseModel = new GsonBuilder().create().fromJson(response.body(), listType);
                            mView.onSuccees(responseModel);
                        } else {
                            mView.onFailure(context.getString(R.string.something_went_wrong_text));
                        }
                    } else if (response.code() == Constants.ERROR_404) {
                    }
                    mView.onHideProgress();
                } catch (JsonSyntaxException e) {
                    mView.onHideProgress();
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
    public void getCoursesCategoryWithoutHeader(Context context) {
        mView.onShowProgress();

        /*Create handle for the RetrofitInstance interface*/
        WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);
        Call<String> call = service.courseCategory();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.code() == Constants.SUCCESS) {
                        if (response.body() != null) {
                            Type listType = new TypeToken<CourseCategoryResponse>() {
                            }.getType();
                            CourseCategoryResponse responseModel = new GsonBuilder().create().fromJson(response.body(), listType);
                            mView.onSuccees(responseModel);
                        } else {
                            mView.onFailure(context.getString(R.string.something_went_wrong_text));
                        }
                    } else if (response.code() == Constants.ERROR_404) {
                    }
                    mView.onHideProgress();
                } catch (JsonSyntaxException e) {
                    mView.onHideProgress();
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
    }

    @Override
    public void saveCoursesCategory(Context context, String categoryIDs) {
        mView.onShowProgress();
        OBDBHelper obdbHelper = new OBDBHelper(context);
        token = obdbHelper.getAccessToken();
        WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);
        SaveCategoryRequest saveCategoryRequest = new SaveCategoryRequest();
        saveCategoryRequest.setCategory_id(categoryIDs);
        OBLogger.e("STRING " + categoryIDs);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(saveCategoryRequest));
        Call<String> call = service.savecourseCategory(Constants.BEARER + token, requestBody);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                mView.onHideProgress();
                try {
                    if (response.code() == Constants.SUCCESS) {
                        if (response.body() != null) {

                            Type listType = new TypeToken<SaveCategoryResponse>() {
                            }.getType();
                            SaveCategoryResponse responseModel = new GsonBuilder().create().fromJson(response.body(), listType);

                            if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.SUCCESS))) {
                                mView.onSuccees(responseModel);
                            } else if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.ERROR_401))) {
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
                else
                    mView.onShowAlertDialog(t.getMessage());
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
