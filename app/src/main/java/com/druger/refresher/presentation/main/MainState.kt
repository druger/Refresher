package com.druger.refresher.presentation.main

import androidx.annotation.StringRes
import com.druger.refresher.domain.model.TaskModel

sealed interface MainState

object MainLoadingState : MainState

data class MainEmptyState(val tasks: List<TaskModel> = emptyList()) : MainState

data class MainLoadedState(val tasks: List<TaskModel>) : MainState

data class MainErrorState(@StringRes val message: Int) : MainState