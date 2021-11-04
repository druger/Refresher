package com.druger.refresher.ui.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.druger.refresher.R
import com.druger.refresher.db.entity.Task
import com.druger.refresher.models.ModelTask
import com.druger.refresher.ui.Screen
import com.druger.refresher.ui.Theme
import com.druger.refresher.utils.DateHelper
import com.druger.refresher.viewmodel.TaskViewModel
import kotlinx.coroutines.launch


// TODO Add view model
class MainActivity : AppCompatActivity() {

    private val viewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            setContent()
        }
    }

    @Preview
    @Composable
    private fun setupToolbar() {
        // TODO add search
        TopAppBar {

        }
    }

    @Preview
    @Composable
    private fun setContent() {
        val navController = rememberNavController()

        Theme.setupTheme {
            Scaffold(
                topBar = { setupToolbar() },
                bottomBar = { setupBottomBar(navController) },
                floatingActionButton = { setupFAB() }
            ) { innerPadding ->
                navigation(navController, innerPadding)
            }
        }
    }

    @Composable
    private fun setupFAB() {
        FloatingActionButton(
            onClick = { /*TODO*/ },
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
        }
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
                    TaskRow(task)
                }
            }
        }
    }

    @Composable
    private fun TaskRow(task: ModelTask) {
        val isDone = task.status == Task.STATUS_DONE
        val isCurrent = task.status == Task.STATUS_CURRENT
        Row(
            modifier = Modifier
                .height(60f.dp)
                .padding(horizontal = 8f.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Checkbox(
                checked = isDone,
                onCheckedChange = {
                    if (isCurrent) moveTaskToDone(task)
                    else if (isDone) moveTaskToCurrent(task)
                },
                modifier = Modifier.weight(0.1f)
            )
            Text(
                text = task.title,
                modifier = Modifier.weight(0.6f),
                color = colorResource(R.color.primary_text_light),
                fontSize = 16.sp
            )
            val reminderDate = task.reminderDate
            if (reminderDate != 0L) {
                Text(
                    text = DateHelper.getFullDate(reminderDate),
                    modifier = Modifier.weight(0.3f),
                    color = colorResource(R.color.secondary_text_light),
                    fontSize = 14.sp
                )
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
    private fun setupBottomBar(navController: NavHostController) {
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

    private fun setVisibilityNavigation(navController: NavController) {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {

            }
        }
    }

    private fun showToolbar() {

    }

    private fun showBottomNav() {

    }

    private fun hideToolbar() {

    }

    private fun hideBottomNav() {

    }
}
