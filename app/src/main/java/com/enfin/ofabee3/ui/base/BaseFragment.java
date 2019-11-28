package com.enfin.ofabee3.ui.base;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.ui.module.nointernet.NoInternetActivity;
import com.enfin.ofabee3.utils.FragmentActionListener;
import com.enfin.ofabee3.utils.OBLogger;
import com.enfin.ofabee3.utils.networkmonitor.NetworkObserver;

/**
 * Created by SARATH on 20/8/19.
 */
public class BaseFragment extends Fragment implements NetworkObserver {
    // TODO: add any relevance methods

    public void onSearchQuery(String searchString) {
    }

    public void setFragmentChangeListener(FragmentActionListener mListener) {
        /*//Override if you want to notify the fragment change
        eg:Title update*/
    }

    @Override
    public void connectivityStatusChanged() {
        OBLogger.e("NO INTERNET");
        Intent intentone = new Intent(getActivity(), NoInternetActivity.class);
        intentone.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentone);
        getActivity().overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
    }
}