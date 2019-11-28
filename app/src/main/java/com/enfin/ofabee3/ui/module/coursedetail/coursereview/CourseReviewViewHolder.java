package com.enfin.ofabee3.ui.module.coursedetail.coursereview;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.enfin.ofabee3.R;
import com.enfin.ofabee3.ui.base.baserecyclerview.BaseViewHolder;
import com.enfin.ofabee3.ui.base.baserecyclerview.OnRecyclerViewClickListener;
import com.enfin.ofabee3.ui.module.explore.ExploreCourse;

public class CourseReviewViewHolder extends BaseViewHolder<ExploreCourse, OnRecyclerViewClickListener<ExploreCourse>> {

    private TextView courseName, coursePriceNew, coursePriceOld;
    private RatingBar courseRatingBar;
    private ImageView courseThumbnail;
    private Button isBundleLabel;
    private TextView categoryTV;

    public CourseReviewViewHolder(@NonNull View itemView) {
        super(itemView);
        courseName = itemView.findViewById(R.id.course_name_tv);
        coursePriceOld = itemView.findViewById(R.id.course_old_price);
        coursePriceNew = itemView.findViewById(R.id.course_new_price);
        courseRatingBar = itemView.findViewById(R.id.course_rating_bar);
        courseThumbnail = itemView.findViewById(R.id.course_thumbnail);
        isBundleLabel = itemView.findViewById(R.id.is_bundle_button);
        categoryTV = itemView.findViewById(R.id.category_tv);
    }


    /**
     * Bind data to the item and set listener if needed.
     *
     * @param item     object, associated with the item.
     * @param listener listener a listener {@link OnRecyclerViewClickListener} which has to b set at the item (if not `null`).
     */
    @Override
    public void onBind(ExploreCourse item, @Nullable OnRecyclerViewClickListener<ExploreCourse> listener) {
        courseName.setText(item.getCourseName());
        //coursePriceOld.setPaintFlags(coursePriceOld.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        courseRatingBar.setNumStars(5);

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(itemView.getContext());
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        Glide.with(itemView.getContext())
                .load(item.getCourseThumbnailImage())
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(courseThumbnail);


        if (!TextUtils.isEmpty(item.getOldItemPrice()) && !TextUtils.isEmpty(item.getCourseDiscount())) {

            int oldPrice = Integer.parseInt(item.getOldItemPrice());
            int discount = Integer.parseInt(item.getCourseDiscount());

            float rating = 0f;
            if (!TextUtils.isEmpty(item.getCourseItemRating()))
                rating = Float.parseFloat(item.getCourseItemRating());

            if (!TextUtils.isEmpty(item.getCourseCategory())) {
                categoryTV.setText(item.getCourseCategory());
            }

            if (item.getItemIsFree().equals("1")) {
                coursePriceOld.setVisibility(View.INVISIBLE);
                coursePriceNew.setText((Html.fromHtml("<font color=#8bc83f>FREE</font>")));
            } else {
                if (oldPrice > 0 && discount > 0)
                    coursePriceOld.setText(String.valueOf("₹ " + oldPrice));
                //int newPrice = oldPrice - discount;
                int newPrice = discount;
                if (newPrice > 0)
                    coursePriceNew.setText(String.valueOf("₹ " + newPrice));
                else if (newPrice == 0 && oldPrice > 0)
                    coursePriceNew.setText(String.valueOf("₹ " + oldPrice));
                else
                    coursePriceNew.setText((Html.fromHtml("<font color=#8bc83f>FREE</font>")));
            }

            if (item.getCourseItemType().equals("bundle"))
                isBundleLabel.setVisibility(View.VISIBLE);
            else
                isBundleLabel.setVisibility(View.INVISIBLE);

            if (rating > 0)
                courseRatingBar.setRating(rating);
            else
                courseRatingBar.setRating(rating);

        }

        if (listener != null)
            itemView.setOnClickListener(view -> listener.onItemClicked(item));
    }

}
