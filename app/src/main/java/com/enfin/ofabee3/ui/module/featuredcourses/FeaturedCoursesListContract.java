package com.enfin.ofabee3.ui.module.featuredcourses;

import android.content.Context;

import com.enfin.ofabee3.ui.base.mvp.BaseContract;
import com.yanzhenjie.album.mvp.BasePresenter;

public class FeaturedCoursesListContract {

    public interface View extends BaseContract.View<Presenter> {
        <T> void onSuccees(T type);

        <T> void onFailure(T type);
    }

    public interface Presenter extends BaseContract.Presenter {
        void getfeaturedcourses(Context context, String categoryIDs, int offset);
    }

}
