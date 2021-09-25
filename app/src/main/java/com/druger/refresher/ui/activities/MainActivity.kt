package com.druger.refresher.ui.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.druger.refresher.R
import com.druger.refresher.ui.Screen
import com.druger.refresher.ui.Theme

class MainActivity : AppCompatActivity() {

    private var selectedItem = mutableStateOf(0)

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
                bottomBar = { setupBottomBar(navController) }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = Screen.CurrentTasks.route,
                    Modifier.padding(innerPadding)
                ) {
                    composable(Screen.CurrentTasks.route) { }
                    composable(Screen.DoneTasks.route) { }
                }
            }
        }
    }

    @Composable
    private fun setupBottomBar(navController: NavHostController) {
        val menus = listOf(
            MenuItem(R.string.current_task, R.drawable.ic_current_task),
            MenuItem(R.string.done_task, R.drawable.ic_done_task)
        )

        BottomNavigation {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            menus.forEachIndexed { index, item ->
                BottomNavigationItem(
                    selected = selectedItem.value == index,
                    onClick = {
                        selectedItem.value = index
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
                R.id.current,
                R.id.done -> {
                    showBottomNav()
                    showToolbar()
                }
                else -> {
                    hideBottomNav()
                    hideToolbar()
                }
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

    private data class MenuItem(
        @StringRes var title: Int,
        @DrawableRes var icon: Int
    )
}
