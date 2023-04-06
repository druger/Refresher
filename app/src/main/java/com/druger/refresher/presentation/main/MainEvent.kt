package com.druger.refresher.presentation.main

import com.druger.refresher.domain.model.TaskModel

sealed interface MainEvent

object GetCurrentTasksEvent : MainEvent

object GetDoneTasksEvent : MainEvent

class InsertTaskEvent(val task: TaskModel) : MainEvent

class UpdateTaskEvent(val task: TaskModel) : MainEvent





