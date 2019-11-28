package com.enfin.ofabee3.ui.module.home.guesthome;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enfin.ofabee3.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GuestHomeFragment extends Fragment {


    public GuestHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guest_home, container, false);
    }

}
