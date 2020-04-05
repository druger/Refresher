package com.druger.refresher.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.druger.refresher.R
import com.druger.refresher.models.ModelTaskNew
import com.druger.refresher.ui.dialogs.DatePickerFragment
import com.druger.refresher.ui.dialogs.TimePickerFragment
import com.druger.refresher.viewmodel.TaskViewModel
import kotlinx.android.synthetic.main.fragment_adding_task.*
import kotlinx.android.synthetic.main.toolbar_adding.*
import kotlinx.coroutines.launch

class AddingTaskFragment: Fragment(R.layout.fragment_adding_task) {

    private val taskModel: TaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvDate.setOnClickListener { showDatePicker() }
        tvTime.setOnClickListener { showTimePicker() }
        ivClose.setOnClickListener { findNavController().navigateUp() }
        ivDone.setOnClickListener { addTask() }
    }

    private fun addTask() {
        val title = etTitle.text.toString().trim()
        if (title.isNotEmpty()) {
            viewLifecycleOwner.lifecycleScope.launch {
                taskModel.insertTask(ModelTaskNew(title)).join()
                findNavController().navigateUp()
            }
        }
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