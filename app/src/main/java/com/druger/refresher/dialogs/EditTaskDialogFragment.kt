package com.druger.refresher.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.design.widget.TextInputLayout
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import com.druger.refresher.App
import com.druger.refresher.R
import com.druger.refresher.alarms.AlarmHelper
import com.druger.refresher.models.ModelTask
import com.druger.refresher.utils.DateHelper
import java.util.*
import javax.inject.Inject

/**
* Created by druger on 30.09.2015.
*/

// TODO вынести в базовый класс
class EditTaskDialogFragment : DialogFragment() {

    @Inject
    lateinit var alarmHelper: AlarmHelper

    private lateinit var task: ModelTask
    private lateinit var calendar: Calendar

    private lateinit var taskTitle: TextInputLayout
    private lateinit var etTitle: EditText
    private lateinit var taskDate: TextInputLayout
    private lateinit var etDate: EditText
    private lateinit var taskTime: TextInputLayout
    private lateinit var etTime: EditText

    companion object {

        fun newInstance(task: ModelTask): EditTaskDialogFragment {
            val editTaskDialogFragment = EditTaskDialogFragment()

            val args = Bundle()
            args.putString("title", task.title)
            args.putLong("date", task.date)
            args.putInt("priority", task.priority)
            args.putLong("time_stamp", task.timeStamp)

            editTaskDialogFragment.arguments = args
            return editTaskDialogFragment
        }
    }

    private lateinit var editingTaskListener: EditingTaskListener

    interface EditingTaskListener {
        fun onTaskEdited(updatedTask: ModelTask)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            editingTaskListener = context as EditingTaskListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() +
                    " must implement EditingTaskListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        App.getApplicationComponent().inject(this)
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle): Dialog {

        task = getModelTask()

        val inflater = activity.layoutInflater
        val container = inflater.inflate(R.layout.dialog_task, null)

        bindViews(container)
        setTextForEditTexts()

        calendar = setupCalendar()

        val builder = setupBuilder(container)
        setupSpinner(container)
        setupDatePicker(etDate)
        setupTimePicker(etTime)

        return setupAlertDialog(builder)
    }

    private fun setTextForEditTexts() {
        etTitle.setText(task.title)
        etTitle.setSelection(etTitle.length())
        if (task.date.toInt() != 0) {
            etDate.setText(DateHelper.getDate(task.date))
            etTime.setText(DateHelper.getTime(task.date))
        }

        taskTitle.hint = resources.getString(R.string.task_title)
        taskDate.hint = resources.getString(R.string.task_date)
        taskTime.hint = resources.getString(R.string.task_time)
    }

    private fun setupCalendar(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 1)
        if (etDate.length() != 0 || etTime.length() != 0) {
            calendar.timeInMillis = task.date
        }
        return calendar
    }

    private fun bindViews(container: View) {
        taskTitle = container.findViewById(R.id.dialogTaskTitle)
        etTitle = taskTitle.editText!!

        taskDate = container.findViewById(R.id.dialogTaskDate)
        etDate = taskDate.editText!!

        taskTime = container.findViewById(R.id.dialogTaskTime)
        etTime = taskTime.editText!!
    }

    @NonNull
    private fun setupAlertDialog(builder: AlertDialog.Builder): AlertDialog {
        val alertDialog = builder.create()
        alertDialog.setOnShowListener({
            val positiveButton = (dialog as AlertDialog).
                    getButton(DialogInterface.BUTTON_POSITIVE)
            if (etTitle.length() == 0) {
                positiveButton.isEnabled = false
                taskTitle.error = resources.
                        getString(R.string.dialog_error_empty_title)
            }

            etTitle.addTextChangedListener(object : TextWatcher {

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.isEmpty()) {
                        positiveButton.isEnabled = false
                        taskTitle.error = resources.
                                getString(R.string.dialog_error_empty_title)
                    } else {
                        positiveButton.isEnabled = true
                        taskTitle.isErrorEnabled = false
                    }
                }

                override fun afterTextChanged(s: Editable) {

                }
            })
        })
        return alertDialog
    }

    private fun setupBuilder(container: View): AlertDialog.Builder {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.dialog_editing_title)
        builder.setView(container)

        builder.setPositiveButton(R.string.dialog_ok, { dialog, _ ->
            run {
                task.title = etTitle.text.toString()
                if (etDate.length() != 0 || etTime.length() != 0) {
                    task.date = calendar.timeInMillis

                    alarmHelper.setAlarm(task)
                }

                task.status = ModelTask.STATUS_CURRENT
                editingTaskListener.onTaskEdited(task)
                dialog.dismiss()
            }
        })
        builder.setNegativeButton(R.string.dialog_cancel, { dialog, _ -> dialog.cancel() })
        return builder
    }

    private fun setupTimePicker(etTime: EditText) {
        etTime.setOnClickListener({
            if (etTime.length() == 0) {
                etTime.setText(" ")
            }
            val timePickerDialog = PickerDialogs.TimePickerFragment()
            timePickerDialog.setEtTime(etTime)
            timePickerDialog.show(fragmentManager, "TimePickerFragment")
        })
    }

    private fun setupDatePicker(etDate: EditText) {
        etDate.setOnClickListener({
            if (etDate.length() == 0) {
                etDate.setText(" ")
            }
            val datePickerFragment = PickerDialogs.DatePickerFragment()
            datePickerFragment.setEtDate(etDate)
            datePickerFragment.show(fragmentManager, "DatePickerFragment")
        })
    }

    private fun setupSpinner(container: View) {
        val spPriority: Spinner = container.findViewById(R.id.spDialogTaskPriority)

        val priorityAdapter = ArrayAdapter(activity,
                android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.priority_levels))

        spPriority.adapter = priorityAdapter

        spPriority.setSelection(task.priority)

        spPriority.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                task.priority = position
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    @NonNull
    private fun getModelTask(): ModelTask {
        val args = Bundle()
        val title = args.getString("title")
        val date = args.getLong("date", 0)
        val priority = args.getInt("priority", 0)
        val timeStamp = args.getLong("time_stamp", 0)

        return ModelTask(title, date, priority, 0, timeStamp)
    }
}
