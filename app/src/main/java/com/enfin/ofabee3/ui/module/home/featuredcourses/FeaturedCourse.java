package com.enfin.ofabee3.ui.module.home.featuredcourses;

import com.enfin.ofabee3.data.remote.model.home.response.HomeResponse;

public class FeaturedCourse {

    private HomeResponse.DataBean.CoursesBean.ListBeanX featuredCourse;

    public HomeResponse.DataBean.CoursesBean.ListBeanX getFeaturedCourse() {
        return featuredCourse;
    }

    public String getCourseThumbnailImage() {
        return featuredCourse.getItem_image();
    }

    public void setFeaturedCourse(HomeResponse.DataBean.CoursesBean.ListBeanX featuredCourse) {
        this.featuredCourse = featuredCourse;
    }

    public String getCourseName() {
        return featuredCourse.getItem_name();
    }


    public boolean isEnrolled() {
        if (featuredCourse != null) {
            return featuredCourse.isEnrolled();
        } else
            return false;
    }

    public String getID() {
        if (featuredCourse != null)
            return featuredCourse.getItem_id();
        else
            return null;
    }

    public String getCourseItemType() {
        if (featuredCourse != null)
            return featuredCourse.getItem_type();
        else
            return null;
    }

    public String getCourseHasRating() {
        if (featuredCourse != null)
            return featuredCourse.getItem_has_rating();
        else
            return null;
    }

}
