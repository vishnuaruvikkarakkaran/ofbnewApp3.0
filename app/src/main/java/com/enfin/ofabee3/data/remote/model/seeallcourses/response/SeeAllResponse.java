package com.enfin.ofabee3.data.remote.model.seeallcourses.response;

import java.util.List;

public class SeeAllResponse {
    /**
     * metadata : {"error":false,"message":"data fetched successfully","status_code":"200"}
     * data : {"title":"Popular Courses","total_records":5,"identifier":"1","list":[{"item_id":"129","item_name":"loss course","item_price":"0","item_discount":"0","item_is_free":"1","item_type":"course","item_search":"2,40,42,3,5,41,37,25,64,26,59,29,58,63,66,69,70,72,73,74,75","item_category":[{"id":"2","ct_name":"Information Technology","ct_slug":"information-technology","ct_route_id":"682","ct_status":"1","ct_deleted":"0"},{"id":"40","ct_name":"Data Structure","ct_slug":"data-structure","ct_route_id":"917","ct_status":"1","ct_deleted":"0"},{"id":"42","ct_name":"Architecture Design and Analysis","ct_slug":"architecture-design-and-analysis","ct_route_id":"918","ct_status":"1","ct_deleted":"0"},{"id":"3","ct_name":"RRB ","ct_slug":"rrb","ct_route_id":"686","ct_status":"1","ct_deleted":"0"},{"id":"5","ct_name":"E-Commerce ","ct_slug":"e-commerce","ct_route_id":"782","ct_status":"1","ct_deleted":"0"},{"id":"41","ct_name":"Agriculture","ct_slug":"agriculture","ct_route_id":"918","ct_status":"1","ct_deleted":"0"},{"id":"37","ct_name":"Computer Science","ct_slug":"computer-science3","ct_route_id":"895","ct_status":"1","ct_deleted":"0"},{"id":"25","ct_name":"Learning Management System","ct_slug":"learning-management-system","ct_route_id":"822","ct_status":"0","ct_deleted":"0"},{"id":"64","ct_name":"Platform","ct_slug":"platform","ct_route_id":"998","ct_status":"0","ct_deleted":"0"},{"id":"26","ct_name":"Electronics","ct_slug":"electronics1","ct_route_id":"824","ct_status":"1","ct_deleted":"0"},{"id":"59","ct_name":"new 1","ct_slug":"new-14","ct_route_id":"989","ct_status":"0","ct_deleted":"0"},{"id":"29","ct_name":"Algorithms","ct_slug":"algorithms","ct_route_id":"825","ct_status":"1","ct_deleted":"0"},{"id":"58","ct_name":"EEE","ct_slug":"eee1","ct_route_id":"988","ct_status":"1","ct_deleted":"0"},{"id":"63","ct_name":"Skill Development","ct_slug":"skill-development","ct_route_id":"997","ct_status":"1","ct_deleted":"0"},{"id":"66","ct_name":"GATE","ct_slug":"gate","ct_route_id":"1028","ct_status":"1","ct_deleted":"0"},{"id":"69","ct_name":"Bio metrics","ct_slug":"bio-metrics","ct_route_id":"1030","ct_status":"0","ct_deleted":"0"},{"id":"70","ct_name":"Data Science","ct_slug":"data-science2","ct_route_id":"1030","ct_status":"1","ct_deleted":"0"},{"id":"72","ct_name":"bb","ct_slug":"bb","ct_route_id":"1108","ct_status":"1","ct_deleted":"0"},{"id":"73","ct_name":"മലയാളം എന്നുവേണം","ct_slug":"മലയള-എനനവണ","ct_route_id":"1126","ct_status":"1","ct_deleted":"0"},{"id":"74","ct_name":"മലയാളം എന്നുവേണം 1","ct_slug":"മലയള-എനനവണ-1","ct_route_id":"1127","ct_status":"1","ct_deleted":"0"},{"id":"75","ct_name":"മലയാളം എന്നുവേണം 2","ct_slug":"മലയള-എനനവണ-2","ct_route_id":"1128","ct_status":"1","ct_deleted":"0"}],"item_image":"https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/129/course/129.jpg?v_300x160.jpg","item_has_rating":"0","item_rating":"0","enrolled":false},{"item_id":"128","item_name":"SN college course","item_price":"0","item_discount":"0","item_is_free":"1","item_type":"course","item_search":"2","item_category":[{"id":"2","ct_name":"Information Technology","ct_slug":"information-technology","ct_route_id":"682","ct_status":"1","ct_deleted":"0"}],"item_image":"https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/128/course/128.jpg?_300x160.jpg","item_has_rating":"0","item_rating":"0","enrolled":false}]}
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
         * title : Popular Courses
         * total_records : 5
         * identifier : 1
         * list : [{"item_id":"129","item_name":"loss course","item_price":"0","item_discount":"0","item_is_free":"1","item_type":"course","item_search":"2,40,42,3,5,41,37,25,64,26,59,29,58,63,66,69,70,72,73,74,75","item_category":[{"id":"2","ct_name":"Information Technology","ct_slug":"information-technology","ct_route_id":"682","ct_status":"1","ct_deleted":"0"},{"id":"40","ct_name":"Data Structure","ct_slug":"data-structure","ct_route_id":"917","ct_status":"1","ct_deleted":"0"},{"id":"42","ct_name":"Architecture Design and Analysis","ct_slug":"architecture-design-and-analysis","ct_route_id":"918","ct_status":"1","ct_deleted":"0"},{"id":"3","ct_name":"RRB ","ct_slug":"rrb","ct_route_id":"686","ct_status":"1","ct_deleted":"0"},{"id":"5","ct_name":"E-Commerce ","ct_slug":"e-commerce","ct_route_id":"782","ct_status":"1","ct_deleted":"0"},{"id":"41","ct_name":"Agriculture","ct_slug":"agriculture","ct_route_id":"918","ct_status":"1","ct_deleted":"0"},{"id":"37","ct_name":"Computer Science","ct_slug":"computer-science3","ct_route_id":"895","ct_status":"1","ct_deleted":"0"},{"id":"25","ct_name":"Learning Management System","ct_slug":"learning-management-system","ct_route_id":"822","ct_status":"0","ct_deleted":"0"},{"id":"64","ct_name":"Platform","ct_slug":"platform","ct_route_id":"998","ct_status":"0","ct_deleted":"0"},{"id":"26","ct_name":"Electronics","ct_slug":"electronics1","ct_route_id":"824","ct_status":"1","ct_deleted":"0"},{"id":"59","ct_name":"new 1","ct_slug":"new-14","ct_route_id":"989","ct_status":"0","ct_deleted":"0"},{"id":"29","ct_name":"Algorithms","ct_slug":"algorithms","ct_route_id":"825","ct_status":"1","ct_deleted":"0"},{"id":"58","ct_name":"EEE","ct_slug":"eee1","ct_route_id":"988","ct_status":"1","ct_deleted":"0"},{"id":"63","ct_name":"Skill Development","ct_slug":"skill-development","ct_route_id":"997","ct_status":"1","ct_deleted":"0"},{"id":"66","ct_name":"GATE","ct_slug":"gate","ct_route_id":"1028","ct_status":"1","ct_deleted":"0"},{"id":"69","ct_name":"Bio metrics","ct_slug":"bio-metrics","ct_route_id":"1030","ct_status":"0","ct_deleted":"0"},{"id":"70","ct_name":"Data Science","ct_slug":"data-science2","ct_route_id":"1030","ct_status":"1","ct_deleted":"0"},{"id":"72","ct_name":"bb","ct_slug":"bb","ct_route_id":"1108","ct_status":"1","ct_deleted":"0"},{"id":"73","ct_name":"മലയാളം എന്നുവേണം","ct_slug":"മലയള-എനനവണ","ct_route_id":"1126","ct_status":"1","ct_deleted":"0"},{"id":"74","ct_name":"മലയാളം എന്നുവേണം 1","ct_slug":"മലയള-എനനവണ-1","ct_route_id":"1127","ct_status":"1","ct_deleted":"0"},{"id":"75","ct_name":"മലയാളം എന്നുവേണം 2","ct_slug":"മലയള-എനനവണ-2","ct_route_id":"1128","ct_status":"1","ct_deleted":"0"}],"item_image":"https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/129/course/129.jpg?v_300x160.jpg","item_has_rating":"0","item_rating":"0","enrolled":false},{"item_id":"128","item_name":"SN college course","item_price":"0","item_discount":"0","item_is_free":"1","item_type":"course","item_search":"2","item_category":[{"id":"2","ct_name":"Information Technology","ct_slug":"information-technology","ct_route_id":"682","ct_status":"1","ct_deleted":"0"}],"item_image":"https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/128/course/128.jpg?_300x160.jpg","item_has_rating":"0","item_rating":"0","enrolled":false}]
         */

