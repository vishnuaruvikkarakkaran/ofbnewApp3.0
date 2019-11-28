package com.enfin.ofabee3.ui.module.coursedetail;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.enfin.ofabee3.R;
import com.enfin.ofabee3.ui.base.baserecyclerview.BaseViewHolder;
import com.enfin.ofabee3.ui.base.baserecyclerview.OnRecyclerViewClickListener;
import com.enfin.ofabee3.ui.module.home.popularcourses.PopularCourse;
import com.enfin.ofabee3.utils.OBLogger;

import hk.ids.gws.android.sclick.SClick;

public class CourseLectureViewHolder extends BaseViewHolder<CourseLecture, OnRecyclerViewClickListener<CourseLecture>> {

    private TextView courseName, coursePriceNew, coursePriceOld, lectureIndex;
    private RatingBar courseRatingBar;
    private ImageView courseThumbnail;
    private Button isBundleLabel;
    private TextView categoryTV, previewTV;
    private FrameLayout lockFL, bodyFL;
    private LinearLayout bodyLL;
    private CardView imageCardView;

    public CourseLectureViewHolder(@NonNull View itemView) {
        super(itemView);
        courseName = itemView.findViewById(R.id.course_name_tv);
        courseThumbnail = itemView.findViewById(R.id.course_thumbnail);
        lectureIndex = itemView.findViewById(R.id.course_lecture_index);
        previewTV = itemView.findViewById(R.id.preview_status);
        lockFL = itemView.findViewById(R.id.lock_fl);
        bodyLL = itemView.findViewById(R.id.ll_body);
        bodyFL = itemView.findViewById(R.id.course_thumbnail_fl);
        imageCardView = itemView.findViewById(R.id.parent_layout);
    }


    /**
     * Bind data to the item and set listener if needed.
     *
     * @param item     object, associated with the item.
     * @param listener listener a listener {@link OnRecyclerViewClickListener} which has to b set at the item (if not `null`).
     */
    @Override
    public void onBind(CourseLecture item, @Nullable OnRecyclerViewClickListener<CourseLecture> listener) {
        courseName.setText(Html.fromHtml(item.getLectureName()));
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(itemView.getContext());
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        Glide.with(itemView.getContext())
                .load(item.getLectureThumbnail())
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(courseThumbnail);

        lectureIndex.setText(String.valueOf(getAdapterPosition() + 1));
        OBLogger.e(item.getPreviewEnabled());

        if (item.isLectureImageEnabled().equals("0")) {
            courseThumbnail.setVisibility(View.GONE);
            bodyFL.setVisibility(View.GONE);
            imageCardView.setVisibility(View.GONE);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.9f);
            LinearLayout.LayoutParams param_index = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.1f);
            lectureIndex.setLayoutParams(param_index);
            bodyLL.setLayoutParams(param);
        }

        if (item.getPreviewEnabled().equals("0"))
            lockFL.setVisibility(View.VISIBLE);
        else
            lockFL.setVisibility(View.GONE);

        if (listener != null) {
            if (item.getPreviewEnabled().equals("1"))
                itemView.setOnClickListener(view -> {
                    if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return;
                    listener.onItemClicked(item);
                });
            else
                previewTV.setVisibility(View.GONE);
        }
    }

}
