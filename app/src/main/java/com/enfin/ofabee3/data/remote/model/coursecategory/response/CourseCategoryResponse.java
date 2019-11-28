package com.enfin.ofabee3.data.remote.model.coursecategory.response;

import java.util.List;

/**
 * Created by SARATH on 26/8/19.
 */
public class CourseCategoryResponse {
    /**
     * metadata : {"error":false,"message":"Successfully Fetched.","status_code":"200"}
     * data : [{"id":"2","ct_name":"Information Technology","ct_slug":"information-technology","ct_route_id":"682","ct_status":"1","ct_deleted":"0","selected":true},{"id":"42","ct_name":"Architecture","ct_slug":"architecture","ct_route_id":"918","ct_status":"1","ct_deleted":"0","selected":true},{"id":"40","ct_name":"Neyyar Academy","ct_slug":"neyyar-academy","ct_route_id":"917","ct_status":"1","ct_deleted":"0","selected":false},{"id":"3","ct_name":"RRB ","ct_slug":"rrb","ct_route_id":"686","ct_status":"1","ct_deleted":"0","selected":false},{"id":"5","ct_name":"E-Commerce ","ct_slug":"e-commerce","ct_route_id":"782","ct_status":"1","ct_deleted":"0","selected":false},{"id":"41","ct_name":"Agriculture","ct_slug":"agriculture","ct_route_id":"918","ct_status":"1","ct_deleted":"0","selected":false},{"id":"37","ct_name":"Computer Science","ct_slug":"computer-science3","ct_route_id":"895","ct_status":"1","ct_deleted":"0","selected":false},{"id":"26","ct_name":"Electronics and Communication","ct_slug":"electronics-and-communication2","ct_route_id":"824","ct_status":"1","ct_deleted":"0","selected":false},{"id":"59","ct_name":"new 1","ct_slug":"new-14","ct_route_id":"989","ct_status":"1","ct_deleted":"0","selected":false},{"id":"29","ct_name":"Smart Stud","ct_slug":"smart-stud","ct_route_id":"825","ct_status":"1","ct_deleted":"0","selected":false},{"id":"58","ct_name":"Testing","ct_slug":"testing","ct_route_id":"988","ct_status":"1","ct_deleted":"0","selected":false},{"id":"63","ct_name":"Skill Development","ct_slug":"skill-development","ct_route_id":"997","ct_status":"1","ct_deleted":"0","selected":false},{"id":"66","ct_name":"Enfin test","ct_slug":"enfin-test1","ct_route_id":"1028","ct_status":"1","ct_deleted":"0","selected":false},{"id":"68","ct_name":"a","ct_slug":"a","ct_route_id":"1030","ct_status":"1","ct_deleted":"0","selected":false},{"id":"69","ct_name":"b","ct_slug":"b","ct_route_id":"1030","ct_status":"1","ct_deleted":"0","selected":false},{"id":"70","ct_name":"c","ct_slug":"c","ct_route_id":"1030","ct_status":"1","ct_deleted":"0","selected":false}]
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

    public static class MetadataBean {
        /**
         * error : false
         * message : Successfully Fetched.
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
         * id : 2
         * ct_name : Information Technology
         * ct_slug : information-technology
         * ct_route_id : 682
         * ct_status : 1
         * ct_deleted : 0
         * selected : true
         */

        private String id;
        private String ct_name;
        private String ct_slug;
        private String ct_route_id;
        private String ct_status;
        private String ct_deleted;
        private boolean selected;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCt_name() {
            return ct_name;
        }

        public void setCt_name(String ct_name) {
            this.ct_name = ct_name;
        }

        public String getCt_slug() {
            return ct_slug;
        }

        public void setCt_slug(String ct_slug) {
            this.ct_slug = ct_slug;
        }

        public String getCt_route_id() {
            return ct_route_id;
        }

        public void setCt_route_id(String ct_route_id) {
            this.ct_route_id = ct_route_id;
        }

        public String getCt_status() {
            return ct_status;
        }

        public void setCt_status(String ct_status) {
            this.ct_status = ct_status;
        }

        public String getCt_deleted() {
            return ct_deleted;
        }

        public void setCt_deleted(String ct_deleted) {
            this.ct_deleted = ct_deleted;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }
}
