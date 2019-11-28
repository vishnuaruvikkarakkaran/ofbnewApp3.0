package com.enfin.ofabee3.ui.module.home.popularcourses;

import com.enfin.ofabee3.data.remote.model.home.response.HomeResponse;
import com.enfin.ofabee3.data.remote.model.seeallcourses.response.SeeAllResponse;

public class PopularCourse {

    public PopularCourse(HomeResponse.DataBean.CoursesBean.ListBeanX popularCourse) {
        this.popularCourse = popularCourse;
    }

    public PopularCourse(SeeAllResponse.DataBean.ListBean morePopularCourse) {
        this.morePopularCourse = morePopularCourse;
    }

    private HomeResponse.DataBean.CoursesBean.ListBeanX popularCourse;

    private SeeAllResponse.DataBean.ListBean morePopularCourse;

    public String getCourseName() {
        if (popularCourse != null)
            return popularCourse.getItem_name();
        else if (morePopularCourse != null)
            return morePopularCourse.getItem_name();
        else
            return null;
    }

    public String getCourseThumbnailImage() {
        if (popularCourse != null)
            return popularCourse.getItem_image();
        else if (morePopularCourse != null)
            return morePopularCourse.getItem_image();
        else
            return null;
    }

    public String getOldItemPrice() {
        if (popularCourse != null)
            return popularCourse.getItem_price();
        else if (morePopularCourse != null)
            return morePopularCourse.getItem_price();
        else
            return null;
    }

    public String getCourseDiscount() {
        if (popularCourse != null)
            return popularCourse.getItem_discount();
        else if (morePopularCourse != null)
            return morePopularCourse.getItem_discount();
        else
            return null;
    }

    public String getCourseItemRating() {
        if (popularCourse != null)
            return popularCourse.getItem_rating();
        else if (morePopularCourse != null)
            return morePopularCourse.getItem_rating();
        else
            return null;
    }

    public String getItemIsFree() {
        if (popularCourse != null)
            return popularCourse.getItem_is_free();
        else if (morePopularCourse != null)
            return morePopularCourse.getItem_is_free();
        else
            return null;
    }

    public String getCourseItemType() {
        if (popularCourse != null)
            return popularCourse.getItem_type();
        else if (morePopularCourse != null)
            return morePopularCourse.getItem_type();
        else
            return null;
    }

    public String getCourseHasRating() {
        if (popularCourse != null)
            return popularCourse.getItem_has_rating();
        else if (morePopularCourse != null)
            return morePopularCourse.getItem_has_rating();
        else
            return null;
    }

    public String getCourseCategory() {
        if (popularCourse != null) {
            return categoryTagResize();
        } else if (morePopularCourse != null) {
            return morecategoryTagResize();
        } else
            return null;
    }

    public boolean isEnrolled() {
        if (popularCourse != null) {
            return popularCourse.isEnrolled();
        } else if (morePopularCourse != null) {
            return morePopularCourse.isEnrolled();
        } else
            return false;
    }

    public String getID() {
        if (popularCourse != null)
            return popularCourse.getItem_id();
        else if (morePopularCourse != null)
            return morePopularCourse.getItem_id();
        else
            return null;
    }

    private String categoryTagResize() {
        String categoryTag = "";
        if (popularCourse.getItem_category().size() > 0) {
            if (popularCourse.getItem_category().size() > 1) {
                if (popularCourse.getItem_category().get(0).getCt_name().length() > 10)
                    categoryTag = popularCourse.getItem_category().get(0).getCt_name().substring(0, 10) + ".." + " + " + (popularCourse.getItem_category().size() - 1);
                else if (popularCourse.getItem_category().get(0).getCt_name().length() <= 10)
                    categoryTag = popularCourse.getItem_category().get(0).getCt_name() + " + " + (popularCourse.getItem_category().size() - 1);
            } else
                categoryTag = popularCourse.getItem_category().get(0).getCt_name();
        }
        return categoryTag;
    }

    private String morecategoryTagResize() {
        String categoryTag="";
        if (morePopularCourse.getItem_category().size() > 1) {
            if (morePopularCourse.getItem_category().get(0).getCt_name().length() > 10)
                categoryTag = morePopularCourse.getItem_category().get(0).getCt_name().substring(0, 10) + ".." + " + " + (morePopularCourse.getItem_category().size() - 1);
            else if (morePopularCourse.getItem_category().get(0).getCt_name().length() <= 10)
                categoryTag = morePopularCourse.getItem_category().get(0).getCt_name() + " + " + (morePopularCourse.getItem_category().size() - 1);
        } else
            categoryTag = morePopularCourse.getItem_category().get(0).getCt_name();

        return categoryTag;
    }

    public HomeResponse.DataBean.CoursesBean.ListBeanX getPopularCourse() {
        return popularCourse;
    }

    public void setPopularCourse(HomeResponse.DataBean.CoursesBean.ListBeanX popularCourse) {
        this.popularCourse = popularCourse;
    }

    public void setMorePopularCourse(SeeAllResponse.DataBean.ListBean popularCourse) {
        this.morePopularCourse = popularCourse;
    }
}
