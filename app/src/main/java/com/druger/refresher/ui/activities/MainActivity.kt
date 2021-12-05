package com.druger.refresher.ui.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.druger.refresher.R
import com.druger.refresher.db.entity.Task
import com.druger.refresher.extensions.getDate
import com.druger.refresher.extensions.getTime
import com.druger.refresher.models.ModelTask
import com.druger.refresher.ui.Screen
import com.druger.refresher.ui.Theme
import com.druger.refresher.ui.compose.AddingTaskCompose
import com.druger.refresher.ui.compose.TaskRowCompose
import com.druger.refresher.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import java.util.*

@ExperimentalAnimationApi
class MainActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var reminderCalendar: Calendar
    private var titleText = mutableStateOf("")
    private var dateText = mutableStateOf("")
    private var timeText = mutableStateOf("")

    private var navigationVisibility = mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reminderCalendar = Calendar.getInstance()
        initDateAndTimeTitle()
        setContent { setContent() }
    }

    private fun initDateAndTimeTitle() {
        dateText.value = getString(R.string.task_date)
        timeText.value = getString(R.string.task_time)
    }

    override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        reminderCalendar.set(year, monthOfYear, dayOfMonth)
        dateText.value = reminderCalendar.timeInMillis.getDate()
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        reminderCalendar.apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
        }
        timeText.value = reminderCalendar.timeInMillis.getTime()
    }

    @Preview
    @Composable
    private fun setContent() {
        val navController = rememberNavController()
        setVisibilityNavigation(navController)

        Theme.setupTheme {
            Scaffold(
                topBar = { setupToolbar(navigationVisibility) },
                bottomBar = { setupBottomBar(navController, navigationVisibility) },
                floatingActionButton = { setupFAB(navController) }
            ) { innerPadding ->
                navigation(navController, innerPadding)
            }
        }
    }

    @Composable
    private fun setupToolbar(navigationVisibility: MutableState<Boolean>) {
        AnimatedVisibility(visible = navigationVisibility.value) {
            // TODO add search
            TopAppBar {

            }
        }
    }

    @Composable
    private fun setupFAB(navController: NavHostController) {
        FloatingActionButton(
            onClick = { navController.navigate(Screen.AddingTask.route) },
            contentColor = Color.White
        ) {
            Icon(Icons.Filled.Add, contentDescription = null)
        }
    }

    @Composable
    private fun navigation(
        navController: NavHostController,
        innerPadding: PaddingValues
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.CurrentTasks.route,
            Modifier.padding(innerPadding)
        ) {
            composable(Screen.CurrentTasks.route) { showCurrentTasks() }
            composable(Screen.DoneTasks.route) { showDoneTasks() }
            composable(Screen.AddingTask.route) { showAddingTask(navController) }
        }
    }

    @Composable
    private fun showAddingTask(navController: NavHostController) {
        Scaffold(
            topBar = {
                AddingTaskCompose.addTaskToolbar(
                    navController,
                    addTask = { addTask(navController) })
            },
            content = {
                AddingTaskCompose.addTaskLayout(
                    titleText.value,
                    dateText = dateText.value,
                    timeText = timeText.value,
                    onValueChange = { titleText.value = it },
                    onDateClick = { showDatePicker() },
                    onTimeClick = { showTimePicker() }
                )
            }
        )
    }

    private fun addTask(navController: NavHostController) {
        val title = titleText.value.trim()
        if (title.isNotEmpty()) {
            lifecycleScope.launch {
                viewModel.insertTask(
                    ModelTask(
                        title = title,
                        reminderDate = reminderCalendar.timeInMillis
                    )
                ).join()
                navController.navigateUp()
            }
        }
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(
            this,
            this,
            hour,
            minute,
            DateFormat.is24HourFormat(this)
        ).apply { show() }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            this,
            this,
            year,
            month,
            day
        ).apply { show() }
    }

    @Composable
    private fun showDoneTasks() {
        viewModel.getDoneTasks()
        setupRecycler()
    }

    @Composable
    private fun showCurrentTasks() {
        viewModel.getCurrentTasks()
        setupRecycler()
    }

    @Composable
    private fun setupRecycler() {
        val tasks = viewModel.tasks.observeAsState().value
        tasks?.let {
            LazyColumn {
                items(it) { task ->
                    TaskRowCompose.TaskRow(
                        task,
                        moveTaskToDone = { moveTaskToDone(task) },
                        moveTaskToCurrent = { moveTaskToCurrent(task) }
                    )
                }
            }
        }
    }

    private fun moveTaskToDone(task: ModelTask) {
        lifecycleScope.launch {
            viewModel.updateTask(task.copy(status = Task.STATUS_DONE))
        }
    }

    private fun moveTaskToCurrent(task: ModelTask) {
        lifecycleScope.launch {
            viewModel.updateTask(task.copy(status = Task.STATUS_CURRENT))
        }
    }

    @Composable
    private fun setupBottomBar(
        navController: NavHostController,
        navigationVisibility: MutableState<Boolean>
    ) {
        AnimatedVisibility(visible = navigationVisibility.value) {
            val menus = listOf(Screen.CurrentTasks, Screen.DoneTasks)

            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                menus.forEach { item ->
                    BottomNavigationItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                navController.graph.startDestinationRoute?.let { route ->
                                    popUpTo(route) { saveState = true }
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                ImageVector.vectorResource(item.icon),
                                contentDescription = null
                            )
                        },
                        label = { Text(stringResource(item.title)) }
                    )
                }
            }
        }
    }

    private fun setVisibilityNavigation(navController: NavHostController) {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.route) {
                Screen.CurrentTasks.route,
                Screen.DoneTasks.route -> {
                    showBottomNav()
                }
                else -> hideBottomNav()
            }
        }
    }

    private fun showBottomNav() {
        navigationVisibility.value = true
    }

    private fun hideBottomNav() {
        navigationVisibility.value = false
    }
}
