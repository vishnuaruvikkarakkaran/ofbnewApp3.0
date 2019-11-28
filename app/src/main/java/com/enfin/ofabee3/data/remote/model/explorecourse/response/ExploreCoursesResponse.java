package com.enfin.ofabee3.data.remote.model.explorecourse.response;

import java.util.List;

public class ExploreCoursesResponse {

    /**
     * metadata : {"error":false,"message":"data fetched successfully","status_code":"200"}
     * data : {"title":"All Courses","total_records":11,"all_course":[{"item_id":"46","item_name":"Algorithms","item_price":"10000","item_discount":"4000","item_is_free":"0","item_type":"course","item_search":"42,40,37,70","item_category":[{"id":"42","ct_name":"Architecture Design and Analysis","ct_slug":"architecture-design-and-analysis","ct_route_id":"918","ct_status":"1","ct_deleted":"0"},{"id":"40","ct_name":"Data Structure","ct_slug":"data-structure","ct_route_id":"917","ct_status":"1","ct_deleted":"0"},{"id":"37","ct_name":"Computer Science","ct_slug":"computer-science3","ct_route_id":"895","ct_status":"1","ct_deleted":"0"},{"id":"70","ct_name":"Data Science","ct_slug":"data-science2","ct_route_id":"1030","ct_status":"1","ct_deleted":"0"}],"item_image":"https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/46/course/46.jpg?v_300x160.jpg","item_has_rating":"1","item_rating":"4.0","enrolled":false},{"item_id":"110","item_name":"Carbon copy of understanding human body","item_price":"0","item_discount":"0","item_is_free":"1","item_type":"course","item_search":"2","item_category":[{"id":"2","ct_name":"Information Technology","ct_slug":"information-technology","ct_route_id":"682","ct_status":"1","ct_deleted":"0"}],"item_image":"https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/110/course/110.jpg?v_300x160.jpg","item_has_rating":"0","item_rating":"0","enrolled":false},{"item_id":"102","item_name":"Kotlin","item_price":"500","item_discount":"0","item_is_free":"0","item_type":"course","item_search":"25","item_category":[],"item_image":"https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/102/course/102.jpg?v_300x160.jpg","item_has_rating":"0","item_rating":"0","enrolled":true},{"item_id":"75","item_name":"Tutu course","item_price":"0","item_discount":"0","item_is_free":"1","item_type":"course","item_search":"37","item_category":[{"id":"37","ct_name":"Computer Science","ct_slug":"computer-science3","ct_route_id":"895","ct_status":"1","ct_deleted":"0"}],"item_image":"https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/75/course/75.jpg?v_300x160.jpg","item_has_rating":"0","item_rating":"0","enrolled":false},{"item_id":"105","item_name":"test course invelator","item_price":"0","item_discount":"0","item_is_free":"1","item_type":"course","item_search":"2","item_category":[{"id":"2","ct_name":"Information Technology","ct_slug":"information-technology","ct_route_id":"682","ct_status":"1","ct_deleted":"0"}],"item_image":"https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/105/course/105.jpg?v_300x160.jpg","item_has_rating":"0","item_rating":"0","enrolled":false},{"item_id":"48","item_name":"Data Structures","item_price":"60","item_discount":"0","item_is_free":"0","item_type":"course","item_search":"37","item_category":[{"id":"37","ct_name":"Computer Science","ct_slug":"computer-science3","ct_route_id":"895","ct_status":"1","ct_deleted":"0"}],"item_image":"https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/48/course/48.jpg?v_300x160.jpg","item_has_rating":"0","item_rating":"3.0","enrolled":false},{"item_id":"49","item_name":"Cyber Security","item_price":"100","item_discount":"50","item_is_free":"1","item_type":"course","item_search":"3","item_category":[{"id":"3","ct_name":"RRB ","ct_slug":"rrb","ct_route_id":"686","ct_status":"1","ct_deleted":"0"}],"item_image":"https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/49/course/49.jpg?v_300x160.jpg","item_has_rating":"0","item_rating":"2.7","enrolled":false},{"item_id":"76","item_name":"Digital Market","item_price":"255","item_discount":"125","item_is_free":"0","item_type":"course","item_search":"41,37","item_category":[{"id":"41","ct_name":"Agriculture","ct_slug":"agriculture","ct_route_id":"918","ct_status":"1","ct_deleted":"0"},{"id":"37","ct_name":"Computer Science","ct_slug":"computer-science3","ct_route_id":"895","ct_status":"1","ct_deleted":"0"}],"item_image":"https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/76/course/76.jpg?v_300x160.jpg","item_has_rating":"1","item_rating":"4.0","enrolled":true},{"item_id":"74","item_name":"Engineering Mechanics","item_price":"50","item_discount":"20","item_is_free":"0","item_type":"course","item_search":"40","item_category":[{"id":"40","ct_name":"Data Structure","ct_slug":"data-structure","ct_route_id":"917","ct_status":"1","ct_deleted":"0"}],"item_image":"https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/74/course/74.jpg?v_300x160.jpg","item_has_rating":"0","item_rating":"2.0","enrolled":false},{"item_id":"69","item_name":"Network Security","item_price":"0","item_discount":"0","item_is_free":"1","item_type":"course","item_search":"2","item_category":[{"id":"2","ct_name":"Information Technology","ct_slug":"information-technology","ct_route_id":"682","ct_status":"1","ct_deleted":"0"}],"item_image":"https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/69/course/69.jpg?v_300x160.jpg","item_has_rating":"0","item_rating":"0","enrolled":false},{"item_id":"82","item_name":"Liberated Structure","item_price":"0","item_discount":"0","item_is_free":"1","item_type":"course","item_search":"42","item_category":[{"id":"42","ct_name":"Architecture Design and Analysis","ct_slug":"architecture-design-and-analysis","ct_route_id":"918","ct_status":"1","ct_deleted":"0"}],"item_image":"https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/82/course/82.jpg?v_300x160.jpg","item_has_rating":"0","item_rating":"0","enrolled":false}]}
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
         * message : data fetched successfully
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
         * title : All Courses
         * total_records : 11
         * all_course : [{"item_id":"46","item_name":"Algorithms","item_price":"10000","item_discount":"4000","item_is_free":"0","item_type":"course","item_search":"42,40,37,70","item_category":[{"id":"42","ct_name":"Architecture Design and Analysis","ct_slug":"architecture-design-and-analysis","ct_route_id":"918","ct_status":"1","ct_deleted":"0"},{"id":"40","ct_name":"Data Structure","ct_slug":"data-structure","ct_route_id":"917","ct_status":"1","ct_deleted":"0"},{"id":"37","ct_name":"Computer Science","ct_slug":"computer-science3","ct_route_id":"895","ct_status":"1","ct_deleted":"0"},{"id":"70","ct_name":"Data Science","ct_slug":"data-science2","ct_route_id":"1030","ct_status":"1","ct_deleted":"0"}],"item_image":"https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/46/course/46.jpg?v_300x160.jpg","item_has_rating":"1","item_rating":"4.0","enrolled":false},{"item_id":"110","item_name":"Carbon copy of understanding human body","item_price":"0","item_discount":"0","item_is_free":"1","item_type":"course","item_search":"2","item_category":[{"id":"2","ct_name":"Information Technology","ct_slug":"information-technology","ct_route_id":"682","ct_status":"1","ct_deleted":"0"}],"item_image":"https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/110/course/110.jpg?v_300x160.jpg","item_has_rating":"0","item_rating":"0","enrolled":false},{"item_id":"102","item_name":"Kotlin","item_price":"500","item_discount":"0","item_is_free":"0","item_type":"course","item_search":"25","item_category":[],"item_image":"https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/102/course/102.jpg?v_300x160.jpg","item_has_rating":"0","item_rating":"0","enrolled":true},{"item_id":"75","item_name":"Tutu course","item_price":"0","item_discount":"0","item_is_free":"1","item_type":"course","item_search":"37","item_category":[{"id":"37","ct_name":"Computer Science","ct_slug":"computer-science3","ct_route_id":"895","ct_status":"1","ct_deleted":"0"}],"item_image":"https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/75/course/75.jpg?v_300x160.jpg","item_has_rating":"0","item_rating":"0","enrolled":false},{"item_id":"105","item_name":"test course invelator","item_price":"0","item_discount":"0","item_is_free":"1","item_type":"course","item_search":"2","item_category":[{"id":"2","ct_name":"Information Technology","ct_slug":"information-technology","ct_route_id":"682","ct_status":"1","ct_deleted":"0"}],"item_image":"https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/105/course/105.jpg?v_300x160.jpg","item_has_rating":"0","item_rating":"0","enrolled":false},{"item_id":"48","item_name":"Data Structures","item_price":"60","item_discount":"0","item_is_free":"0","item_type":"course","item_search":"37","item_category":[{"id":"37","ct_name":"Computer Science","ct_slug":"computer-science3","ct_route_id":"895","ct_status":"1","ct_deleted":"0"}],"item_image":"https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/48/course/48.jpg?v_300x160.jpg","item_has_rating":"0","item_rating":"3.0","enrolled":false},{"item_id":"49","item_name":"Cyber Security","item_price":"100","item_discount":"50","item_is_free":"1","item_type":"course","item_search":"3","item_category":[{"id":"3","ct_name":"RRB ","ct_slug":"rrb","ct_route_id":"686","ct_status":"1","ct_deleted":"0"}],"item_image":"https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/49/course/49.jpg?v_300x160.jpg","item_has_rating":"0","item_rating":"2.7","enrolled":false},{"item_id":"76","item_name":"Digital Market","item_price":"255","item_discount":"125","item_is_free":"0","item_type":"course","item_search":"41,37","item_category":[{"id":"41","ct_name":"Agriculture","ct_slug":"agriculture","ct_route_id":"918","ct_status":"1","ct_deleted":"0"},{"id":"37","ct_name":"Computer Science","ct_slug":"computer-science3","ct_route_id":"895","ct_status":"1","ct_deleted":"0"}],"item_image":"https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/76/course/76.jpg?v_300x160.jpg","item_has_rating":"1","item_rating":"4.0","enrolled":true},{"item_id":"74","item_name":"Engineering Mechanics","item_price":"50","item_discount":"20","item_is_free":"0","item_type":"course","item_search":"40","item_category":[{"id":"40","ct_name":"Data Structure","ct_slug":"data-structure","ct_route_id":"917","ct_status":"1","ct_deleted":"0"}],"item_image":"https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/74/course/74.jpg?v_300x160.jpg","item_has_rating":"0","item_rating":"2.0","enrolled":false},{"item_id":"69","item_name":"Network Security","item_price":"0","item_discount":"0","item_is_free":"1","item_type":"course","item_search":"2","item_category":[{"id":"2","ct_name":"Information Technology","ct_slug":"information-technology","ct_route_id":"682","ct_status":"1","ct_deleted":"0"}],"item_image":"https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/69/course/69.jpg?v_300x160.jpg","item_has_rating":"0","item_rating":"0","enrolled":false},{"item_id":"82","item_name":"Liberated Structure","item_price":"0","item_discount":"0","item_is_free":"1","item_type":"course","item_search":"42","item_category":[{"id":"42","ct_name":"Architecture Design and Analysis","ct_slug":"architecture-design-and-analysis","ct_route_id":"918","ct_status":"1","ct_deleted":"0"}],"item_image":"https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/82/course/82.jpg?v_300x160.jpg","item_has_rating":"0","item_rating":"0","enrolled":false}]
         */

