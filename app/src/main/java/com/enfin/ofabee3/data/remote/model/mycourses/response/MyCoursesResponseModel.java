package com.enfin.ofabee3.data.remote.model.mycourses.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by SARATH on 20/8/19.
 */


public class MyCoursesResponseModel implements Serializable {
    /**
     * metadata : {"error":false,"message":"Subscribed Courses fetched successfully.","status_code":"200"}
     * data : [{"course_id":"119","subscribe_id":"18065","cb_title":"ASSESSMENTS (KAS)","cb_image":"https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/119/course/119.jpg?v=169","cs_percentage":100,"cs_approved":"1","cs_course_validity_status":"0","cs_end_date":"2028-01-25","item_type":"course","course_completion":100,"expired":false,"expire_in":"+3000","expire_in_days":3001,"validity_format_date":"25-01-2028"},{"course_id":"131","subscribe_id":"18064","cb_title":"Appium","cb_image":"https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/131/course/131.jpg?v=619","cs_percentage":10,"cs_approved":"1","cs_course_validity_status":"0","cs_end_date":"2028-01-25","item_type":"course","course_completion":10,"expired":false,"expire_in":"+3000","expire_in_days":3001,"validity_format_date":"25-01-2028"},{"bundle_id":"26","id":"26","bs_end_date":"2028-01-25","c_title":"Test Bundle","c_slug":"test-bundle","bs_bundle_id":"26","bs_approved":"1","bs_course_validity_status":"0","c_category":"4","c_image":"26.jpg?v=423","item_type":"bundle","bs_bundle_details":"{\"id\":\"26\",\"c_title\":\"Test Bundle\",\"c_code\":\"TB\",\"c_courses\":[{\"id\":\"131\",\"course_name\":\"Appium\",\"course_code\":\"ENF 3\",\"status\":\"1\"},{\"id\":\"119\",\"course_name\":\"ASSESSMENTS (KAS)\",\"course_code\":\"KASAM\",\"status\":\"0\"}],\"c_access_validity\":\"0\",\"c_validity\":\"0\",\"c_validity_date\":\"0000-00-00\",\"c_price\":\"0\",\"c_discount\":\"0\",\"c_tax_method\":\"0\",\"bundle_id\":\"26\"}","expired":false,"expire_in":"+3000","expire_in_days":3001,"validity_format_date":"25-01-2028"},{"bundle_id":"26","id":"26","bs_end_date":"2028-01-25","c_title":"Test Bundle","c_slug":"test-bundle","bs_bundle_id":"26","bs_approved":"1","bs_course_validity_status":"0","c_category":"4","c_image":"26.jpg?v=423","item_type":"bundle","bs_bundle_details":"{\"id\":\"26\",\"c_title\":\"Test Bundle\",\"c_code\":\"TB\",\"c_courses\":[{\"id\":\"131\",\"course_name\":\"Appium\",\"course_code\":\"ENF 3\",\"status\":\"1\"},{\"id\":\"119\",\"course_name\":\"ASSESSMENTS (KAS)\",\"course_code\":\"KASAM\",\"status\":\"0\"}],\"c_access_validity\":\"0\",\"c_validity\":\"0\",\"c_validity_date\":\"0000-00-00\",\"c_price\":\"0\",\"c_discount\":\"0\",\"c_tax_method\":\"0\",\"bundle_id\":\"26\"}","expired":false,"expire_in":"+3000","expire_in_days":3001,"validity_format_date":"25-01-2028"}]
     */

    private MetadataBean metadata;
    private List<DataBean> data;

    public MetadataBean getMetadata() {
        return metadata;
    }

