package com.druger.refresher.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.DialogFragment
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import com.druger.refresher.utils.DateHelper
import java.util.*

/**
* Created by druger on 26.08.2017.
*/
class PickerDialogs {

    class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

        private lateinit var etDate: EditText

        override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            return DatePickerDialog(activity, this, year, month, day)
        }

        override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            etDate.setText(DateHelper.getDate(calendar.timeInMillis))
        }

        override fun onCancel(dialog: DialogInterface) {
            etDate.text = null
        }

        fun setEtDate(etDate: EditText) {
            this.etDate = etDate
        }
    }

    class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

        private lateinit var etTime: EditText

        override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            return TimePickerDialog(activity, this, hour, minute,
                    DateFormat.is24HourFormat(activity))
        }

        override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            etTime.setText(DateHelper.getTime(calendar.timeInMillis))
        }

        override fun onCancel(dialog: DialogInterface) {
            etTime.text = null
        }

        fun setEtTime(etTime: EditText) {
            this.etTime = etTime
        }
    }
}
