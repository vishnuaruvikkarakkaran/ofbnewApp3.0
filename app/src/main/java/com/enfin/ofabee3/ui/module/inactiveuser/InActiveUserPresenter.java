package com.enfin.ofabee3.ui.module.inactiveuser;

import android.content.Context;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.local.database.OBDBHelper;
import com.enfin.ofabee3.data.remote.ApiClient;
import com.enfin.ofabee3.data.remote.WebApiListener;
import com.enfin.ofabee3.data.remote.model.home.request.HomeRequest;
import com.enfin.ofabee3.data.remote.model.home.response.HomeResponse;
import com.enfin.ofabee3.data.remote.model.mycourses.response.MyCoursesResponseModel;
import com.enfin.ofabee3.data.remote.model.mycourses.response.NoDataFound;
import com.enfin.ofabee3.ui.base.mvp.BasePresenter;
import com.enfin.ofabee3.utils.Constants;
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

public class InActiveUserPresenter extends BasePresenter implements InActiveUserContract.Presenter {

    private InActiveUserContract.View mView;
    private Context mContext;

    public InActiveUserPresenter(@Nonnull Context context, @Nonnull InActiveUserContract.View view) {
        this.mView = view;
        this.mContext = context;
        this.mView.setPresenter(this);
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
