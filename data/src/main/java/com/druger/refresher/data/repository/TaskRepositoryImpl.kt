package com.druger.refresher.data.repository

import com.druger.refresher.data.db.entity.TaskEntity
import com.druger.refresher.data.db.entity.map
import com.druger.refresher.data.storage.TaskStorage
import com.druger.refresher.domain.model.TaskModel
import com.druger.refresher.domain.repository.TaskRepository

class TaskRepositoryImpl(private val taskStorage: TaskStorage) : TaskRepository {

    override suspend fun getCurrentTasks(): List<TaskModel> {
        return taskStorage.getCurrentTasks().map { it.map() }
    }

    override suspend fun getDoneTasks(): List<TaskModel> {
        return taskStorage.getDoneTasks().map { it.map() }
    }

    override suspend fun insert(task: TaskModel) {
        taskStorage.insert(task.map())
    }

    override suspend fun update(task: TaskModel) {
        taskStorage.update(task.map())
    }

    override suspend fun delete(task: TaskModel) {
        taskStorage.delete(task.map())
    }

    private fun TaskModel.map() = TaskEntity(id, title, timeStamp, status, reminderDate)
}