package com.enfin.ofabee3.ui.module.mycourses;

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
import com.enfin.ofabee3.data.remote.model.mycourses.response.MyCoursesResponseModel;
import com.enfin.ofabee3.ui.module.bundlesubject.BundleSubjectActivity;
import com.enfin.ofabee3.ui.module.contentdelivery.ContentDeliveryActivity;
import com.enfin.ofabee3.ui.module.home.popularcourses.PopularCourse;
import com.enfin.ofabee3.ui.module.home.popularcourses.PopularCoursesViewHolder;
import com.enfin.ofabee3.utils.AvenirTextView;
import com.enfin.ofabee3.utils.Constants;
import com.enfin.ofabee3.utils.NetworkUtil;
import com.enfin.ofabee3.utils.OpenSansTextView;

import java.util.List;

import hk.ids.gws.android.sclick.SClick;

/**
 * Created by SARATH on 20/8/19.
 */
public class MycoursesAdapter extends RecyclerView.Adapter<MycoursesAdapter.CoursesViewHolder> {

    Context mContext;
    List<MyCoursesResponseModel.DataBean> myCoursesList;
    private static final int VIEW_TYPE_COURSE = 0;
    private static final int VIEW_TYPE_BUNDLE = 1;

    public MycoursesAdapter(Context context, List<MyCoursesResponseModel.DataBean> coursesList) {
        this.mContext = context;
        this.myCoursesList = coursesList;
    }

    @NonNull
    @Override
    public CoursesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*View v = LayoutInflater.from(mContext).inflate(R.layout.course_item, parent, false);
        CoursesViewHolder myViewHolder = new CoursesViewHolder(v);
        return myViewHolder;*/
        switch (viewType) {
            case VIEW_TYPE_COURSE:
                return new CoursesViewHolder(
                        LayoutInflater.from(mContext).inflate(R.layout.course_item, parent, false));
            case VIEW_TYPE_BUNDLE:
                return new CoursesViewHolder(
                        LayoutInflater.from(mContext).inflate(R.layout.bundle_item, parent, false));

            //case VIEW_TYPE_LOADING:
            //return new ProgressHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MycoursesAdapter.CoursesViewHolder holder, int position) {
        if (myCoursesList.get(position).getItem_type().equals("course"))
            updateCourseUI(holder, position);
        if (myCoursesList.get(position).getItem_type().equals("bundle"))
            updateBundleUI(holder, position);
    }

