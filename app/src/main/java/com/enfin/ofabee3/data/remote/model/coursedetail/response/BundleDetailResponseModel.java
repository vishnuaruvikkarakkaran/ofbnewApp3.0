package com.enfin.ofabee3.data.remote.model.coursedetail.response;

import java.util.List;

public class BundleDetailResponseModel {
    /**
     * metadata : {"error":false,"message":"All details fetched successfully.","status_code":"200"}
     * data : {"id":"26","c_title":"Test Bundle","c_is_free":"1","c_description":"<p>Test bundle<\/p>","c_access_validity":"0","c_price":"0","c_discount":"0","c_validity":"0","c_validity_date":"0000-00-00","c_image":"https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/catalog/26/catalog/26.jpg?v=423","c_status":"1","c_deleted":"0","c_courses":"[{\"id\":\"131\",\"course_name\":\"Appium\",\"course_code\":\"ENF 3\",\"status\":\"1\"},{\"id\":\"119\",\"course_name\":\"ASSESSMENTS (KAS)\",\"course_code\":\"KASAM\",\"status\":\"0\"}]","c_tax_method":"0","c_rating_enabled":"0","slug":"test-bundle","route_id":"844","item_type":"bundle","rating":0,"reviews":[],"access_expired":false,"courses":[{"id":"131","cb_title":"Appium","cb_description":"<p>New test<\/p>\r\n<figure><img src=\"https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/redactor/74c77312e84da4fa3a17c3e27383d3eb.jpg\" data-image=\"evlab035srhl\"><\/figure>","cb_category":"6","cb_image":"https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/131/course/131.jpg?v_300x160.jpg","class_count":10,"test_count":0},{"id":"119","cb_title":"ASSESSMENTS (KAS)","cb_description":"<p>ASSESSMENTS for KAS<\/p>","cb_category":"4","cb_image":"https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/119/course/119.jpg?v_300x160.jpg","class_count":1,"test_count":0}],"course_count":2,"topic_class_count":11,"topic_test_count":"0","tax_details":{"sgst":8,"cgst":8},"course_key_pass":{"key":"cnpwX3Rlc3RfOXZmdXhpbE1KcDdoN08=","pass":"NnMzVmJOQlRUZjdkcWp5eWliUEFsMmNj"}}
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
         * message : All details fetched successfully.
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
         * id : 26
         * c_title : Test Bundle
         * c_is_free : 1
         * c_description : <p>Test bundle</p>
         * c_access_validity : 0
         * c_price : 0
         * c_discount : 0
         * c_validity : 0
         * c_validity_date : 0000-00-00
         * c_image : https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/catalog/26/catalog/26.jpg?v=423
         * c_status : 1
         * c_deleted : 0
         * c_courses : [{"id":"131","course_name":"Appium","course_code":"ENF 3","status":"1"},{"id":"119","course_name":"ASSESSMENTS (KAS)","course_code":"KASAM","status":"0"}]
         * c_tax_method : 0
         * c_rating_enabled : 0
         * slug : test-bundle
         * route_id : 844
         * item_type : bundle
         * rating : 0
         * reviews : []
         * access_expired : false
         * courses : [{"id":"131","cb_title":"Appium","cb_description":"<p>New test<\/p>\r\n<figure><img src=\"https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/redactor/74c77312e84da4fa3a17c3e27383d3eb.jpg\" data-image=\"evlab035srhl\"><\/figure>","cb_category":"6","cb_image":"https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/131/course/131.jpg?v_300x160.jpg","class_count":10,"test_count":0},{"id":"119","cb_title":"ASSESSMENTS (KAS)","cb_description":"<p>ASSESSMENTS for KAS<\/p>","cb_category":"4","cb_image":"https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/119/course/119.jpg?v_300x160.jpg","class_count":1,"test_count":0}]
         * course_count : 2
         * topic_class_count : 11
         * topic_test_count : 0
         * tax_details : {"sgst":8,"cgst":8}
         * course_key_pass : {"key":"cnpwX3Rlc3RfOXZmdXhpbE1KcDdoN08=","pass":"NnMzVmJOQlRUZjdkcWp5eWliUEFsMmNj"}
         */

        private String id;
        private String c_title;
        private String c_is_free;
        private String c_description;
        private String c_access_validity;
        private String c_price;
        private String c_discount;
        private String c_validity;
        private String c_validity_date;
        private String c_image;
        private String c_status;
        private String c_deleted;
        private String c_courses;
        private String c_tax_method;
        private String c_rating_enabled;
        private String slug;
        private String route_id;
        private String item_type;
        private float rating;
        private boolean access_expired;
        private int course_count;
        private int topic_class_count;
        private String topic_test_count;
        private TaxDetailsBean tax_details;
        private CourseKeyPassBean course_key_pass;
        private List<?> reviews;
        private List<CoursesBean> courses;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getC_title() {
            return c_title;
        }

        public void setC_title(String c_title) {
            this.c_title = c_title;
        }

        public String getC_is_free() {
            return c_is_free;
        }

        public void setC_is_free(String c_is_free) {
            this.c_is_free = c_is_free;
        }

        public String getC_description() {
            return c_description;
        }

        public void setC_description(String c_description) {
            this.c_description = c_description;
        }

        public String getC_access_validity() {
            return c_access_validity;
        }

        public void setC_access_validity(String c_access_validity) {
            this.c_access_validity = c_access_validity;
        }

