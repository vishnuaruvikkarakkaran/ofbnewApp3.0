package com.enfin.ofabee3.ui.module.coursedetail.coursereview;

import android.content.Context;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.local.database.OBDBHelper;
import com.enfin.ofabee3.data.remote.ApiClient;
import com.enfin.ofabee3.data.remote.WebApiListener;
import com.enfin.ofabee3.data.remote.model.coursecategory.response.CourseCategoryResponse;
import com.enfin.ofabee3.data.remote.model.explorecourse.request.ExploreCoursesRequest;
import com.enfin.ofabee3.data.remote.model.explorecourse.response.ExploreCoursesResponse;
import com.enfin.ofabee3.ui.base.mvp.BasePresenter;
import com.enfin.ofabee3.ui.module.explore.ExploreContract;
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

public class CourseReviewPresenter extends BasePresenter implements CourseReviewContract.Presenter {

    private CourseReviewContract.View mView;
    private Context mContext;
    private String token;

    public CourseReviewPresenter(CourseReviewContract.View mView, Context mContext) {
        this.mView = mView;
        this.mContext = mContext;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {
    }
}
