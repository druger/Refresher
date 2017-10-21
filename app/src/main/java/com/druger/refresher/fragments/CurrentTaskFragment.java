package com.druger.refresher.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.druger.refresher.R;
import com.druger.refresher.adapters.CurrentTasksAdapter;
import com.druger.refresher.models.ModelSeparator;
import com.druger.refresher.models.ModelTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentTaskFragment extends TaskFragment {

    public CurrentTaskFragment() {
        // Required empty public constructor
    }

    OnTaskDoneListener onTaskDoneListener;

    public interface OnTaskDoneListener {
        void onTaskDone(ModelTask task);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onTaskDoneListener = activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTaskDoneListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_current_task, container, false);
        setupRecycler(rootView);
        return rootView;
    }

    private void setupRecycler(View rootView) {
        recyclerView = rootView.findViewById(R.id.rvCurrentTasks);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        tasksAdapter = new CurrentTasksAdapter(this);
        recyclerView.setAdapter(tasksAdapter);
    }

    @Override
    public void addTask(ModelTask newTask, boolean saveToDB) {
        int position = -1;
        ModelSeparator separator = null;

        for (int i = 0; i < tasksAdapter.getItemCount(); i++) {
            if (tasksAdapter.getItem(i).isTask()) {
                ModelTask task = (ModelTask) tasksAdapter.getItem(i);
                if (newTask.getDate() < task.getDate()) {
                    position = i;
                    break;
                }
            }
        }

        if (newTask.getDate() != 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(newTask.getDate());

            if (calendar.get(Calendar.DAY_OF_YEAR) < Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                newTask.setDateStatus(ModelSeparator.TYPE_OVERDUE);
                if (!tasksAdapter.isContainsSeparatorOverdue()) {
                    tasksAdapter.setContainsSeparatorOverdue(true);
                    separator = new ModelSeparator(ModelSeparator.TYPE_OVERDUE);
                }
            } else if (calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                newTask.setDateStatus(ModelSeparator.TYPE_TODAY);
                if (!tasksAdapter.isContainsSeparatorToday()) {
                    tasksAdapter.setContainsSeparatorToday(true);
                    separator = new ModelSeparator(ModelSeparator.TYPE_TODAY);
                }
            } else if (calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 1) {
                newTask.setDateStatus(ModelSeparator.TYPE_TOMORROW);
                if (!tasksAdapter.isContainsSeparatorTomorrow()) {
                    tasksAdapter.setContainsSeparatorTomorrow(true);
                    separator = new ModelSeparator(ModelSeparator.TYPE_TOMORROW);
                }
            } else if (calendar.get(Calendar.DAY_OF_YEAR) > Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 1) {
                newTask.setDateStatus(ModelSeparator.TYPE_FUTURE);
                if (!tasksAdapter.isContainsSeparatorFuture()) {
                    tasksAdapter.setContainsSeparatorFuture(true);
                    separator = new ModelSeparator(ModelSeparator.TYPE_FUTURE);
                }
            }
        }

        if (position != -1) {
            if (!tasksAdapter.getItem(position - 1).isTask()){
                if (position - 2 >= 0 && tasksAdapter.getItem(position -2).isTask()){
                    ModelTask task = (ModelTask) tasksAdapter.getItem(position - 2);
                    if (task.getDateStatus() == newTask.getDateStatus()){
                        position -= 1;
                    }
                } else if (position - 2 < 0 && newTask.getDate() == 0){
                    position -= 1;
                }
            }

            if (separator != null) {
                tasksAdapter.addItem(position - 1, separator);
            }

            tasksAdapter.addItem(position, newTask);
        } else {
            if (separator != null){
                tasksAdapter.addItem(separator);
            }
            tasksAdapter.addItem(newTask);
        }

        if (saveToDB) {
            activity.dbHelper.saveTask(newTask);
        }
    }

    @Override
    public void findTasks(String title) {
        tasksAdapter.removeAllItems();
        List<ModelTask> tasks = new ArrayList<>();
        tasks.addAll(activity.dbHelper.getTasksByTitleAndAnyStatus(title));
        for (int i = 0; i < tasks.size(); i++) {
            addTask(tasks.get(i), false);
        }
    }

    @Override
    public void checkAdapter() {
        if (tasksAdapter == null){
            tasksAdapter = new CurrentTasksAdapter(this);
            addTaskFromDB();
        }
    }

    @Override
    public void addTaskFromDB() {
        tasksAdapter.removeAllItems();
        List<ModelTask> tasks = new ArrayList<>();
        tasks.addAll(activity.dbHelper.getTasksByAnyStatus());
        for (int i = 0; i < tasks.size(); i++) {
            addTask(tasks.get(i), false);
        }
    }

    @Override
    public void moveTask(ModelTask task) {
        alarmHelper.removeAlarm(task.getTimeStamp());
        if (onTaskDoneListener != null) {
            onTaskDoneListener.onTaskDone(task);
        }
    }
}
