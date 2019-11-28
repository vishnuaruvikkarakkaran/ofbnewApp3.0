package com.enfin.ofabee3.data.remote.model.Response;

public class UserLocationModel {


    /**
     * metadata : {"error":false,"message":"Location details fetched successfully!","status_code":"200"}
     * data : {"location_name":"IN"}
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
         * message : Location details fetched successfully!
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
         * location_name : IN
         */

        private String location_name;

        public String getLocation_name() {
            return location_name;
        }

        public void setLocation_name(String location_name) {
            this.location_name = location_name;
        }
    }
}