        public String getC_price() {
            return c_price;
        }

        public void setC_price(String c_price) {
            this.c_price = c_price;
        }

        public String getC_discount() {
            return c_discount;
        }

        public void setC_discount(String c_discount) {
            this.c_discount = c_discount;
        }

        public String getC_validity() {
            return c_validity;
        }

        public void setC_validity(String c_validity) {
            this.c_validity = c_validity;
        }

        public String getC_validity_date() {
            return c_validity_date;
        }

        public void setC_validity_date(String c_validity_date) {
            this.c_validity_date = c_validity_date;
        }

        public String getC_image() {
            return c_image;
        }

        public void setC_image(String c_image) {
            this.c_image = c_image;
        }

        public String getC_status() {
            return c_status;
        }

        public void setC_status(String c_status) {
            this.c_status = c_status;
        }

        public String getC_deleted() {
            return c_deleted;
        }

        public void setC_deleted(String c_deleted) {
            this.c_deleted = c_deleted;
        }

        public String getC_courses() {
            return c_courses;
        }

        public void setC_courses(String c_courses) {
            this.c_courses = c_courses;
        }

        public String getC_tax_method() {
            return c_tax_method;
        }

        public void setC_tax_method(String c_tax_method) {
            this.c_tax_method = c_tax_method;
        }

        public String getC_rating_enabled() {
            return c_rating_enabled;
        }

        public void setC_rating_enabled(String c_rating_enabled) {
            this.c_rating_enabled = c_rating_enabled;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public String getRoute_id() {
            return route_id;
        }

        public void setRoute_id(String route_id) {
            this.route_id = route_id;
        }

        public String getItem_type() {
            return item_type;
        }

        public void setItem_type(String item_type) {
            this.item_type = item_type;
        }

        public float getRating() {
            return rating;
        }

        public void setRating(float rating) {
            this.rating = rating;
        }

        public boolean isAccess_expired() {
            return access_expired;
        }

        public void setAccess_expired(boolean access_expired) {
            this.access_expired = access_expired;
        }

        public int getCourse_count() {
            return course_count;
        }

        public void setCourse_count(int course_count) {
            this.course_count = course_count;
        }

        public int getTopic_class_count() {
            return topic_class_count;
        }

        public void setTopic_class_count(int topic_class_count) {
            this.topic_class_count = topic_class_count;
        }

        public String getTopic_test_count() {
            return topic_test_count;
        }

        public void setTopic_test_count(String topic_test_count) {
            this.topic_test_count = topic_test_count;
        }

        public TaxDetailsBean getTax_details() {
            return tax_details;
        }

        public void setTax_details(TaxDetailsBean tax_details) {
            this.tax_details = tax_details;
        }

        public CourseKeyPassBean getCourse_key_pass() {
            return course_key_pass;
        }

        public void setCourse_key_pass(CourseKeyPassBean course_key_pass) {
            this.course_key_pass = course_key_pass;
        }

        public List<?> getReviews() {
            return reviews;
        }

        public void setReviews(List<?> reviews) {
            this.reviews = reviews;
        }

        public List<CoursesBean> getCourses() {
            return courses;
        }

        public void setCourses(List<CoursesBean> courses) {
            this.courses = courses;
        }

        public static class TaxDetailsBean {
            /**
             * sgst : 8
             * cgst : 8
             */

            private int sgst;
            private int cgst;

            public int getSgst() {
                return sgst;
            }

            public void setSgst(int sgst) {
                this.sgst = sgst;
            }

            public int getCgst() {
                return cgst;
            }

            public void setCgst(int cgst) {
                this.cgst = cgst;
            }
        }

        public static class CourseKeyPassBean {
            /**
             * key : cnpwX3Rlc3RfOXZmdXhpbE1KcDdoN08=
             * pass : NnMzVmJOQlRUZjdkcWp5eWliUEFsMmNj
             */

            private String key;
            private String pass;

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public String getPass() {
                return pass;
            }

            public void setPass(String pass) {
                this.pass = pass;
            }
        }

        public static class CoursesBean {
            /**
             * id : 131
             * cb_title : Appium
             * cb_description : <p>New test</p>
             * <figure><img src="https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/redactor/74c77312e84da4fa3a17c3e27383d3eb.jpg" data-image="evlab035srhl"></figure>
             * cb_category : 6
             * cb_image : https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/131/course/131.jpg?v_300x160.jpg
             * class_count : 10
             * test_count : 0
             */

            private String id;
            private String cb_title;
            private String cb_description;
            private String cb_category;
            private String cb_image;
            private int class_count;
            private int test_count;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getCb_title() {
                return cb_title;
            }

            public void setCb_title(String cb_title) {
                this.cb_title = cb_title;
            }

            public String getCb_description() {
                return cb_description;
            }

            public void setCb_description(String cb_description) {
                this.cb_description = cb_description;
            }

            public String getCb_category() {
                return cb_category;
            }

            public void setCb_category(String cb_category) {
                this.cb_category = cb_category;
            }

            public String getCb_image() {
                return cb_image;
            }

            public void setCb_image(String cb_image) {
                this.cb_image = cb_image;
            }

            public int getClass_count() {
                return class_count;
            }

            public void setClass_count(int class_count) {
                this.class_count = class_count;
            }

            public int getTest_count() {
                return test_count;
            }

            public void setTest_count(int test_count) {
                this.test_count = test_count;
            }
        }
    }
}
