package com.enfin.ofabee3.data.remote.model.seeallcourses.request;

public class SeeAllCoursesRequest {
    /**
     * identifier : 1
     * category_ids : 25,37
     * search_keyword :
     * offset :
     * limit :
     */

    private String identifier;
    private String category_ids;
    private String search_keyword;
    private String offset;
    private String limit;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getCategory_ids() {
        return category_ids;
    }

    public void setCategory_ids(String category_ids) {
        this.category_ids = category_ids;
    }

    public String getSearch_keyword() {
        return search_keyword;
    }

    public void setSearch_keyword(String search_keyword) {
        this.search_keyword = search_keyword;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }
}
