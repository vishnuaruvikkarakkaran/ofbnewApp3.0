package com.enfin.ofabee3.ui.base.baserecyclerview;

public interface OnRecyclerViewClickListener<T> extends BaseRecyclerViewListener {
    /**
     * Item has been clicked.
     *
     * @param item object associated with the clicked item.
     */
    void onItemClicked(T item);
}
