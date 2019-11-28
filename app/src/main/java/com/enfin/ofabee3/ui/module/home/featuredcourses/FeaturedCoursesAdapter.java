package com.enfin.ofabee3.ui.module.home.featuredcourses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.ui.base.baserecyclerview.BaseRecyclerviewAdapter;
import com.enfin.ofabee3.ui.base.baserecyclerview.OnRecyclerViewClickListener;
import com.enfin.ofabee3.ui.module.home.popularcourses.PopularCourse;
import com.enfin.ofabee3.ui.module.home.popularcourses.PopularCoursesViewHolder;

public class FeaturedCoursesAdapter extends BaseRecyclerviewAdapter<FeaturedCourse,
        OnRecyclerViewClickListener<FeaturedCourse>, FeaturedCoursesViewHolder> {
    private Context mContext;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private static final int VIEW_TYPE_NORMAL_ENROLLED = 2;

    /**
     * Base constructor.
     * Allocate adapter-related objects here if needed.
     *
     * @param context Context needed to retrieve LayoutInflater
     */
    public FeaturedCoursesAdapter(Context context) {
        super(context);
        this.mContext = context;
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
    public FeaturedCoursesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new FeaturedCoursesViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_course_item, parent, false));
            case VIEW_TYPE_NORMAL_ENROLLED:
                return new FeaturedCoursesViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_course_item, parent, false));

            //case VIEW_TYPE_LOADING:
            //return new ProgressHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
            default:
                return null;
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getFeaturedCourse().isEnrolled())
            return VIEW_TYPE_NORMAL;
        else
            return VIEW_TYPE_NORMAL;
    }

}
