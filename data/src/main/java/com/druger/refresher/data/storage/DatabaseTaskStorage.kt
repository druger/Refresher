package com.druger.refresher.data.storage

import com.druger.refresher.data.db.dao.TaskDao
import com.druger.refresher.data.db.entity.TaskEntity

class DatabaseTaskStorage(private val taskDao: TaskDao) : TaskStorage {

    override suspend fun getCurrentTasks(): List<TaskEntity> {
        return getTasks(TaskEntity.STATUS_CURRENT)
    }

    override suspend fun getDoneTasks(): List<TaskEntity> {
        return getTasks(TaskEntity.STATUS_DONE)
    }

    private fun getTasks(status: Int): List<TaskEntity> {
        return taskDao.getTasks(status)
    }

    override suspend fun insert(task: TaskEntity) {
        taskDao.insert(task)
    }

    override suspend fun update(task: TaskEntity) {
        taskDao.update(task)
    }

    override suspend fun delete(task: TaskEntity) {
        taskDao.delete(task)
    }
}