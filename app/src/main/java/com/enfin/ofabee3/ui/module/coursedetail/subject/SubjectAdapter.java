package com.enfin.ofabee3.ui.module.coursedetail.subject;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.remote.model.coursedetail.response.BundleDetailResponseModel;
import com.enfin.ofabee3.data.remote.model.coursedetail.response.CourseDetailResponseModel;
import com.enfin.ofabee3.data.remote.model.mycourses.response.MyCoursesResponseModel;
import com.enfin.ofabee3.ui.module.contentdelivery.ContentDeliveryActivity;
import com.enfin.ofabee3.ui.module.coursedetail.CourseCurriculamItemListener;
import com.enfin.ofabee3.utils.OnSubjectItemClickListener;
import com.enfin.ofabee3.utils.OpenSansTextView;
import com.enfin.ofabee3.utils.RecyclerViewItemClickListener;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.List;

import hk.ids.gws.android.sclick.SClick;

/**
 * Created by SARATH on 20/8/19.
 */
public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

    private Context mContext;
    private OnSubjectItemClickListener listener;
    private BundleDetailResponseModel bundleDetailResponseModel;
    private List<BundleDetailResponseModel.DataBean.CoursesBean> subjectList = new ArrayList<>();

    public SubjectAdapter(Context context, BundleDetailResponseModel data, OnSubjectItemClickListener listener) {
        this.mContext = context;
        this.bundleDetailResponseModel = data;
        this.subjectList = bundleDetailResponseModel.getData().getCourses();
        this.listener = listener;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.subject_item, parent, false);
        SubjectViewHolder myViewHolder = new SubjectViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectAdapter.SubjectViewHolder holder, int position) {
        holder.sectionName.setText(Html.fromHtml(subjectList.get(position).getCb_title()));
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(mContext);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        Glide.with(mContext)
                .load(subjectList.get(position).getCb_image())
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.sectionImageParent);

        if (subjectList.get(position).getClass_count() > 0 && subjectList.get(position).getTest_count() > 0) {
            holder.classCountTV.setText(subjectList.get(position).getClass_count() + " Classes" + " | " + subjectList.get(position).getTest_count() + " Tests");
            //holder.classCountHeaderTV.setText(subjectList.get(position).getClass_count() + " Classes" + " | " + subjectList.get(position).getTest_count() + " Tests");
        } else if (subjectList.get(position).getClass_count() > 0 && subjectList.get(position).getTest_count() <= 0) {
            holder.classCountTV.setText(subjectList.get(position).getClass_count() + " Classes");
            //holder.classCountHeaderTV.setText(subjectList.get(position).getClass_count() + " Classes");

        } else if (subjectList.get(position).getClass_count() <= 0 && subjectList.get(position).getTest_count() > 0) {
            holder.classCountTV.setText(subjectList.get(position).getTest_count() + " Tests");
            //holder.classCountHeaderTV.setText(subjectList.get(position).getTest_count() + " Tests");

        } else if (subjectList.get(position).getClass_count() == 0 && subjectList.get(position).getTest_count() == 0) {
            holder.classCountTV.setText("");
            //holder.classCountHeaderTV.setText("");
        } else {
            holder.classCountTV.setText(subjectList.get(position).getClass_count() + " Classes" + " | " + subjectList.get(position).getTest_count() + " Tests");
            //holder.classCountHeaderTV.setText(subjectList.get(position).getClass_count() + " Classes" + " | " + subjectList.get(position).getTest_count() + " Tests");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return;
                listener.onItemSelected(position, subjectList.get(position).getId(), subjectList.get(position).getCb_title());
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public static class SubjectViewHolder extends RecyclerView.ViewHolder {

        private TextView sectionName, lectureHeaderName, classCountTV, classCountHeaderTV;
        private ImageView sectionImageChild, sectionImageParent;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionName = itemView.findViewById(R.id.course_name_tv_parent);
            lectureHeaderName = itemView.findViewById(R.id.course_name_tv_child);
            classCountTV = itemView.findViewById(R.id.class_count_tv);
            classCountHeaderTV = itemView.findViewById(R.id.class_count_header_tv);
            sectionImageParent = itemView.findViewById(R.id.course_thumbnail_parent);
        }
    }

}
