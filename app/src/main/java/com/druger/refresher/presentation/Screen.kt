package com.druger.refresher.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.druger.refresher.R

sealed class Screen(
    val route: String,
    @StringRes val title: Int,
    @DrawableRes val icon: Int = -1
) {
    object CurrentTasks : Screen("currentTasks", R.string.current_task, R.drawable.ic_current_task)
    object DoneTasks : Screen("doneTasks", R.string.done_task, R.drawable.ic_done_task)
    object AddingTask : Screen("addingTask", R.string.adding_task)
}
