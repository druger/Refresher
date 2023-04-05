package com.druger.refresher.domain.repository

import com.druger.refresher.domain.model.TaskModel

interface TaskRepository {

    suspend fun getCurrentTasks(): List<TaskModel>

    suspend fun getDoneTasks(): List<TaskModel>

    suspend fun insert(task: TaskModel)

    suspend fun update(task: TaskModel)

    suspend fun delete(task: TaskModel)
}