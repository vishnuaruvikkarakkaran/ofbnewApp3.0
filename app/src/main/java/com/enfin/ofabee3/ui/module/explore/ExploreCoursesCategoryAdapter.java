package com.enfin.ofabee3.ui.module.explore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.remote.model.coursecategory.response.CourseCategoryResponse;
import com.enfin.ofabee3.ui.base.baserecyclerview.BaseRecyclerviewAdapter;
import com.enfin.ofabee3.ui.base.baserecyclerview.OnRecyclerViewClickListener;

import java.util.ArrayList;
import java.util.List;

public class ExploreCoursesCategoryAdapter extends BaseRecyclerviewAdapter<CourseCategoryResponse.DataBean,
        OnRecyclerViewClickListener<CourseCategoryResponse.DataBean>, ExploreCourseCategoryViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoadingAdded = false;
    private List<CourseCategoryResponse.DataBean> categoryList;
    public ArrayList<String> categoryIDList = new ArrayList<>();
    /**
     * Base constructor.
     * Allocate adapter-related objects here if needed.
     *  @param context Context needed to retrieve LayoutInflater
     * @param categoryList
     * @param categoryIDList
     */
    public ExploreCoursesCategoryAdapter(Context context, List<CourseCategoryResponse.DataBean> categoryList, ArrayList<String> categoryIDList) {
        super(context);
        this.categoryList = categoryList;
        this.categoryIDList = categoryIDList;
    }

    /**
     * To be implemented in as specific adapter
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public ExploreCourseCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ExploreCourseCategoryViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.popup_category_item, parent, false),categoryIDList);
            //case VIEW_TYPE_LOADING:
                //return new ProgressHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return getItems() == null ? 0 : getItems().size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoadingAdded) {
            return position == getItems().size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }
}
