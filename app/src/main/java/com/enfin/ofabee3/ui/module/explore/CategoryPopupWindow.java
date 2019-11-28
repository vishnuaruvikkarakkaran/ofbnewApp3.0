package com.enfin.ofabee3.ui.module.explore;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.remote.model.coursecategory.response.CourseCategoryResponse;
import com.enfin.ofabee3.ui.base.baserecyclerview.OnRecyclerViewClickListener;
import com.enfin.ofabee3.ui.module.home.popularcourses.PopularCourse;
import com.enfin.ofabee3.utils.Constants;
import com.enfin.ofabee3.utils.OBLogger;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

public class CategoryPopupWindow implements OnRecyclerViewClickListener {

    private View popupView;
    private RecyclerView courseCategoryRecyclerView;
    public ExploreCoursesCategoryAdapter adapterCategory;
    private Context mContext;
    private TextView applyBtn, resetButton;
    private RelativeLayout closeBtn;
    private LinearLayoutManager layoutManager;
    private List<CourseCategoryResponse.DataBean> categoryList = new ArrayList<>();
    private List<CourseCategoryResponse.DataBean> categoryListFiltered = new ArrayList<>();
    public ArrayList<String> categoryIDList = new ArrayList<>();
    private String categoryIDString;

    private static CategoryPopupWindow popupWindow = null;

    public CategoryPopupWindow(Context context, List<CourseCategoryResponse.DataBean> mCoursesCategoryList) {
        this.mContext = context;
        this.categoryList = mCoursesCategoryList;
        filterList();
        OBLogger.e("STATE INVOKED");

        //resetUnsavedStatus();
        //updateCategoryID();
    }

    private void resetUnsavedStatus() {
        categoryIDList.clear();
        for (int i = 0; i < categoryListFiltered.size(); i++) {
            categoryListFiltered.get(i).setSelected(false);
        }
        adapterCategory.setItems(categoryListFiltered);
        adapterCategory.notifyDataSetChanged();
    }

    public CategoryPopupWindow getInstance(Context context, List<CourseCategoryResponse.DataBean> mCoursesCategoryList) {
        if (popupWindow == null)
            return new CategoryPopupWindow(context, mCoursesCategoryList);
        else
            return new CategoryPopupWindow(context, mCoursesCategoryList);
    }

    private void updateCategoryID() {
        for (int i = 0; i < categoryListFiltered.size(); i++) {
            if (categoryListFiltered.get(i).isSelected())
                if (!categoryIDList.contains(categoryListFiltered.get(i).getId())) {
                    categoryIDList.add(categoryListFiltered.get(i).getId());
                } else
                    categoryIDList.remove(categoryListFiltered.get(i).getId());
        }
    }

    public void setListener() {

    }

    //PopupWindow display method
    public void showPopupWindow(final View view) {

        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.category_popup, null);
        courseCategoryRecyclerView = popupView.findViewById(R.id.category_courses_rv);
        applyBtn = popupView.findViewById(R.id.apply_button);
        closeBtn = popupView.findViewById(R.id.close_btn);
        resetButton = popupView.findViewById(R.id.reset_button);
        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler

        TextView test2 = popupView.findViewById(R.id.titleText);

        initCoursesCategoryList();
        //Handler for clicking on the inactive zone of the window

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (categoryIDList.size() > 0) {
                    categoryIDString = "";
                    for (int i = 0; i < categoryIDList.size(); i++) {
                        if (!TextUtils.isEmpty(categoryIDList.get(i))) {
                            if (i == 0)
                                categoryIDString = categoryIDList.get(i);
                            else
                                categoryIDString = categoryIDString + "," + categoryIDList.get(i);
                        }
                    }
                    Prefs.putString(Constants.CATEGORY_IDS, categoryIDString);
                    ExploreFragment.mPresenter.getCourses(mContext, categoryIDString, 0);
                } else {
                    Prefs.putString(Constants.CATEGORY_IDS, "");
                    ExploreFragment.mPresenter.getCourses(mContext, "", 0);
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResetButtonActive(false);
                categoryIDList.clear();
                for (int i = 0; i < categoryListFiltered.size(); i++) {
                    categoryListFiltered.get(i).setSelected(false);
                }
                adapterCategory.setItems(categoryListFiltered);
                adapterCategory.notifyDataSetChanged();
                //Prefs.putString(Constants.CATEGORY_IDS, "");
                //ExploreFragment.mPresenter.getCourses(mContext, "", 0);
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                categoryIDList.clear();
                for (int i = 0; i < categoryListFiltered.size(); i++) {
                    categoryListFiltered.get(i).setSelected(false);
                }
                adapterCategory.setItems(categoryListFiltered);
                adapterCategory.notifyDataSetChanged();
            }
        });

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Close the window when clicked
                //popupWindow.dismiss();
                return true;
            }
        });

    }

    private void initCoursesCategoryList() {
        categoryIDString = Prefs.getString(Constants.CATEGORY_IDS, "");
        StringToList(categoryIDString);
        if (categoryIDList.size() > 0)
            setResetButtonActive(true);
        else
            setResetButtonActive(false);
        adapterCategory = new ExploreCoursesCategoryAdapter(mContext, categoryListFiltered, categoryIDList);
        layoutManager = new LinearLayoutManager(mContext);
        courseCategoryRecyclerView.setAdapter(adapterCategory);
        courseCategoryRecyclerView.setLayoutManager(layoutManager);
        adapterCategory.setItems(categoryListFiltered);
        adapterCategory.setListener(this::onItemClicked);
        adapterCategory.notifyDataSetChanged();
    }

    private void StringToList(String categoryIDString) {
        String[] list = categoryIDString.split(",");
        for (int i = 0; i < list.length; i++) {
            if (!categoryIDList.contains(list[i])) {
                categoryIDList.add(list[i]);
            } else
                categoryIDList.remove(list[i]);
        }
    }

    private void filterList() {
        categoryListFiltered.clear();
        for (CourseCategoryResponse.DataBean category : categoryList) {
            if (category.getCt_status().equalsIgnoreCase("1"))
                categoryListFiltered.add(category);
        }
    }

    @Override
    public void onItemClicked(Object item) {
        if (item instanceof CourseCategoryResponse.DataBean) {
            CourseCategoryResponse.DataBean data = (CourseCategoryResponse.DataBean) item;

            /* if (categoryListFiltered.get(index).isSelected())
                if (!categoryIDList.contains(categoryListFiltered.get(index).getId())) {
                    categoryIDList.add(categoryListFiltered.get(index).getId());
                } else
                    categoryIDList.remove(categoryListFiltered.get(index).getId());*/

            if (!categoryIDList.contains(data.getId())) {
                categoryIDList.add(data.getId());

            } else {
                categoryIDList.remove(data.getId());
            }

            /*int index = categoryListFiltered.indexOf(data);
            if (index > -1)
                categoryListFiltered.set(index, data);*/


            if (categoryIDList.size() > 0)
                setResetButtonActive(true);
            else
                setResetButtonActive(false);
        }
    }

    public String getCategoryData() {
        categoryIDString = Prefs.getString(Constants.CATEGORY_IDS, "");
        return categoryIDString;
    }

    public void setResetButtonActive(boolean isActive) {
        if (isActive)
            resetButton.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlue));
        else
            resetButton.setTextColor(ContextCompat.getColor(mContext, R.color.colorGrey));
    }
}
