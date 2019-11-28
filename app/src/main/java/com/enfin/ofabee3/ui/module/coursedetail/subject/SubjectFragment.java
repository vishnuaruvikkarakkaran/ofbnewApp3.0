package com.enfin.ofabee3.ui.module.coursedetail.subject;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.remote.model.coursedetail.response.BundleDetailResponseModel;
import com.enfin.ofabee3.data.remote.model.coursedetail.response.CourseDetailResponseModel;
import com.enfin.ofabee3.ui.module.contentdelivery.ContentDeliveryActivity;
import com.enfin.ofabee3.ui.module.coursedetail.CourseCurriculamAdapter;
import com.enfin.ofabee3.ui.module.coursedetail.TabsHeaderActivity;
import com.enfin.ofabee3.ui.module.coursedetail.bundledetails.BundleDetailActivity;
import com.enfin.ofabee3.ui.module.explore.ExploreCourse;
import com.enfin.ofabee3.utils.Constants;
import com.enfin.ofabee3.utils.NetworkUtil;
import com.enfin.ofabee3.utils.OBLogger;
import com.enfin.ofabee3.utils.OnSubjectItemClickListener;
import com.enfin.ofabee3.utils.RecyclerViewItemClickListener;
import com.enfin.ofabee3.utils.SimpleDividerItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubjectFragment extends Fragment implements OnSubjectItemClickListener {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private Dialog progressDialog;
    private BundleDetailResponseModel bundleDetailResponseModel;
    private String source;
    private RelativeLayout swipeContainer;

    public SubjectFragment() {
        // Required empty public constructor
    }

    public SubjectFragment(BundleDetailResponseModel data, String source) {
        this.bundleDetailResponseModel = data;
        this.source = source;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_subject, container, false);
        recyclerView = rootview.findViewById(R.id.subject_rv);
        swipeContainer = rootview.findViewById(R.id.swipe_container);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        SubjectAdapter adapter = new SubjectAdapter(getActivity(), bundleDetailResponseModel, this);
        recyclerView.setAdapter(adapter);
        updateBottomView();
        return rootview;
    }

    private void updateBottomView() {
        if (source.equals(Constants.NOT_PURCHASED)) {
            RelativeLayout.LayoutParams buttonLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            buttonLayoutParams.setMargins(5, 5, 5, 150);
            swipeContainer.setLayoutParams(buttonLayoutParams);
        }
        else if (source.equals(Constants.PURCHASED)) {
            RelativeLayout.LayoutParams buttonLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            buttonLayoutParams.setMargins(5, 5, 5, 5);
            swipeContainer.setLayoutParams(buttonLayoutParams);
        }
    }

    @Override
    public void onItemSelected(int position, String selectedID, String subject) {
        if (NetworkUtil.isConnected(getActivity())) {
            //onShowProgress();
            if (source.equals(Constants.NOT_PURCHASED)) {
                Intent i = new Intent(getActivity(), BundleDetailActivity.class);
                i.putExtra(Constants.COURSE_ID, selectedID);
                i.putExtra(Constants.TYPE, "course");
                i.putExtra(Constants.SUBJECT_NAME, subject);
                getActivity().startActivity(i);
            }
            if (source.equals(Constants.PURCHASED)) {
                Intent contentDeliveryIntent = new Intent(getActivity(), ContentDeliveryActivity.class);
                contentDeliveryIntent.putExtra(Constants.COURSE_ID, selectedID);
                getActivity().startActivity(contentDeliveryIntent);
            }
        } else {
            Toast.makeText(getActivity(), "Please check your internet connection!!", Toast.LENGTH_SHORT).show();
        }
    }
}
