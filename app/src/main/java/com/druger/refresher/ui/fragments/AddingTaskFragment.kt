package com.druger.refresher.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.druger.refresher.R
import com.druger.refresher.ui.dialogs.DatePickerFragment
import com.druger.refresher.ui.dialogs.TimePickerFragment
import kotlinx.android.synthetic.main.fragment_adding_task.*

class AddingTaskFragment: Fragment(R.layout.fragment_adding_task) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvDate.setOnClickListener { showDatePicker() }
        tvTime.setOnClickListener { showTimePicker() }
    }

    private fun showTimePicker() {
        TimePickerFragment(tvTime).show(parentFragmentManager, TIME_PICKER_TAG)
    }

    private fun showDatePicker() {
        DatePickerFragment(tvDate).show(parentFragmentManager, DATE_PICKER_TAG)
    }

    companion object {
        private const val DATE_PICKER_TAG = "datePicker"
        private const val TIME_PICKER_TAG = "timePicker"
    }
}