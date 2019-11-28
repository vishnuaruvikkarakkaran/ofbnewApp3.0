package com.enfin.ofabee3.ui.module.coursedetail.bundledetails;

import android.content.Context;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.local.database.OBDBHelper;
import com.enfin.ofabee3.data.remote.ApiClient;
import com.enfin.ofabee3.data.remote.WebApiListener;
import com.enfin.ofabee3.data.remote.model.coursedetail.request.CourseDetailRequestModel;
import com.enfin.ofabee3.data.remote.model.coursedetail.response.BundleDetailResponseModel;
import com.enfin.ofabee3.data.remote.model.coursedetail.response.CourseDetailResponseModel;
import com.enfin.ofabee3.ui.base.mvp.BasePresenter;
import com.enfin.ofabee3.utils.Constants;
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

public class BundleDetailPresenter extends BasePresenter implements BundleDetailContract.Presenter {

    private BundleDetailContract.View mView;
    private Context mContext;
    private String token;

    public BundleDetailPresenter(BundleDetailContract.View mView, Context mContext) {
        this.mView = mView;
        this.mContext = mContext;
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @Override
    public void getBundleDetail(Context context,String courseID, String type) {
        mView.onShowProgress();
        OBDBHelper obdbHelper = new OBDBHelper(context);
        token = obdbHelper.getAccessToken();
        WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);
        CourseDetailRequestModel request = new CourseDetailRequestModel();
        request.setItem_id(courseID);
        request.setItem_type(type);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(request));
        Call<String> call = service.coursedetail(Constants.BEARER + token, requestBody);
        maptoModel(context,type,call);
    }

    private void maptoModel(Context context, String type, Call<String> call) {
        if (type.equals("course")){
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    //mView.onHideProgress();
                    try {
                        if (response.code() == Constants.SUCCESS) {
                            if (response.body() != null) {
                                Type listType = new TypeToken<CourseDetailResponseModel>() {
                                }.getType();
                                CourseDetailResponseModel responseModel = new GsonBuilder().create().fromJson(response.body(), listType);
                                if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.SUCCESS))) {
                                    mView.onSuccess(responseModel);
                                } else if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.ERROR_401))) {
                                    mView.onShowAlertDialog(responseModel.getMetadata().getMessage());
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
        else if (type.equals("bundle")){
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    //mView.onHideProgress();
                    try {
                        if (response.code() == Constants.SUCCESS) {
                            if (response.body() != null) {
                                Type listType = new TypeToken<BundleDetailResponseModel>() {
                                }.getType();
                                BundleDetailResponseModel responseModel = new GsonBuilder().create().fromJson(response.body(), listType);
                                if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.SUCCESS))) {
                                    mView.onSuccess(responseModel);
                                } else if (responseModel.getMetadata().getStatus_code().equals(String.valueOf(Constants.ERROR_401))) {
                                    mView.onShowAlertDialog(responseModel.getMetadata().getMessage());
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
                        mView.onShowAlertDialog(t.getMessage());
                }
            });
        }
    }
}
