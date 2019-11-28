package com.enfin.ofabee3.data.remote.model.applycoupon.response;

public class ApplyCouponResponse {
    /**
     * metadata : {"error":false,"message":"This is valid Discount Coupon.","status_code":"200"}
     * data : {"type":"0","rate":"10","expiry":"2019-10-31 00:00:00"}
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
         * message : This is valid Discount Coupon.
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
         * type : 0
         * rate : 10
         * expiry : 2019-10-31 00:00:00
         */

        private String type;
        private String rate;
        private String expiry;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getExpiry() {
            return expiry;
        }

        public void setExpiry(String expiry) {
            this.expiry = expiry;
        }
    }
}
