package com.druger.refresher.ui.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.druger.refresher.databinding.FragmentAddingTaskBinding
import com.druger.refresher.models.ModelTaskNew
import com.druger.refresher.utils.DateHelper
import com.druger.refresher.viewmodel.TaskViewModel
import kotlinx.coroutines.launch
import java.util.*

class AddingTaskFragment : Fragment(),
    DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private val taskModel: TaskViewModel by viewModels()

    private var binding: FragmentAddingTaskBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddingTaskBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            tvDate.setOnClickListener { showDatePicker() }
            tvTime.setOnClickListener { showTimePicker() }
            toolbar.ivClose.setOnClickListener { findNavController().navigateUp() }
            toolbar.ivDone.setOnClickListener { addTask() }
        }
    }

    override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        binding?.tvDate?.text = DateHelper.getDate(calendar.timeInMillis)
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        binding?.tvTime?.text = DateHelper.getTime(calendar.timeInMillis)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun addTask() {
        val title = binding?.etTitle?.text.toString().trim()
        if (title.isNotEmpty()) {
            viewLifecycleOwner.lifecycleScope.launch {
                taskModel.insertTask(ModelTaskNew(title)).join()
                findNavController().navigateUp()
            }
        }
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(
            requireContext(),
            this,
            hour,
            minute,
            DateFormat.is24HourFormat(activity)
        ).apply { show() }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            requireContext(),
            this,
            year,
            month,
            day
        ).apply { show() }
    }
}