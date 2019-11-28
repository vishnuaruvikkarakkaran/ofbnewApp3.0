package com.enfin.ofabee3.data.remote.model.Request;

public class RequestModel {


    /**
     * header : {"method":"","token":""}
     */

    private HeaderBean header;

    public HeaderBean getHeader() {
        return header;
    }

    public void setHeader(HeaderBean header) {
        this.header = header;
    }

    public static class HeaderBean {
        /**
         * method :
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
}
