package com.enfin.ofabee3.ui.module.coursedetail;

import android.widget.ImageView;

import com.enfin.ofabee3.data.remote.model.home.response.HomeResponse;
import com.enfin.ofabee3.data.remote.model.seeallcourses.response.SeeAllResponse;

public class CourseLecture {

    private String lectureName, lectureThumbnail;
    private String previewEnabled="0", previewURL;
    private String lectureImageEnabled = "0";
    public String getLectureName() {
        return lectureName;
    }

    public void setLectureName(String lectureName) {
        this.lectureName = lectureName;
    }

    public String getLectureThumbnail() {
        return lectureThumbnail;
    }

    public void setLectureThumbnail(String lectureThumbnail) {
        this.lectureThumbnail = lectureThumbnail;
    }

    public String isLectureImageEnabled() {
        return lectureImageEnabled;
    }

    public void setLectureImageEnabled(String lectureImageEnabled) {
        this.lectureImageEnabled = lectureImageEnabled;
    }

    public String getPreviewEnabled() {
        return previewEnabled;
    }

    public void setPreviewEnabled(String previewEnabled) {
        this.previewEnabled = previewEnabled;
    }

    public String getPreviewURL() {
        return previewURL;
    }

    public void setPreviewURL(String previewURL) {
        this.previewURL = previewURL;
    }
}
