package com.enfin.ofabee3.ui.module.coursedetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.remote.model.coursedetail.response.CourseDetailResponseModel;
import com.enfin.ofabee3.ui.base.baserecyclerview.BaseRecyclerviewAdapter;
import com.enfin.ofabee3.ui.base.baserecyclerview.OnRecyclerViewClickListener;
import com.enfin.ofabee3.ui.module.home.popularcourses.PopularCourse;
import com.enfin.ofabee3.ui.module.home.popularcourses.PopularCoursesViewHolder;

public class CourseLectureAdapter extends BaseRecyclerviewAdapter<CourseLecture,
        OnRecyclerViewClickListener<CourseLecture>, CourseLectureViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoadingAdded = false;
    private CourseDetailResponseModel courseDetailResponseModel;
    /**
     * Base constructor.
     * Allocate adapter-related objects here if needed.
     *
     * @param context Context needed to retrieve LayoutInflater
     * @param mcourseDetailResponseModel
     */
    public CourseLectureAdapter(Context context, CourseDetailResponseModel mcourseDetailResponseModel) {
        super(context);
        this.courseDetailResponseModel =  mcourseDetailResponseModel;
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
    public CourseLectureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new CourseLectureViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.course_lecture_item, parent, false));
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
