package com.druger.refresher.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.druger.refresher.R;
import com.druger.refresher.adapter.CurrentTasksAdapter;
import com.druger.refresher.model.ModelTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentTaskFragment extends Fragment {

    private RecyclerView rvCurrentTask;
    private RecyclerView.LayoutManager layoutManager;

    private CurrentTasksAdapter tasksAdapter;

    public CurrentTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_current_task, container, false);

        rvCurrentTask = (RecyclerView) rootView.findViewById(R.id.rvCurrentTasks);
        layoutManager = new LinearLayoutManager(getActivity());

        rvCurrentTask.setLayoutManager(layoutManager);

        tasksAdapter = new CurrentTasksAdapter();
        rvCurrentTask.setAdapter(tasksAdapter);

        // Inflate the layout for this fragment
        return rootView;
    }

    public void addTask(ModelTask newTask){
        int position = -1;

        for (int i = 0; i < tasksAdapter.getItemCount(); i++) {
            if (tasksAdapter.getItem(i).isTask()){
                ModelTask task = (ModelTask) tasksAdapter.getItem(i);
                if (newTask.getDate() < task.getDate()){
                    position = i;
                    break;
                }
            }
        }

        if (position != -1){
            tasksAdapter.addItem(position, newTask);
        } else {
            tasksAdapter.addItem(newTask);
        }
    }


}