    public void setMetadata(MetadataBean metadata) {
        this.metadata = metadata;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class MetadataBean implements Serializable{
        /**
         * error : false
         * message : Subscribed Courses fetched successfully.
         * status_code : 200
         */

        private boolean error;
        private String message;
        private String status_code;

        public boolean isError() {
            return error;
        }

        public void setError(boolean error) {
            this.error = error;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getStatus_code() {
            return status_code;
        }

        public void setStatus_code(String status_code) {
            this.status_code = status_code;
        }
    }

    public static class DataBean implements Serializable{
        /**
         * course_id : 119
         * subscribe_id : 18065
         * cb_title : ASSESSMENTS (KAS)
         * cb_image : https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/119/course/119.jpg?v=169
         * cs_percentage : 100
         * cs_approved : 1
         * cs_course_validity_status : 0
         * cs_end_date : 2028-01-25
         * item_type : course
         * course_completion : 100
         * expired : false
         * expire_in : +3000
         * expire_in_days : 3001
         * validity_format_date : 25-01-2028
         * bundle_id : 26
         * id : 26
         * bs_end_date : 2028-01-25
         * c_title : Test Bundle
         * c_slug : test-bundle
         * bs_bundle_id : 26
         * bs_approved : 1
         * bs_course_validity_status : 0
         * c_category : 4
         * c_image : 26.jpg?v=423
         * bs_bundle_details : {"id":"26","c_title":"Test Bundle","c_code":"TB","c_courses":[{"id":"131","course_name":"Appium","course_code":"ENF 3","status":"1"},{"id":"119","course_name":"ASSESSMENTS (KAS)","course_code":"KASAM","status":"0"}],"c_access_validity":"0","c_validity":"0","c_validity_date":"0000-00-00","c_price":"0","c_discount":"0","c_tax_method":"0","bundle_id":"26"}
         */

        private String course_id;
        private String subscribe_id;
        private String cb_title;
        private String cb_image;
        private int cs_percentage;
        private String cs_approved;
        private String cs_course_validity_status;
        private String cs_end_date;
        private String item_type;
        private int course_completion;
        private boolean expired;
        private String expire_in;
        private int expire_in_days;
        private String validity_format_date;
        private String bundle_id;
        private String id;
        private String bs_end_date;
        private String c_title;
        private String c_slug;
        private String bs_bundle_id;
        private String bs_approved;
        private String bs_course_validity_status;
        private String c_category;
        private String c_image;
        private String bs_bundle_details;

        public String getCourse_id() {
            return course_id;
        }

        public void setCourse_id(String course_id) {
            this.course_id = course_id;
        }

        public String getSubscribe_id() {
            return subscribe_id;
        }

        public void setSubscribe_id(String subscribe_id) {
            this.subscribe_id = subscribe_id;
        }

        public String getCb_title() {
            return cb_title;
        }

        public void setCb_title(String cb_title) {
            this.cb_title = cb_title;
        }

        public String getCb_image() {
            return cb_image;
        }

        public void setCb_image(String cb_image) {
            this.cb_image = cb_image;
        }

        public int getCs_percentage() {
            return cs_percentage;
        }

        public void setCs_percentage(int cs_percentage) {
            this.cs_percentage = cs_percentage;
        }

        public String getCs_approved() {
            return cs_approved;
        }

        public void setCs_approved(String cs_approved) {
            this.cs_approved = cs_approved;
        }

        public String getCs_course_validity_status() {
            return cs_course_validity_status;
        }

        public void setCs_course_validity_status(String cs_course_validity_status) {
            this.cs_course_validity_status = cs_course_validity_status;
        }

        public String getCs_end_date() {
            return cs_end_date;
        }

        public void setCs_end_date(String cs_end_date) {
            this.cs_end_date = cs_end_date;
        }

        public String getItem_type() {
            return item_type;
        }

        public void setItem_type(String item_type) {
            this.item_type = item_type;
        }

        public int getCourse_completion() {
            return course_completion;
        }

        public void setCourse_completion(int course_completion) {
            this.course_completion = course_completion;
        }

        public boolean isExpired() {
            return expired;
        }

        public void setExpired(boolean expired) {
            this.expired = expired;
        }

        public String getExpire_in() {
            return expire_in;
        }

        public void setExpire_in(String expire_in) {
            this.expire_in = expire_in;
        }

        public int getExpire_in_days() {
            return expire_in_days;
        }

        public void setExpire_in_days(int expire_in_days) {
            this.expire_in_days = expire_in_days;
        }

        public String getValidity_format_date() {
            return validity_format_date;
        }

        public void setValidity_format_date(String validity_format_date) {
            this.validity_format_date = validity_format_date;
        }

        public String getBundle_id() {
            return bundle_id;
        }

        public void setBundle_id(String bundle_id) {
            this.bundle_id = bundle_id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBs_end_date() {
            return bs_end_date;
        }

        public void setBs_end_date(String bs_end_date) {
            this.bs_end_date = bs_end_date;
        }

        public String getC_title() {
            return c_title;
        }

        public void setC_title(String c_title) {
            this.c_title = c_title;
        }

        public String getC_slug() {
            return c_slug;
        }

        public void setC_slug(String c_slug) {
            this.c_slug = c_slug;
        }

        public String getBs_bundle_id() {
            return bs_bundle_id;
        }

        public void setBs_bundle_id(String bs_bundle_id) {
            this.bs_bundle_id = bs_bundle_id;
        }

        public String getBs_approved() {
            return bs_approved;
        }

        public void setBs_approved(String bs_approved) {
            this.bs_approved = bs_approved;
        }

        public String getBs_course_validity_status() {
            return bs_course_validity_status;
        }

        public void setBs_course_validity_status(String bs_course_validity_status) {
            this.bs_course_validity_status = bs_course_validity_status;
        }

        public String getC_category() {
            return c_category;
        }

        public void setC_category(String c_category) {
            this.c_category = c_category;
        }

        public String getC_image() {
            return c_image;
        }

        public void setC_image(String c_image) {
            this.c_image = c_image;
        }

        public String getBs_bundle_details() {
            return bs_bundle_details;
        }

        public void setBs_bundle_details(String bs_bundle_details) {
            this.bs_bundle_details = bs_bundle_details;
        }
    }
}
