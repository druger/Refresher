package com.druger.refresher.fragments;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.druger.refresher.App;
import com.druger.refresher.R;
import com.druger.refresher.activities.MainActivity;
import com.druger.refresher.adapters.TaskAdapter;
import com.druger.refresher.alarms.AlarmHelper;
import com.druger.refresher.dialogs.EditTaskDialogFragment;
import com.druger.refresher.models.Item;
import com.druger.refresher.models.ModelTask;

import javax.inject.Inject;

/**
 * Created by druger on 19.09.2015.
 */
public abstract class TaskFragment extends Fragment {

    protected RecyclerView recyclerView;
    protected RecyclerView.LayoutManager layoutManager;

    protected TaskAdapter tasksAdapter;

    public MainActivity activity;

    @Inject
    AlarmHelper alarmHelper;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null){
            activity = (MainActivity) getActivity();
        }
        addTaskFromDB();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
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

            dialogBuilder.setPositiveButton(R.string.dialog_ok, (dialog, which) -> {

                tasksAdapter.removeItem(location);
                isRemoved[0] = true;
                showSnackbar(timeStamp, isRemoved);
                dialog.dismiss();
            });

            dialogBuilder.setNegativeButton(R.string.dialog_cancel, (dialog, which) -> dialog.cancel());
        }

        dialogBuilder.show();
    }

    private void showSnackbar(final long timeStamp, final boolean[] isRemoved) {
        Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.coordinator),
                R.string.removed, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.dialog_cancel, v -> {
            addTask(activity.realmHelper.getTaskByTimestamp(timeStamp), false);
            isRemoved[0] = false;
        });
        snackbar.getView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                if (isRemoved[0]){
                    alarmHelper.removeAlarm(timeStamp);
                    activity.realmHelper.removeTaskByTimestamp(timeStamp);
                }
            }
        });

        snackbar.show();
    }

    public void showEditTaskDialog(ModelTask task) {
        DialogFragment editingTaskDialog = EditTaskDialogFragment.newInstance(task);
        editingTaskDialog.show(getActivity().getFragmentManager(), "EditTaskDialogFragment");
    }

    public abstract void findTasks(String title);

    public abstract void checkAdapter();

    public abstract void addTaskFromDB();

    public abstract void moveTask(ModelTask task);
}
