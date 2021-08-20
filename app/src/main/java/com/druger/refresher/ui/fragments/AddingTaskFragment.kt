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
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.druger.refresher.R
import com.druger.refresher.models.ModelTask
import com.druger.refresher.ui.Theme
import com.druger.refresher.utils.DateHelper
import com.druger.refresher.viewmodel.TaskViewModel
import kotlinx.coroutines.launch
import java.util.*

class AddingTaskFragment : Fragment(),
    DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private val taskModel: TaskViewModel by viewModels()

    private lateinit var reminderCalendar: Calendar
    private var titleText = mutableStateOf("")
    private var dateText = mutableStateOf("")
    private var timeText = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reminderCalendar = Calendar.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            Theme.setupTheme {
                Scaffold(
                    topBar = { addToolbar() },
                    content = { addLayout() }
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dateText.value = getString(R.string.task_date)
        timeText.value = getString(R.string.task_time)
    }

    override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        reminderCalendar.set(year, monthOfYear, dayOfMonth)
        dateText.value = DateHelper.getDate(reminderCalendar.timeInMillis)
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        reminderCalendar.apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
        }
        timeText.value = DateHelper.getTime(reminderCalendar.timeInMillis)
    }

    private fun addTask() {
        val title = titleText.value.trim()
        if (title.isNotEmpty()) {
            lifecycleScope.launch {
                taskModel.insertTask(
                    ModelTask(
                        title = title,
                        reminderDate = reminderCalendar.timeInMillis
                    )
                ).join()
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

    @Preview
    @Composable
    private fun addToolbar() {
        TopAppBar {
            Image(
                ImageVector.vectorResource(R.drawable.ic_close),
                contentDescription = null,
                modifier = Modifier
                    .weight(.1f)
                    .clickable { findNavController().navigateUp() }
            )

            Text(
                stringResource(R.string.add_task),
                modifier = Modifier.weight(.8f),
                textAlign = TextAlign.Center
            )

            Image(
                ImageVector.vectorResource(R.drawable.ic_done),
                contentDescription = null,
                modifier = Modifier
                    .weight(.1f)
                    .clickable { addTask() }
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun addLayout() {
        var descriptionText by remember { mutableStateOf("") }

        Column(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = titleText.value,
                onValueChange = { titleText.value = it },
                label = { Text(stringResource(R.string.task_title)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textStyle = TextStyle(color = Color.Black, fontSize = 22.sp)
            )

            OutlinedTextField(
                value = descriptionText,
                onValueChange = { descriptionText = it },
                label = { Text(stringResource(R.string.text)) },
                modifier = Modifier.fillMaxWidth()
            )

            ClickableText(
                AnnotatedString(dateText.value),
                modifier = Modifier.padding(16.dp),
                onClick = { showDatePicker() },
                style = TextStyle(
                    fontSize = 22.sp,
                    color = colorResource(R.color.secondary_text_light)
                )
            )

            ClickableText(
                AnnotatedString(timeText.value),
                modifier = Modifier.padding(horizontal = 16.dp),
                onClick = { showTimePicker() },
                style = TextStyle(
                    fontSize = 22.sp,
                    color = colorResource(R.color.secondary_text_light)
                )
            )
        }
    }
}