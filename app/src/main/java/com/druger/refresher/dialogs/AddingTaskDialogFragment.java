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

import com.druger.refresher.R;
import com.druger.refresher.alarms.AlarmHelper;
import com.druger.refresher.models.ModelTask;

import java.util.Calendar;

/**
 * Created by druger on 15.09.2015.
 */
public class AddingTaskDialogFragment extends DialogFragment {

    private AddingTaskListener addingTaskListener;

    public interface AddingTaskListener {
        void onTaskAdded(ModelTask newTask);

        void onTaskAddingCancel();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            addingTaskListener = (AddingTaskListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " must implement AddingTaskListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.dialog_title);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View container = inflater.inflate(R.layout.dialog_task, null);

        final TextInputLayout taskTitle = container.findViewById(R.id.dialogTaskTitle);
        final EditText etTitle = taskTitle.getEditText();

        final TextInputLayout taskDate = container.findViewById(R.id.dialogTaskDate);
        final EditText etDate = taskDate.getEditText();

        final TextInputLayout taskTime = container.findViewById(R.id.dialogTaskTime);
        final EditText etTime = taskTime.getEditText();

        Spinner spPriority = container.findViewById(R.id.spDialogTaskPriority);

        taskTitle.setHint(getResources().getString(R.string.task_title));
        taskDate.setHint(getResources().getString(R.string.task_date));
        taskTime.setHint(getResources().getString(R.string.task_time));

        builder.setView(container);

        final ModelTask task = new ModelTask();

        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, /*ModelTask.PRIORITY_LEVELS*/
                getResources().getStringArray(R.array.priority_levels));

        spPriority.setAdapter(priorityAdapter);

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

        if (etDate != null) {
            etDate.setOnClickListener(v -> {
                if (etDate.length() == 0) {
                    etDate.setText(" ");
                }
                PickerDialogs.DatePickerFragment datePickerFragment = new PickerDialogs.DatePickerFragment();
                datePickerFragment.setEtDate(etDate);
                datePickerFragment.show(getFragmentManager(), "DatePickerFragment");
            });
        }

        if (etTime != null) {
            etTime.setOnClickListener(v -> {
                if (etTime.length() == 0) {
                    etTime.setText(" ");
                }
                PickerDialogs.TimePickerFragment timePickerDialog = new PickerDialogs.TimePickerFragment();
                timePickerDialog.setEtTime(etTime);
                timePickerDialog.show(getFragmentManager(), "TimePickerFragment");
            });
        }

        builder.setPositiveButton(R.string.dialog_ok, (dialog, which) -> {
            task.setTitle(etTitle.getText().toString());
            if (etDate.length() != 0 || etDate.length() != 0) {
                task.setDate(calendar.getTimeInMillis());

                AlarmHelper alarmHelper = AlarmHelper.getInstance();
                alarmHelper.setAlarm(task);
            }

            task.setStatus(ModelTask.STATUS_CURRENT);
            addingTaskListener.onTaskAdded(task);
            dialog.dismiss();
        });

        builder.setNegativeButton(R.string.dialog_cancel, (dialog, which) -> {
            addingTaskListener.onTaskAddingCancel();
            dialog.cancel();
        });

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
