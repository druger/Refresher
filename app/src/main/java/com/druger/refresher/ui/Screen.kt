package com.druger.refresher.ui

import androidx.annotation.StringRes
import com.druger.refresher.R

sealed class Screen(
    val route: String,
    @StringRes val resId: Int
) {
    object CurrentTasks : Screen("currentTasks", R.string.current_task)
    object DoneTasks : Screen("doneTasks", R.string.done_task)
    object AddingTask : Screen("addingTask", R.string.adding_task)
}
