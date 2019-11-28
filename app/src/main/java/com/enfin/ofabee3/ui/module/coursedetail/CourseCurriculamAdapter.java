package com.enfin.ofabee3.ui.module.coursedetail;

import android.content.Context;

import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.remote.model.coursedetail.response.CourseDetailResponseModel;
import com.enfin.ofabee3.ui.base.baserecyclerview.OnRecyclerViewClickListener;
import com.enfin.ofabee3.ui.module.freepreview.FreePreviewActivity;
import com.enfin.ofabee3.utils.NetworkUtil;
import com.enfin.ofabee3.utils.OBLogger;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.List;

import hk.ids.gws.android.sclick.SClick;

/**
 * Created by Sarath on 02/10/19.
 */

public class CourseCurriculamAdapter extends RecyclerView.Adapter<CourseCurriculamAdapter.CourseCurriculamViewHolder> implements OnRecyclerViewClickListener {

    private List<CourseLecture> lecturesList = new ArrayList<>();
    private List<CourseDetailResponseModel.DataBean.CurriculumBean.ListBean> sectionList = new ArrayList<>();
    private CourseLectureAdapter adapter;
    private Context context;
    private CourseCurriculamItemListener listener;
    private CourseDetailResponseModel courseDetailResponseModel;
    private int expandedCellPosition = -1;
    private CourseCurriculamViewHolder oldHolder;

    public CourseCurriculamAdapter(Context context, CourseCurriculamItemListener mListener, CourseDetailResponseModel courseDetailResponseData) {
        this.context = context;
        this.listener = mListener;
        this.courseDetailResponseModel = courseDetailResponseData;
        this.sectionList = courseDetailResponseModel.getData().getCurriculum().getList();
    }

