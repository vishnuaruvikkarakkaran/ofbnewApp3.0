package com.enfin.ofabee3.data.remote.model.mycourses.response;

import java.io.Serializable;

/**
 * Created by SARATH on 22/8/19.
 */
public class NoDataFound implements Serializable {

    /**
     * metadata : {"error":true,"message":"No Courses Subscribed.","status_code":"404"}
     * data : {}
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

    public static class MetadataBean implements Serializable {
        /**
         * error : true
         * message : No Courses Subscribed.
         * status_code : 404
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

    public static class DataBean implements Serializable {
    }
}
