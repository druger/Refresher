package com.druger.refresher.data.storage

import com.druger.refresher.data.db.entity.TaskEntity

interface TaskStorage {

    suspend fun getCurrentTasks(): List<TaskEntity>

    suspend fun getDoneTasks(): List<TaskEntity>

    suspend fun insert(task: TaskEntity)

    suspend fun update(task: TaskEntity)

    suspend fun delete(task: TaskEntity)
}