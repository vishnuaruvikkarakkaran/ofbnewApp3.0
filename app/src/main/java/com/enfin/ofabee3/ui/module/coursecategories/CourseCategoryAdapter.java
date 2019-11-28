package com.enfin.ofabee3.ui.module.coursecategories;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.remote.model.coursecategory.response.CourseCategoryResponse;
import com.enfin.ofabee3.data.remote.model.mycourses.response.MyCoursesResponseModel;
import com.enfin.ofabee3.utils.RecyclerViewItemClickListener;

import java.util.ArrayList;
import java.util.List;

import hk.ids.gws.android.sclick.SClick;

/**
 * Created by SARATH on 20/8/19.
 */
public class CourseCategoryAdapter extends RecyclerView.Adapter<CourseCategoryAdapter.CoursesViewHolder> {

    Context mContext;
    List<CourseCategoryResponse.DataBean> mCourseCategoryList, mCourseCategoryListActive;
    private RecyclerViewItemClickListener itemClickListener;

    public CourseCategoryAdapter(Context context, List<CourseCategoryResponse.DataBean> coursesCategoryList, RecyclerViewItemClickListener listener) {
        this.mContext = context;
        this.mCourseCategoryList = coursesCategoryList;
        this.itemClickListener = listener;
        this.mCourseCategoryListActive = new ArrayList<>();
        filterList();
    }


    @NonNull
    @Override
    public CoursesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.course_category_item, parent, false);
        CoursesViewHolder myViewHolder = new CoursesViewHolder(v);
        return myViewHolder;
    }

    private void filterList() {
        for (CourseCategoryResponse.DataBean category : mCourseCategoryList) {
            if (category.getCt_status().equalsIgnoreCase("1"))
                mCourseCategoryListActive.add(category);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CourseCategoryAdapter.CoursesViewHolder holder, int position) {

        if (mCourseCategoryListActive.get(position).getCt_name() != null) {
            holder.courseCategoryName.setText(Html.fromHtml(mCourseCategoryListActive.get(position).getCt_name()));
        }
        if (mCourseCategoryListActive.get(position).isSelected()) {
            holder.courseCategoryCB.setChecked(true);
            itemClickListener.onItemSelected(position, true);
        } else {
            holder.courseCategoryCB.setChecked(false);
        }

        holder.itemView.setOnClickListener(view -> {
        });

        holder.courseCategoryCB.setOnCheckedChangeListener((compoundButton, selected) ->{
            //if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return;
            itemClickListener.onItemSelected(position, selected);
        });
    }

    @Override
    public int getItemCount() {
        return mCourseCategoryListActive.size();
    }

    public static class CoursesViewHolder extends RecyclerView.ViewHolder {

        TextView courseCategoryName;
        ImageView courseCategoryThumbnail;
        CheckBox courseCategoryCB;

        public CoursesViewHolder(@NonNull View itemView) {
            super(itemView);
            courseCategoryName = itemView.findViewById(R.id.text_course_category);
            courseCategoryThumbnail = itemView.findViewById(R.id.ic_course_category);
            courseCategoryCB = itemView.findViewById(R.id.cb_course_category);
        }
    }
}
