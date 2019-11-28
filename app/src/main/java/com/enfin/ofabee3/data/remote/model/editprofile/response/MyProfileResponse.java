package com.enfin.ofabee3.data.remote.model.editprofile.response;

public class MyProfileResponse {

    /**
     * metadata : {"error":false,"message":"User details fetched successfully.","status_code":"200"}
     * data : {"id":"87","us_name":"Kiran123","us_email":"kiran.jb+123@enfintechnologies.com","us_image":"https://testing-neyyar.enfinlabs.com/uploads/testing-neyyar.enfinlabs.com/user/87.jpg?v=148","us_phone":"9874563215","us_category_id":"1,3","score":"1815","grade":"-"}
     */

    private MetadataBean metadata;
    private DataBean data;

    public MetadataBean getMetadata() {
        return metadata;
    }

    public void setMetadata(MetadataBean metadata) {
        this.metadata = metadata;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class MetadataBean {
        /**
         * error : false
         * message : User details fetched successfully.
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

    public static class DataBean {
        /**
         * id : 87
         * us_name : Kiran123
         * us_email : kiran.jb+123@enfintechnologies.com
         * us_image : https://testing-neyyar.enfinlabs.com/uploads/testing-neyyar.enfinlabs.com/user/87.jpg?v=148
         * us_phone : 9874563215
         * us_category_id : 1,3
         * score : 1815
         * grade : -
         */

        private String id;
        private String us_name;
        private String us_email;
        private String us_image;
        private String us_phone;
        private String us_category_id;
        private String score;
        private String grade;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUs_name() {
            return us_name;
        }

        public void setUs_name(String us_name) {
            this.us_name = us_name;
        }

        public String getUs_email() {
            return us_email;
        }

        public void setUs_email(String us_email) {
            this.us_email = us_email;
        }

        public String getUs_image() {
            return us_image;
        }

        public void setUs_image(String us_image) {
            this.us_image = us_image;
        }

        public String getUs_phone() {
            return us_phone;
        }

        public void setUs_phone(String us_phone) {
            this.us_phone = us_phone;
        }

        public String getUs_category_id() {
            return us_category_id;
        }

        public void setUs_category_id(String us_category_id) {
            this.us_category_id = us_category_id;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }
    }
}
