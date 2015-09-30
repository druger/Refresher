package com.druger.refresher.fragments;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.druger.refresher.MainActivity;
import com.druger.refresher.R;
import com.druger.refresher.adapter.TaskAdapter;
import com.druger.refresher.alarm.AlarmHelper;
import com.druger.refresher.dialog.EditTaskDialogFragment;
import com.druger.refresher.model.Item;
import com.druger.refresher.model.ModelTask;

/**
 * Created by druger on 19.09.2015.
 */
public abstract class TaskFragment extends Fragment {

    protected RecyclerView recyclerView;
    protected RecyclerView.LayoutManager layoutManager;

    protected TaskAdapter tasksAdapter;

    public MainActivity activity;

    public AlarmHelper alarmHelper;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null){
            activity = (MainActivity) getActivity();
        }

        alarmHelper = AlarmHelper.getInstance();

        addTaskFromDB();
    }

    public abstract void addTask(ModelTask newTask, boolean saveToDB);

    public void updateTask(ModelTask task) {
        tasksAdapter.updateTask(task);
    }

    public void removeTaskDialog(final int location){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        dialogBuilder.setMessage(R.string.dialog_removing_message);

        Item item = tasksAdapter.getItem(location);

        if (item.isTask()){
            ModelTask removingTask = (ModelTask) item;

            final long timeStamp = removingTask.getTimeStamp();
            final boolean[] isRemoved = {false};

            dialogBuilder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    tasksAdapter.removeItem(location);
                    isRemoved[0] = true;
                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.coordinator),
                            R.string.removed, Snackbar.LENGTH_LONG);
                    snackbar.setAction(R.string.dialog_cancel, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addTask(activity.dbHelper.query().getTask(timeStamp), false);
                            isRemoved[0] = false;
                        }
                    });
                    snackbar.getView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                        @Override
                        public void onViewAttachedToWindow(View v) {

                        }

                        @Override
                        public void onViewDetachedFromWindow(View v) {
                            if (isRemoved[0]){
                                alarmHelper.removeAlarm(timeStamp);
                                activity.dbHelper.removeTask(timeStamp);
                            }
                        }
                    });

                    snackbar.show();
                    dialog.dismiss();
                }
            });

            dialogBuilder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }

        dialogBuilder.show();
    }

    public void showEditTaskDialog(ModelTask task) {
        DialogFragment editingTaskDialog = EditTaskDialogFragment.newInstance(task);
        editingTaskDialog.show(getActivity().getFragmentManager(), "EditTaskDialogFragment");
    }

    public abstract void findTasks(String title);

    public abstract void addTaskFromDB();

    public abstract void moveTask(ModelTask task);
}
