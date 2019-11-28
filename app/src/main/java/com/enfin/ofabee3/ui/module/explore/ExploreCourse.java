package com.enfin.ofabee3.ui.module.explore;

import com.enfin.ofabee3.data.remote.model.explorecourse.response.ExploreCoursesResponse;
import com.enfin.ofabee3.data.remote.model.home.response.HomeResponse;
import com.enfin.ofabee3.data.remote.model.seeallcourses.response.SeeAllResponse;

public class ExploreCourse {

    private ExploreCoursesResponse.DataBean.AllCourseBean exploreCourse;

    public ExploreCoursesResponse.DataBean.AllCourseBean getExploreCourse() {
        return exploreCourse;
    }

    public String getCourseThumbnailImage() {
        return exploreCourse.getItem_image();
    }

    public void setExploreCourse(ExploreCoursesResponse.DataBean.AllCourseBean exploreCourse) {
        this.exploreCourse = exploreCourse;
    }

    public String getCourseName() {
        return exploreCourse.getItem_name();
    }


    public String getOldItemPrice() {
        if (exploreCourse != null)
            return exploreCourse.getItem_price();
        else
            return null;
    }

    public String getCourseDiscount() {
        if (exploreCourse != null)
            return exploreCourse.getItem_discount();
        else
            return null;
    }

    public String getCourseItemRating() {
        if (exploreCourse != null)
            return exploreCourse.getItem_rating();
        else
            return null;
    }

    public String getItemIsFree() {
        if (exploreCourse != null)
            return exploreCourse.getItem_is_free();
        else
            return null;
    }

    public String getCourseItemType() {
        if (exploreCourse != null)
            return exploreCourse.getItem_type();
        else
            return null;
    }

    public String getCourseCategory() {
        if (exploreCourse != null) {
            return categoryTagResize();
        } else
            return null;
    }

    public String getCourseHasRating() {
        if (exploreCourse != null)
            return exploreCourse.getItem_has_rating();
        else
            return null;
    }

    private String categoryTagResize() {
        String categoryTag = "";
        if (exploreCourse.getItem_category().size() > 0) {
            if (exploreCourse.getItem_category().size() > 1) {
                if (exploreCourse.getItem_category().get(0).getCt_name().length() > 10)
                    categoryTag = exploreCourse.getItem_category().get(0).getCt_name().substring(0, 10) + ".." + " + " + (exploreCourse.getItem_category().size() - 1);
                else if (exploreCourse.getItem_category().get(0).getCt_name().length() <= 10)
                    categoryTag = exploreCourse.getItem_category().get(0).getCt_name() + " + " + (exploreCourse.getItem_category().size() - 1);
            } else
                categoryTag = exploreCourse.getItem_category().get(0).getCt_name();
        }
        return categoryTag;
    }

    public boolean isEnroll() {
        if (exploreCourse != null) {
            return exploreCourse.isEnrolled();
        } else
            return false;
    }

    public String getID() {
        if (exploreCourse != null)
            return exploreCourse.getItem_id();
        else
            return null;
    }

}
