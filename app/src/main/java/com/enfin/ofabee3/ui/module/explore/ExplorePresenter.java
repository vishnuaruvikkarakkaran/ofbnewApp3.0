package com.enfin.ofabee3.ui.module.explore;

import android.content.Context;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.local.database.OBDBHelper;
import com.enfin.ofabee3.data.remote.ApiClient;
import com.enfin.ofabee3.data.remote.WebApiListener;
import com.enfin.ofabee3.data.remote.model.coursecategory.response.CourseCategoryResponse;
import com.enfin.ofabee3.data.remote.model.explorecourse.request.ExploreCoursesRequest;
import com.enfin.ofabee3.data.remote.model.explorecourse.response.ExploreCoursesResponse;
import com.enfin.ofabee3.data.remote.model.seeallcourses.request.SeeAllCoursesRequest;
import com.enfin.ofabee3.data.remote.model.seeallcourses.response.SeeAllResponse;
import com.enfin.ofabee3.ui.base.mvp.BasePresenter;
import com.enfin.ofabee3.ui.module.userprofile.UserProfileContract;
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

public class ExplorePresenter extends BasePresenter implements ExploreContract.Presenter {

    private ExploreContract.View mView;
    private Context mContext;
    private String token;

    public ExplorePresenter(ExploreContract.View mView, Context mContext) {
        this.mView = mView;
        this.mContext = mContext;
    }

    @Override
    public void getCourses(Context context, String categoryIDs, int offset) {
        mView.onShowProgress();
        OBDBHelper obdbHelper = new OBDBHelper(context);
        token = obdbHelper.getAccessToken();
        WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);
        ExploreCoursesRequest request = new ExploreCoursesRequest();
        request.setCategory_ids(categoryIDs);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(request));
        Call<String> call = service.explorecourses(Constants.BEARER + token, requestBody);
        //Call<String> call = service.explorecourses(requestBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                OBLogger.e("RESPONSE CODE "+ response.code());
                try {
                    OBLogger.e("RESPONSE CODE "+ response.code());
                    if (response.code() == Constants.SUCCESS) {
                        if (response.body() != null) {
                            Type listType = new TypeToken<ExploreCoursesResponse>() {
                            }.getType();
                            ExploreCoursesResponse responseModel = new GsonBuilder().create().fromJson(response.body(), listType);
                            if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.SUCCESS))) {
                                mView.onSuccess(responseModel);
                            } else if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.ERROR_401))) {
                                mView.logoutAction(responseModel.getMetadata().getMessage());
                            } else
                                mView.onShowAlertDialog(responseModel.getMetadata().getMessage());

                        } else
                            mView.onShowAlertDialog(context.getString(R.string.something_went_wrong_text));

                    } else
                        mView.onShowAlertDialog(context.getString(R.string.something_went_wrong_text));
                    mView.onHideProgress();
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mView.onHideProgress();
                OBLogger.e("RESPONSE CODE "+ t.getLocalizedMessage());
                OBLogger.e("RESPONSE CODE "+ t.getMessage());
                OBLogger.e("RESPONSE CODE "+ t.getCause());
                if (t instanceof UnknownHostException)
                    mView.onShowAlertDialog("No Internet");
                else
                    mView.onServerError(t.getMessage());
            }
        });
    }

    @Override
    public void getCoursesCategory(Context context) {
        if (NetworkUtil.isConnected(context)) {
            //mView.onShowProgress();
            OBDBHelper obdbHelper = new OBDBHelper(context);
            if (obdbHelper.getAccessToken() != null)
                token = obdbHelper.getAccessToken();
            else
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
                                if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.SUCCESS)))
                                    mView.onSuccess(responseModel);
                                else if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.ERROR_401))) {
                                    mView.logoutAction(responseModel.getMetadata().getMessage());
                                }
                            } else {
                                mView.onFailure(context.getString(R.string.something_went_wrong_text));
                            }
                        } else if (response.code() == Constants.ERROR_404) {
                        }
                        //mView.onHideProgress();
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
        } else {
            mView.onShowAlertDialog("No Internet");
        }
    }

    @Override
    public void getCoursesCategoryWithoutHeader(Context context) {
        if (NetworkUtil.isConnected(context)) {
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

                                if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.SUCCESS)))
                                    mView.onSuccess(responseModel);
                                else if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.ERROR_401))) {
                                    mView.logoutAction(responseModel.getMetadata().getMessage());
                                }

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
        } else {
            mView.onShowAlertDialog("No Internet");
        }
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }
}
