package com.enfin.ofabee3.data.remote.model.home.request;

public class HomeRequest {
    /**
     * search_item :
     * category_ids : 41,37
     */

    private String search_item;
    private String category_ids;

    public String getSearch_item() {
        return search_item;
    }

    public void setSearch_item(String search_item) {
        this.search_item = search_item;
    }

    public String getCategory_ids() {
        return category_ids;
    }

    public void setCategory_ids(String category_ids) {
        this.category_ids = category_ids;
    }
}


