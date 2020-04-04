package com.druger.refresher.ui.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.druger.refresher.utils.DateHelper
import java.util.*

class DatePickerFragment(var tvDate: TextView) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        tvDate.text = DateHelper.getDate(calendar.timeInMillis)
    }

    override fun onCancel(dialog: DialogInterface) {
        tvDate.text = null
    }
}