    private void updateCourseUI(MycoursesAdapter.CoursesViewHolder holder, int position) {
        holder.courseName.setText(Html.fromHtml(myCoursesList.get(position).getCb_title()));
        holder.courseProgress.setText(String.valueOf(myCoursesList.get(position).getCs_percentage()) + "%");
        holder.courseProgressBar.setProgress(myCoursesList.get(position).getCs_percentage());
        holder.itemView.setEnabled(true);
        courseLabelUpdate(holder, myCoursesList.get(position));

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(mContext);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        Glide.with(mContext)
                .load(myCoursesList.get(position).getCb_image())
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.courseThumbnail);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtil.isConnected(mContext)) {
                    if (myCoursesList.get(position).getCs_approved().equals("1")) {
                        if (!myCoursesList.get(position).isExpired()) {
                            if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return;
                            /*Intent i = new Intent(mContext, BundleSubjectActivity.class);
                            i.putExtra(Constants.COURSE_ID, "27");
                            mContext.startActivity(i);*/
                            Intent contentDeliveryIntent = new Intent(mContext, ContentDeliveryActivity.class);
                            contentDeliveryIntent.putExtra("courseId", myCoursesList.get(position).getCourse_id());
                            mContext.startActivity(contentDeliveryIntent);
                        } else {
                            Toast.makeText(mContext, "Course expired! Please contact administrator for further assistance.", Toast.LENGTH_SHORT).show();
                        }
                    } else
                        Toast.makeText(mContext, "Course Suspended! Please contact administrator for further assistance.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Please check your internet connection!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateBundleUI(MycoursesAdapter.CoursesViewHolder holder, int position) {
        holder.courseName.setText(Html.fromHtml(myCoursesList.get(position).getC_title()));
        holder.courseProgress.setVisibility(View.INVISIBLE);
        holder.courseProgressBar.setVisibility(View.INVISIBLE);
        holder.itemView.setEnabled(true);
        bundleLabelUpdate(holder, myCoursesList.get(position));
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(mContext);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        Glide.with(mContext)
                .load(myCoursesList.get(position).getC_image())
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.courseThumbnail);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtil.isConnected(mContext)) {
                    if (myCoursesList.get(position).getBs_approved().equals("1")) {
                        if (!myCoursesList.get(position).isExpired()) {
                            if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return;
                            Intent i = new Intent(mContext, BundleSubjectActivity.class);
                            i.putExtra(Constants.COURSE_ID, myCoursesList.get(position).getBundle_id());
                            mContext.startActivity(i);
                            /*Intent contentDeliveryIntent = new Intent(mContext, ContentDeliveryActivity.class);
                            contentDeliveryIntent.putExtra("courseId", myCoursesList.get(position).getCourse_id());
                            mContext.startActivity(contentDeliveryIntent);*/
                        } else {
                            Toast.makeText(mContext, "Course expired! Please contact administrator for further assistance.", Toast.LENGTH_SHORT).show();
                        }
                    } else
                        Toast.makeText(mContext, "Course Suspended! Please contact administrator for further assistance.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Please check your internet connection!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void courseLabelUpdate(CoursesViewHolder holder, MyCoursesResponseModel.DataBean dataBean) {
        if (dataBean.getCs_approved().equals("1")) {
            if (!dataBean.isExpired()) {
                if (Integer.parseInt(dataBean.getExpire_in()) > 5) {
                    if (dataBean.getCs_percentage() == 0)
                        holder.courseStatus.setText(mContext.getString(R.string.course_status_not_started));
                    else if (dataBean.getCs_percentage() == 100)
                        holder.courseStatus.setText(mContext.getString(R.string.course_status_completed));
                    else
                        holder.courseStatus.setText(mContext.getString(R.string.course_status_in_progress));
                } else {
                    if (dataBean.getExpire_in_days() > 2)
                        holder.courseStatus.setText(Html.fromHtml("<font color=#E86100>" + mContext.getString(R.string.course_will_expire) + " " + dataBean.getExpire_in_days() + " Days" + "</font>"));
                    else if (dataBean.getExpire_in_days() == 2)
                        holder.courseStatus.setText(Html.fromHtml("<font color=#FF8C00>" + mContext.getString(R.string.course_will_expire_tomorrow) + "</font>"));
                    else if (dataBean.getExpire_in_days() == 1)
                        holder.courseStatus.setText(Html.fromHtml("<font color=#FF8C00>" + mContext.getString(R.string.course_will_expire_today) + "</font>"));
                }
            } else {
                holder.courseStatus.setText(Html.fromHtml("<font color=#B22222>" + mContext.getString(R.string.course_expired) + "</font>"));
            }
        } else
            holder.courseStatus.setText(Html.fromHtml("<font color=#B22222>" + mContext.getString(R.string.course_suspended) + "</font>"));
    }

    private void bundleLabelUpdate(CoursesViewHolder holder, MyCoursesResponseModel.DataBean dataBean) {
        if (dataBean.getBs_approved().equals("1")) {
            if (!dataBean.isExpired()) {
                if (Integer.parseInt(dataBean.getExpire_in()) > 5) {
                    if (dataBean.getCs_percentage() == 0)
                        holder.courseStatus.setText(mContext.getString(R.string.course_status_not_started));
                    else if (dataBean.getCs_percentage() == 100)
                        holder.courseStatus.setText(mContext.getString(R.string.course_status_completed));
                    else
                        holder.courseStatus.setText(mContext.getString(R.string.course_status_in_progress));
                } else {
                    if (dataBean.getExpire_in_days() > 2)
                        holder.courseStatus.setText(Html.fromHtml("<font color=#E86100>" + mContext.getString(R.string.course_will_expire) + " " + dataBean.getExpire_in_days() + " Days" + "</font>"));
                    else if (dataBean.getExpire_in_days() == 2)
                        holder.courseStatus.setText(Html.fromHtml("<font color=#FF8C00>" + mContext.getString(R.string.course_will_expire_tomorrow) + "</font>"));
                    else if (dataBean.getExpire_in_days() == 1)
                        holder.courseStatus.setText(Html.fromHtml("<font color=#FF8C00>" + mContext.getString(R.string.course_will_expire_today) + "</font>"));
                }
            } else {
                holder.courseStatus.setText(Html.fromHtml("<font color=#B22222>" + mContext.getString(R.string.course_expired) + "</font>"));
            }
        } else
            holder.courseStatus.setText(Html.fromHtml("<font color=#B22222>" + mContext.getString(R.string.course_suspended) + "</font>"));
    }

    @Override
    public int getItemCount() {
        return myCoursesList.size();
    }

    public static class CoursesViewHolder extends RecyclerView.ViewHolder {

        OpenSansTextView courseName, courseProgress, courseStatus;
        ImageView courseThumbnail;
        ProgressBar courseProgressBar;

        public CoursesViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.course_name_tv);
            courseProgress = itemView.findViewById(R.id.progress_percentile_tv);
            courseStatus = itemView.findViewById(R.id.course_status_tv);
            courseThumbnail = itemView.findViewById(R.id.course_thumbnail);
            courseProgressBar = itemView.findViewById(R.id.course_status_pb);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (myCoursesList.get(position).getItem_type().equals("course"))
            return VIEW_TYPE_COURSE;
        else if (myCoursesList.get(position).getItem_type().equals("bundle"))
            return VIEW_TYPE_BUNDLE;
        else
            return VIEW_TYPE_COURSE;
    }
}
