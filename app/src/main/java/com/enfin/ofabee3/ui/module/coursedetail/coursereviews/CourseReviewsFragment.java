package com.enfin.ofabee3.ui.module.coursedetail.coursereviews;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enfin.ofabee3.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseReviewsFragment extends Fragment {


    public CourseReviewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course_reviews, container, false);
    }

}
