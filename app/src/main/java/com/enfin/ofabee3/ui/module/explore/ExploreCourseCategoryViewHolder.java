package com.enfin.ofabee3.ui.module.explore;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.remote.model.coursecategory.response.CourseCategoryResponse;
import com.enfin.ofabee3.ui.base.baserecyclerview.BaseViewHolder;
import com.enfin.ofabee3.ui.base.baserecyclerview.OnRecyclerViewClickListener;
import com.enfin.ofabee3.utils.OBLogger;

import java.util.ArrayList;

public class ExploreCourseCategoryViewHolder extends BaseViewHolder<CourseCategoryResponse.DataBean, OnRecyclerViewClickListener<CourseCategoryResponse.DataBean>> {

    private TextView categoryTV;
    private CheckBox categoryRB;
    public ArrayList<String> categoryIDList = new ArrayList<>();

    public ExploreCourseCategoryViewHolder(@NonNull View itemView, ArrayList<String> categoryIDList) {
        super(itemView);
        categoryTV = itemView.findViewById(R.id.text_course_category);
        categoryRB = itemView.findViewById(R.id.rb_course_category);
        this.categoryIDList = categoryIDList;
    }


    /**
     * Bind data to the item and set listener if needed.
     *
     * @param item     object, associated with the item.
     * @param listener listener a listener {@link OnRecyclerViewClickListener} which has to b set at the item (if not `null`).
     */
    @Override
    public void onBind(CourseCategoryResponse.DataBean item, @Nullable OnRecyclerViewClickListener<CourseCategoryResponse.DataBean> listener) {
        categoryTV.setText(Html.fromHtml(item.getCt_name()));
        int index = -1;
        index = categoryIDList.indexOf(item.getId());
        for (int i = 0; i < categoryIDList.size(); i++) {
            OBLogger.e("indexed data " + categoryIDList.get(i));
        }

        if (index != -1) {
            OBLogger.e("ID TRUE");
            categoryRB.setChecked(true);
            item.setSelected(true);
        }
        if (item.isSelected()) {
            categoryRB.setChecked(true);
        } else {
            categoryRB.setChecked(false);
        }

        itemView.setOnClickListener(view -> {
            if (categoryRB.isChecked()) {
                categoryRB.setChecked(false);
                item.setSelected(false);
            } else {
                categoryRB.setChecked(true);
                item.setSelected(true);
            }
            if (listener != null)
                listener.onItemClicked(item);

        });

        /*categoryRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    OBLogger.e("idp " + item.getId());
                    //item.setSelected(b);
                }
                if (listener != null)
                    listener.onItemClicked(item);
            }
        });*/
        /*for (String categoryId : categoryIDList) {
            OBLogger.e("ID OF CAT " + categoryId);
            if (categoryId.equals(item.getId())) {
                categoryRB.setChecked(true);
            } else {
                categoryRB.setChecked(false);
            }
        }*/
    }
}
