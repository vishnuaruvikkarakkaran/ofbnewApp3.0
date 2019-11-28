package com.enfin.ofabee3.data.remote.model.Response;


public class LoginResponseModel {

    /**
     * metadata : {"error":false,"message":"Login successfully!","status_code":"200"}
     * data : {"token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6Ijg3IiwiZW1haWxfaWQiOiJraXJhbi5qYisxMjNAZW5maW50ZWNobm9sb2dpZXMuY29tIiwibW9iaWxlIjoiOTg3NDU2MzIxNSJ9.xIwdYf1exXdfF21EtBrSdNhs2jydxjKZJcY3LrHYYvY","user":{"id":"87","us_name":"Kiran123","us_email":"kiran.jb+123@enfintechnologies.com","us_image":"https://testing-neyyar.enfinlabs.com/uploads/testing-neyyar.enfinlabs.com/user/87.jpg?v=563","us_about":"","us_phone":"9874563215","us_phone_verfified":"1","us_email_verified":"1","us_role_id":"2","us_category_id":"","us_status":"1"}}
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
         * message : Login successfully!
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
         * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6Ijg3IiwiZW1haWxfaWQiOiJraXJhbi5qYisxMjNAZW5maW50ZWNobm9sb2dpZXMuY29tIiwibW9iaWxlIjoiOTg3NDU2MzIxNSJ9.xIwdYf1exXdfF21EtBrSdNhs2jydxjKZJcY3LrHYYvY
         * user : {"id":"87","us_name":"Kiran123","us_email":"kiran.jb+123@enfintechnologies.com","us_image":"https://testing-neyyar.enfinlabs.com/uploads/testing-neyyar.enfinlabs.com/user/87.jpg?v=563","us_about":"","us_phone":"9874563215","us_phone_verfified":"1","us_email_verified":"1","us_role_id":"2","us_category_id":"","us_status":"1"}
         */

        private String token;
        private UserBean user;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * id : 87
             * us_name : Kiran123
             * us_email : kiran.jb+123@enfintechnologies.com
             * us_image : https://testing-neyyar.enfinlabs.com/uploads/testing-neyyar.enfinlabs.com/user/87.jpg?v=563
             * us_about :
             * us_phone : 9874563215
             * us_phone_verfified : 1
             * us_email_verified : 1
             * us_role_id : 2
             * us_category_id :
             * us_status : 1
             */

            private String id;
            private String us_name;
            private String us_email;
            private String us_image;
            private String us_about;
            private String us_phone;
            private String us_phone_verfified;
            private String us_email_verified;
            private String us_role_id;
            private String us_category_id;
            private String us_status;

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

            public String getUs_about() {
                return us_about;
            }

            public void setUs_about(String us_about) {
                this.us_about = us_about;
            }

            public String getUs_phone() {
                return us_phone;
            }

            public void setUs_phone(String us_phone) {
                this.us_phone = us_phone;
            }

            public String getUs_phone_verfified() {
                return us_phone_verfified;
            }

            public void setUs_phone_verfified(String us_phone_verfified) {
                this.us_phone_verfified = us_phone_verfified;
            }

            public String getUs_email_verified() {
                return us_email_verified;
            }

            public void setUs_email_verified(String us_email_verified) {
                this.us_email_verified = us_email_verified;
            }

            public String getUs_role_id() {
                return us_role_id;
            }

            public void setUs_role_id(String us_role_id) {
                this.us_role_id = us_role_id;
            }

            public String getUs_category_id() {
                return us_category_id;
            }

            public void setUs_category_id(String us_category_id) {
                this.us_category_id = us_category_id;
            }

            public String getUs_status() {
                return us_status;
            }

            public void setUs_status(String us_status) {
                this.us_status = us_status;
            }
        }
    }
}