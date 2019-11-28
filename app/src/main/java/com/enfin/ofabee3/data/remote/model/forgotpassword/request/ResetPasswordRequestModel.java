package com.enfin.ofabee3.data.remote.model.forgotpassword.request;

/**
 * Created by SARATH on 26/8/19.
 */
public class ResetPasswordRequestModel {

    /**
     * header : {"method":"otp_verification","token":""}
     * body : {"name":"","phone":"","password":"123456abc","email":"sarath.pv@enfintechnologies.com","mode":"2","otp":"12676","identifier":"2"}
     */

    private HeaderBean header;
    private BodyBean body;

    public HeaderBean getHeader() {
        return header;
    }

    public void setHeader(HeaderBean header) {
        this.header = header;
    }

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class HeaderBean {
        /**
         * method : otp_verification
         * token :
         */

        private String method;
        private String token;

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    public static class BodyBean {
        /**
         * name :
         * phone :
         * password : 123456abc
         * email : sarath.pv@enfintechnologies.com
         * mode : 2
         * otp : 12676
         * identifier : 2
         */

        private String name;
        private String phone;
        private String password;
        private String email;
        private String mode;
        private String otp;
        private String identifier;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }
    }
}