        private String title;
        private int total_records;
        private List<AllCourseBean> all_course;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getTotal_records() {
            return total_records;
        }

        public void setTotal_records(int total_records) {
            this.total_records = total_records;
        }

        public List<AllCourseBean> getAll_course() {
            return all_course;
        }

        public void setAll_course(List<AllCourseBean> all_course) {
            this.all_course = all_course;
        }

        public static class AllCourseBean {
            /**
             * item_id : 46
             * item_name : Algorithms
             * item_price : 10000
             * item_discount : 4000
             * item_is_free : 0
             * item_type : course
             * item_search : 42,40,37,70
             * item_category : [{"id":"42","ct_name":"Architecture Design and Analysis","ct_slug":"architecture-design-and-analysis","ct_route_id":"918","ct_status":"1","ct_deleted":"0"},{"id":"40","ct_name":"Data Structure","ct_slug":"data-structure","ct_route_id":"917","ct_status":"1","ct_deleted":"0"},{"id":"37","ct_name":"Computer Science","ct_slug":"computer-science3","ct_route_id":"895","ct_status":"1","ct_deleted":"0"},{"id":"70","ct_name":"Data Science","ct_slug":"data-science2","ct_route_id":"1030","ct_status":"1","ct_deleted":"0"}]
             * item_image : https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/46/course/46.jpg?v_300x160.jpg
             * item_has_rating : 1
             * item_rating : 4.0
             * enrolled : false
             */

