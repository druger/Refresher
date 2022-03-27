package com.druger.refresher.domain.task.repository

import com.druger.refresher.domain.task.model.ModelTask

interface TaskRepository {

    suspend fun getCurrentTasks()

    suspend fun getDoneTasks()

    suspend fun insert(task: ModelTask)

    suspend fun update(task: ModelTask)

    suspend fun delete(task: ModelTask)
}