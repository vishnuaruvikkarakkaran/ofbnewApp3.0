package com.enfin.ofabee3.data.remote.model.editprofile.response;

public class ImageUploadResponse {

    /**
     * metadata : {"error":false,"message":"Image upload completed","status_code":204}
     * data : {"upload_image":"https://testing-neyyar.enfinlabs.com/uploads/testing-neyyar.enfinlabs.com/user/87.jpg?v=255"}
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
         * message : Image upload completed
         * status_code : 204
         */

        private boolean error;
        private String message;
        private int status_code;

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

        public int getStatus_code() {
            return status_code;
        }

        public void setStatus_code(int status_code) {
            this.status_code = status_code;
        }
    }

    public static class DataBean {
        /**
         * upload_image : https://testing-neyyar.enfinlabs.com/uploads/testing-neyyar.enfinlabs.com/user/87.jpg?v=255
         */

        private String upload_image;

        public String getUpload_image() {
            return upload_image;
        }

        public void setUpload_image(String upload_image) {
            this.upload_image = upload_image;
        }
    }
}