            private String item_id;
            private String item_name;
            private String item_price;
            private String item_discount;
            private String item_is_free;
            private String item_type;
            private String item_search;
            private String item_image;
            private String item_has_rating;
            private String item_rating;
            private boolean enrolled;
            private List<ItemCategoryBean> item_category;

            public String getItem_id() {
                return item_id;
            }

            public void setItem_id(String item_id) {
                this.item_id = item_id;
            }

            public String getItem_name() {
                return item_name;
            }

            public void setItem_name(String item_name) {
                this.item_name = item_name;
            }

            public String getItem_price() {
                return item_price;
            }

            public void setItem_price(String item_price) {
                this.item_price = item_price;
            }

            public String getItem_discount() {
                return item_discount;
            }

            public void setItem_discount(String item_discount) {
                this.item_discount = item_discount;
            }

            public String getItem_is_free() {
                return item_is_free;
            }

            public void setItem_is_free(String item_is_free) {
                this.item_is_free = item_is_free;
            }

            public String getItem_type() {
                return item_type;
            }

            public void setItem_type(String item_type) {
                this.item_type = item_type;
            }

            public String getItem_search() {
                return item_search;
            }

            public void setItem_search(String item_search) {
                this.item_search = item_search;
            }

            public String getItem_image() {
                return item_image;
            }

            public void setItem_image(String item_image) {
                this.item_image = item_image;
            }

            public String getItem_has_rating() {
                return item_has_rating;
            }

            public void setItem_has_rating(String item_has_rating) {
                this.item_has_rating = item_has_rating;
            }

            public String getItem_rating() {
                return item_rating;
            }

            public void setItem_rating(String item_rating) {
                this.item_rating = item_rating;
            }

            public boolean isEnrolled() {
                return enrolled;
            }

            public void setEnrolled(boolean enrolled) {
                this.enrolled = enrolled;
            }

            public List<ItemCategoryBean> getItem_category() {
                return item_category;
            }

            public void setItem_category(List<ItemCategoryBean> item_category) {
                this.item_category = item_category;
            }

            public static class ItemCategoryBean {
                /**
                 * id : 42
                 * ct_name : Architecture Design and Analysis
                 * ct_slug : architecture-design-and-analysis
                 * ct_route_id : 918
                 * ct_status : 1
                 * ct_deleted : 0
                 */

                private String id;
                private String ct_name;
                private String ct_slug;
                private String ct_route_id;
                private String ct_status;
                private String ct_deleted;

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
            }
        }
    }
}
