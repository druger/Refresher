package com.druger.refresher.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.druger.refresher.data.db.dao.TaskDao
import com.druger.refresher.data.db.entity.Task
import com.druger.refresher.data.db.entity.map
import com.druger.refresher.domain.task.model.ModelTask
import com.druger.refresher.domain.task.model.map
import com.druger.refresher.domain.task.repository.TaskRepository

class TaskRepositoryImpl(private val taskDao: TaskDao) : TaskRepository {

    override suspend fun getCurrentTasks(): LiveData<List<ModelTask>> {
        return getTasks(Task.STATUS_CURRENT)
    }

    override suspend fun getDoneTasks(): LiveData<List<ModelTask>> {
        return getTasks(Task.STATUS_DONE)
    }

    private fun getTasks(status: Int): LiveData<List<ModelTask>> {
        return taskDao.getTasks(status).map { tasks ->
            tasks.map { task ->
                task.map()
            }
        }
    }

    override suspend fun insert(task: ModelTask) {
        taskDao.insert(task.map())
    }

    override suspend fun update(task: ModelTask) = taskDao.update(task.map())

    override suspend fun delete(task: ModelTask) = taskDao.delete(task.map())
}