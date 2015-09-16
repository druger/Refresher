package com.druger.refresher.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.druger.refresher.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DoneTaskFragment extends Fragment {

    private RecyclerView rvDoneTask;
    private RecyclerView.LayoutManager layoutManager;


    public DoneTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_done_task, container, false);

        rvDoneTask = (RecyclerView) rootView.findViewById(R.id.rvDoneTasks);
        layoutManager = new LinearLayoutManager(getActivity());

        rvDoneTask.setLayoutManager(layoutManager);

        // Inflate the layout for this fragment
        return rootView;
    }


}
