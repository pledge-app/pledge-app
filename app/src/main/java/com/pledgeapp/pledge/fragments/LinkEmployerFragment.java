package com.pledgeapp.pledge.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pledgeapp.pledge.R;

public class LinkEmployerFragment extends Fragment {
    public static LinkEmployerFragment newInstance() {
        
        Bundle args = new Bundle();
        
        LinkEmployerFragment fragment = new LinkEmployerFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_link_employer, container, false);
    }
}