    @Override
    public CourseCurriculamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.course_curriculam_item, parent, false);
        return new CourseCurriculamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CourseCurriculamViewHolder holder, int position) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        holder.sectionName.setText(Html.fromHtml(sectionList.get(position).getTopic_name()));
        holder.lectureHeaderName.setText(Html.fromHtml(sectionList.get(position).getTopic_name()));

        if (sectionList.get(position).getTopic_class_count() > 0 && sectionList.get(position).getTopic_test_count() > 0) {
            holder.classCountTV.setText(sectionList.get(position).getTopic_class_count() + " Classes" + " | " + sectionList.get(position).getTopic_test_count() + " Tests");
            holder.classCountHeaderTV.setText(sectionList.get(position).getTopic_class_count() + " Classes" + " | " + sectionList.get(position).getTopic_test_count() + " Tests");
        } else if (sectionList.get(position).getTopic_class_count() > 0 && sectionList.get(position).getTopic_test_count() <= 0) {
            holder.classCountTV.setText(sectionList.get(position).getTopic_class_count() + " Classes");
            holder.classCountHeaderTV.setText(sectionList.get(position).getTopic_class_count() + " Classes");

        } else if (sectionList.get(position).getTopic_class_count() <= 0 && sectionList.get(position).getTopic_test_count() > 0) {
            holder.classCountTV.setText(sectionList.get(position).getTopic_test_count() + " Tests");
            holder.classCountHeaderTV.setText(sectionList.get(position).getTopic_test_count() + " Tests");

        } else if (sectionList.get(position).getTopic_class_count() == 0 && sectionList.get(position).getTopic_test_count() == 0) {
            holder.classCountTV.setText("");
            holder.classCountHeaderTV.setText("");
        } else {
            holder.classCountTV.setText(sectionList.get(position).getTopic_class_count() + " Classes" + " | " + sectionList.get(position).getTopic_test_count() + " Tests");
            holder.classCountHeaderTV.setText(sectionList.get(position).getTopic_class_count() + " Classes" + " | " + sectionList.get(position).getTopic_test_count() + " Tests");
        }

        holder.lectureListRV.setLayoutManager(linearLayoutManager);
        holder.lectureListRV.setHasFixedSize(true);

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        Glide.with(context)
                .load(sectionList.get(position).getTopic_image())
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.sectionImageParent);

        Glide.with(context)
                .load(sectionList.get(position).getTopic_image())
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.sectionImageChild);

        adapter = new CourseLectureAdapter(context, courseDetailResponseModel);
        adapter.setListener(this);
        holder.lectureListRV.setAdapter(adapter);
        if (sectionList.size() > 0)
            loadPopularCourseList(position);

        if (courseDetailResponseModel.getData().getCb_has_lecture_image().equals("0")) {
            holder.sectionImageParent.setVisibility(View.GONE);
            holder.sectionImageChild.setVisibility(View.GONE);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            );
            holder.llParentBody.setLayoutParams(param);
            holder.llChildBody.setLayoutParams(param);
        }

        holder.foldingCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return;
                if (expandedCellPosition != -1 && expandedCellPosition != position) {
                    if (oldHolder.foldingCell.isUnfolded())
                        oldHolder.foldingCell.fold(true);
                }
                expandedCellPosition = position;
                oldHolder = holder;
                holder.foldingCell.toggle(true);
                listener.itemClickListener(position);
            }
        });

        if (position == 0) {
            holder.foldingCell.post(new Runnable() {
                @Override
                public void run() {
                    holder.foldingCell.performClick();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return sectionList == null ? 0 : sectionList.size();
    }

    @Override
    public void onItemClicked(Object item) {
        if (NetworkUtil.isConnected(context)) {
            //if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return;
            if (item instanceof CourseLecture) {
                Intent freepreview = new Intent(context, FreePreviewActivity.class);
                freepreview.putExtra("videoID", ((CourseLecture) item).getPreviewURL());
                context.startActivity(freepreview);
            }
        } else {
            Toast.makeText(context, "Please check your internet connection!!", Toast.LENGTH_SHORT).show();
        }
    }

    public static class CourseCurriculamViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView lectureListRV;
        private FoldingCell foldingCell;
        private TextView sectionName, lectureHeaderName, classCountTV, classCountHeaderTV;
        private ImageView sectionImageChild, sectionImageParent;
        private LinearLayout llParentBody, llChildBody;

        public CourseCurriculamViewHolder(View itemView) {
            super(itemView);
            llParentBody = itemView.findViewById(R.id.ll_parent_content);
            llChildBody = itemView.findViewById(R.id.ll_child_content);
            foldingCell = itemView.findViewById(R.id.folding_cell);
            lectureListRV = itemView.findViewById(R.id.course_lecture_rv);
            sectionName = itemView.findViewById(R.id.course_name_tv_parent);
            lectureHeaderName = itemView.findViewById(R.id.course_name_tv_child);
            classCountTV = itemView.findViewById(R.id.class_count_tv);
            classCountHeaderTV = itemView.findViewById(R.id.class_count_header_tv);
            sectionImageChild = itemView.findViewById(R.id.course_thumbnail_child);
            sectionImageParent = itemView.findViewById(R.id.course_thumbnail_parent);
        }
    }

    private void loadPopularCourseList(int position) {
        lecturesList.clear();
        for (int i = 0; i < courseDetailResponseModel.getData().getCurriculum().getList().get(position).getTopic_class_count(); i++) {
            CourseLecture courseLecture = new CourseLecture();
            courseLecture.setLectureName(courseDetailResponseModel.getData().getCurriculum().getList().get(position).getTopic_classes().get(i).getCl_lecture_name());
            courseLecture.setLectureThumbnail(courseDetailResponseModel.getData().getCurriculum().getList().get(position).getTopic_classes().get(i).getLecture_image());
            courseLecture.setLectureImageEnabled(courseDetailResponseModel.getData().getCb_has_lecture_image());
            OBLogger.e("PRE " + courseDetailResponseModel.getData().getCurriculum().getList().get(position).getTopic_classes().get(i).getLecture_preview_enabled());

            if (courseDetailResponseModel.getData().getCurriculum().getList().get(position).getTopic_classes().get(i).getLecture_preview_enabled() != null && !courseDetailResponseModel.getData().getCurriculum().getList().get(position).getTopic_classes().get(i).getLecture_preview_enabled().equals("") && !courseDetailResponseModel.getData().getCurriculum().getList().get(position).getTopic_classes().get(i).getLecture_preview_enabled().equals(" ")) {
                courseLecture.setPreviewEnabled(courseDetailResponseModel.getData().getCurriculum().getList().get(position).getTopic_classes().get(i).getLecture_preview_enabled());
                if (courseDetailResponseModel.getData().getCurriculum().getList().get(position).getTopic_classes().get(i).getLecture_preview_enabled().equals("1")) {
                    if (courseDetailResponseModel.getData().getCurriculum().getList().get(position).getTopic_classes().get(i).getLecture_preview() != null)
                        courseLecture.setPreviewURL(courseDetailResponseModel.getData().getCurriculum().getList().get(position).getTopic_classes().get(i).getLecture_preview().getVimeo_id());
                    else {
                        courseLecture.setPreviewEnabled("0");
                    }
                }
            } else {
                courseLecture.setPreviewEnabled("0");
            }
            lecturesList.add(courseLecture);
        }
        adapter.setItems(lecturesList);
    }

}