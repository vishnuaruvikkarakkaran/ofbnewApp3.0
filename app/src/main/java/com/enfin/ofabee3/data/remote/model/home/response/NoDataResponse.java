package com.enfin.ofabee3.data.remote.model.home.response;

import java.util.List;

public class NoDataResponse {
    /**
     * metadata : {"error":false,"message":"No data available","status_code":"204"}
     * data : {"courses":[{"title":"Popular Course","identifier":"1","total_records":0,"list":[]},{"title":"Featured Course","identifier":"2","total_records":0,"list":[]}],"banners":{"title":"Banners","identifier":"3","total_records":2,"list":[{"id":"1","mb_title":"https://testing-neyyar.enfinlabs.com/uploads/testing-neyyar.enfinlabs.com/mobile_banners/7c9a778d594c025dc99213019cdf6bcf.jpg"},{"id":"2","mb_title":"https://testing-neyyar.enfinlabs.com/uploads/testing-neyyar.enfinlabs.com/mobile_banners/d0c57a75e4fbb302e2f3774d76a8d0ad.jpg"}]}}
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
         * message : No data available
         * status_code : 204
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
         * courses : [{"title":"Popular Course","identifier":"1","total_records":0,"list":[]},{"title":"Featured Course","identifier":"2","total_records":0,"list":[]}]
         * banners : {"title":"Banners","identifier":"3","total_records":2,"list":[{"id":"1","mb_title":"https://testing-neyyar.enfinlabs.com/uploads/testing-neyyar.enfinlabs.com/mobile_banners/7c9a778d594c025dc99213019cdf6bcf.jpg"},{"id":"2","mb_title":"https://testing-neyyar.enfinlabs.com/uploads/testing-neyyar.enfinlabs.com/mobile_banners/d0c57a75e4fbb302e2f3774d76a8d0ad.jpg"}]}
         */

        private BannersBean banners;
        private List<CoursesBean> courses;

        public BannersBean getBanners() {
            return banners;
        }

        public void setBanners(BannersBean banners) {
            this.banners = banners;
        }

        public List<CoursesBean> getCourses() {
            return courses;
        }

        public void setCourses(List<CoursesBean> courses) {
            this.courses = courses;
        }

        public static class BannersBean {
            /**
             * title : Banners
             * identifier : 3
             * total_records : 2
             * list : [{"id":"1","mb_title":"https://testing-neyyar.enfinlabs.com/uploads/testing-neyyar.enfinlabs.com/mobile_banners/7c9a778d594c025dc99213019cdf6bcf.jpg"},{"id":"2","mb_title":"https://testing-neyyar.enfinlabs.com/uploads/testing-neyyar.enfinlabs.com/mobile_banners/d0c57a75e4fbb302e2f3774d76a8d0ad.jpg"}]
             */

            private String title;
            private String identifier;
            private int total_records;
            private List<ListBean> list;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getIdentifier() {
                return identifier;
            }

            public void setIdentifier(String identifier) {
                this.identifier = identifier;
            }

            public int getTotal_records() {
                return total_records;
            }

            public void setTotal_records(int total_records) {
                this.total_records = total_records;
            }

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public static class ListBean {
                /**
                 * id : 1
                 * mb_title : https://testing-neyyar.enfinlabs.com/uploads/testing-neyyar.enfinlabs.com/mobile_banners/7c9a778d594c025dc99213019cdf6bcf.jpg
                 */

                private String id;
                private String mb_title;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getMb_title() {
                    return mb_title;
                }

                public void setMb_title(String mb_title) {
                    this.mb_title = mb_title;
                }
            }
        }

        public static class CoursesBean {
            /**
             * title : Popular Course
             * identifier : 1
             * total_records : 0
             * list : []
             */

            private String title;
            private String identifier;
            private int total_records;
            private List<?> list;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getIdentifier() {
                return identifier;
            }

            public void setIdentifier(String identifier) {
                this.identifier = identifier;
            }

            public int getTotal_records() {
                return total_records;
            }

            public void setTotal_records(int total_records) {
                this.total_records = total_records;
            }

            public List<?> getList() {
                return list;
            }

            public void setList(List<?> list) {
                this.list = list;
            }
        }
    }
}