        private String title;
        private int total_records;
        private String identifier;
        private List<ListBean> list;

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

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * item_id : 129
             * item_name : loss course
             * item_price : 0
             * item_discount : 0
             * item_is_free : 1
             * item_type : course
             * item_search : 2,40,42,3,5,41,37,25,64,26,59,29,58,63,66,69,70,72,73,74,75
             * item_category : [{"id":"2","ct_name":"Information Technology","ct_slug":"information-technology","ct_route_id":"682","ct_status":"1","ct_deleted":"0"},{"id":"40","ct_name":"Data Structure","ct_slug":"data-structure","ct_route_id":"917","ct_status":"1","ct_deleted":"0"},{"id":"42","ct_name":"Architecture Design and Analysis","ct_slug":"architecture-design-and-analysis","ct_route_id":"918","ct_status":"1","ct_deleted":"0"},{"id":"3","ct_name":"RRB ","ct_slug":"rrb","ct_route_id":"686","ct_status":"1","ct_deleted":"0"},{"id":"5","ct_name":"E-Commerce ","ct_slug":"e-commerce","ct_route_id":"782","ct_status":"1","ct_deleted":"0"},{"id":"41","ct_name":"Agriculture","ct_slug":"agriculture","ct_route_id":"918","ct_status":"1","ct_deleted":"0"},{"id":"37","ct_name":"Computer Science","ct_slug":"computer-science3","ct_route_id":"895","ct_status":"1","ct_deleted":"0"},{"id":"25","ct_name":"Learning Management System","ct_slug":"learning-management-system","ct_route_id":"822","ct_status":"0","ct_deleted":"0"},{"id":"64","ct_name":"Platform","ct_slug":"platform","ct_route_id":"998","ct_status":"0","ct_deleted":"0"},{"id":"26","ct_name":"Electronics","ct_slug":"electronics1","ct_route_id":"824","ct_status":"1","ct_deleted":"0"},{"id":"59","ct_name":"new 1","ct_slug":"new-14","ct_route_id":"989","ct_status":"0","ct_deleted":"0"},{"id":"29","ct_name":"Algorithms","ct_slug":"algorithms","ct_route_id":"825","ct_status":"1","ct_deleted":"0"},{"id":"58","ct_name":"EEE","ct_slug":"eee1","ct_route_id":"988","ct_status":"1","ct_deleted":"0"},{"id":"63","ct_name":"Skill Development","ct_slug":"skill-development","ct_route_id":"997","ct_status":"1","ct_deleted":"0"},{"id":"66","ct_name":"GATE","ct_slug":"gate","ct_route_id":"1028","ct_status":"1","ct_deleted":"0"},{"id":"69","ct_name":"Bio metrics","ct_slug":"bio-metrics","ct_route_id":"1030","ct_status":"0","ct_deleted":"0"},{"id":"70","ct_name":"Data Science","ct_slug":"data-science2","ct_route_id":"1030","ct_status":"1","ct_deleted":"0"},{"id":"72","ct_name":"bb","ct_slug":"bb","ct_route_id":"1108","ct_status":"1","ct_deleted":"0"},{"id":"73","ct_name":"മലയാളം എന്നുവേണം","ct_slug":"മലയള-എനനവണ","ct_route_id":"1126","ct_status":"1","ct_deleted":"0"},{"id":"74","ct_name":"മലയാളം എന്നുവേണം 1","ct_slug":"മലയള-എനനവണ-1","ct_route_id":"1127","ct_status":"1","ct_deleted":"0"},{"id":"75","ct_name":"മലയാളം എന്നുവേണം 2","ct_slug":"മലയള-എനനവണ-2","ct_route_id":"1128","ct_status":"1","ct_deleted":"0"}]
             * item_image : https://testing-ofabee.enfinlabs.com/uploads/testing-ofabee.enfinlabs.com/course/129/course/129.jpg?v_300x160.jpg
             * item_has_rating : 0
             * item_rating : 0
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
                 * id : 2
                 * ct_name : Information Technology
                 * ct_slug : information-technology
                 * ct_route_id : 682
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
