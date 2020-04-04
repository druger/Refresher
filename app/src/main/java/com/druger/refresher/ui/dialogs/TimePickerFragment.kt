package com.druger.refresher.ui.dialogs

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.druger.refresher.utils.DateHelper
import java.util.*

class TimePickerFragment(var tvTime: TextView) : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        tvTime.text = DateHelper.getTime(calendar.timeInMillis)
    }

    override fun onCancel(dialog: DialogInterface) {
        tvTime.text = null
    }
}
