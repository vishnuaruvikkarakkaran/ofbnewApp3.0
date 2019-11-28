package com.enfin.ofabee3.ui.module.featuredcourses;

import android.content.Context;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.local.database.OBDBHelper;
import com.enfin.ofabee3.data.remote.ApiClient;
import com.enfin.ofabee3.data.remote.WebApiListener;
import com.enfin.ofabee3.data.remote.model.seeallcourses.request.SeeAllCoursesRequest;
import com.enfin.ofabee3.data.remote.model.seeallcourses.response.SeeAllResponse;
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

public class FeaturedCoursesListPresenter extends BasePresenter implements FeaturedCoursesListContract.Presenter {

    private FeaturedCoursesListContract.View mView;
    private Context context;

    public FeaturedCoursesListPresenter(Context context, FeaturedCoursesListContract.View mView) {
        this.mView = mView;
        this.context = context;
        this.mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void getfeaturedcourses(Context context, String categoryIDs, int offset) {
        mView.onShowProgress();
        OBDBHelper obdbHelper = new OBDBHelper(context);
        String token = obdbHelper.getAccessToken();
        WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);
        SeeAllCoursesRequest request = new SeeAllCoursesRequest();
        request.setCategory_ids(categoryIDs);
        request.setIdentifier("2");
        request.setSearch_keyword("");
        request.setOffset(String.valueOf(offset));
        request.setLimit("8");
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(request));
        Call<String> call;
        if (token != null)
            call = service.seeallcourses(Constants.BEARER + token, requestBody);
        else
            call = service.seeallcourses(requestBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                mView.onHideProgress();
                try {
                    if (response.code() == Constants.SUCCESS) {
                        if (response.body() != null) {
                            Type listType = new TypeToken<SeeAllResponse>() {
                            }.getType();
                            SeeAllResponse responseModel = new GsonBuilder().create().fromJson(response.body(), listType);

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
}
