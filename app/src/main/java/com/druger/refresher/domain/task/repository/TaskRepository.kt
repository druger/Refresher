package com.druger.refresher.domain.task.repository

import androidx.lifecycle.LiveData
import com.druger.refresher.domain.task.model.ModelTask

interface TaskRepository {

    suspend fun getCurrentTasks(): LiveData<List<ModelTask>>

    suspend fun getDoneTasks(): LiveData<List<ModelTask>>

    suspend fun insert(task: ModelTask)

    suspend fun update(task: ModelTask)

    suspend fun delete(task: ModelTask)
}