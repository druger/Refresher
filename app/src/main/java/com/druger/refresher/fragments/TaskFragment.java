package com.druger.refresher.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.druger.refresher.MainActivity;
import com.druger.refresher.adapter.TaskAdapter;
import com.druger.refresher.model.ModelTask;

/**
 * Created by druger on 19.09.2015.
 */
public abstract class TaskFragment extends Fragment {

    protected RecyclerView recyclerView;
    protected RecyclerView.LayoutManager layoutManager;

    protected TaskAdapter tasksAdapter;

    public MainActivity activity;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null){
            activity = (MainActivity) getActivity();
        }

        addTaskFromDB();
    }

    public void addTask(ModelTask newTask, boolean saveToDB){
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

        if (saveToDB){
            activity.dbHelper.saveTask(newTask);
        }
    }

    public abstract void addTaskFromDB();

    public abstract void moveTask(ModelTask task);
}
