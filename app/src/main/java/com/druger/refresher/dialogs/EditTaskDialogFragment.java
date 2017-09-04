package com.druger.refresher.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.druger.refresher.App;
import com.druger.refresher.R;
import com.druger.refresher.alarms.AlarmHelper;
import com.druger.refresher.models.ModelTask;
import com.druger.refresher.utils.DateHelper;

import java.util.Calendar;

import javax.inject.Inject;

/**
 * Created by druger on 30.09.2015.
 */
public class EditTaskDialogFragment extends DialogFragment {

    @Inject
    AlarmHelper alarmHelper;

    public static EditTaskDialogFragment newInstance(ModelTask task) {
        EditTaskDialogFragment editTaskDialogFragment = new EditTaskDialogFragment();

        Bundle args = new Bundle();
        args.putString("title", task.getTitle());
        args.putLong("date", task.getDate());
        args.putInt("priority", task.getPriority());
        args.putLong("time_stamp", task.getTimeStamp());

        editTaskDialogFragment.setArguments(args);
        return editTaskDialogFragment;
    }

    private EditingTaskListener editingTaskListener;

    public interface EditingTaskListener {
        void onTaskEdited(ModelTask updatedTask);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            editingTaskListener = (EditingTaskListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " must implement EditingTaskListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = new Bundle();
        String title = args.getString("title");
        long date = args.getLong("date", 0);
        int priority = args.getInt("priority", 0);
        long timeStamp = args.getLong("time_stamp", 0);

        final ModelTask task = new ModelTask(title, date, priority, 0, timeStamp);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.dialog_editing_title);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View container = inflater.inflate(R.layout.dialog_task, null);

        final TextInputLayout taskTitle = container.findViewById(R.id.dialogTaskTitle);
        final EditText etTitle = taskTitle.getEditText();

        final TextInputLayout taskDate = container.findViewById(R.id.dialogTaskDate);
        final EditText etDate = taskDate.getEditText();

        final TextInputLayout taskTime = container.findViewById(R.id.dialogTaskTime);
        final EditText etTime = taskTime.getEditText();

        Spinner spPriority = container.findViewById(R.id.spDialogTaskPriority);

        etTitle.setText(task.getTitle());
        etTitle.setSelection(etTitle.length());
        if (task.getDate() != 0){
            etDate.setText(DateHelper.getDate(task.getDate()));
            etTime.setText(DateHelper.getTime(task.getDate()));
        }

        taskTitle.setHint(getResources().getString(R.string.task_title));
        taskDate.setHint(getResources().getString(R.string.task_date));
        taskTime.setHint(getResources().getString(R.string.task_time));

        builder.setView(container);

        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, /*ModelTask.PRIORITY_LEVELS*/
                getResources().getStringArray(R.array.priority_levels));

        spPriority.setAdapter(priorityAdapter);

        spPriority.setSelection(task.getPriority());

        spPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                task.setPriority(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 1);
        if (etDate.length() != 0 || etTime.length() != 0){
            calendar.setTimeInMillis(task.getDate());
        }

        etDate.setOnClickListener(v -> {
            if (etDate.length() == 0) {
                etDate.setText(" ");
            }

            PickerDialogs.DatePickerFragment datePickerFragment
                    = new PickerDialogs.DatePickerFragment();
            datePickerFragment.setEtDate(etDate);
            datePickerFragment.show(getFragmentManager(), "DatePickerFragment");
        });

        if (etTime != null) {
            etTime.setOnClickListener(v -> {
                if (etTime.length() == 0) {
                    etTime.setText(" ");
                }
                PickerDialogs.TimePickerFragment timePickerDialog
                        = new PickerDialogs.TimePickerFragment();
                timePickerDialog.setEtTime(etTime);
                timePickerDialog.show(getFragmentManager(), "TimePickerFragment");
            });
        }

        builder.setPositiveButton(R.string.dialog_ok, (dialog, which) -> {
            task.setTitle(etTitle.getText().toString());
            if (etDate.length() != 0 || etDate.length() != 0) {
                task.setDate(calendar.getTimeInMillis());

                alarmHelper.setAlarm(task);
            }

            task.setStatus(ModelTask.STATUS_CURRENT);
            editingTaskListener.onTaskEdited(task);
            dialog.dismiss();
        });

        builder.setNegativeButton(R.string.dialog_cancel, (dialog, which) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(dialog -> {
            final Button positiveButton = ((AlertDialog) dialog).
                    getButton(DialogInterface.BUTTON_POSITIVE);
            if (etTitle.length() == 0) {
                positiveButton.setEnabled(false);
                taskTitle.setError(getResources().
                        getString(R.string.dialog_error_empty_title));
            }

            etTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 0) {
                        positiveButton.setEnabled(false);
                        taskTitle.setError(getResources().
                                getString(R.string.dialog_error_empty_title));
                    } else {
                        positiveButton.setEnabled(true);
                        taskTitle.setErrorEnabled(false);
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        });
        return alertDialog;
    }
}
