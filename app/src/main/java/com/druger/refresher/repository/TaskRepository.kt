package com.druger.refresher.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.druger.refresher.db.dao.TaskDao
import com.druger.refresher.db.entity.Task
import com.druger.refresher.db.entity.map
import com.druger.refresher.models.ModelTask
import com.druger.refresher.models.map

class TaskRepository(private val taskDao: TaskDao) {

    fun getCurrentTasks(): LiveData<List<ModelTask>> =
        getTasks(Task.STATUS_CURRENT)

    fun getDoneTasks(): LiveData<List<ModelTask>> = getTasks(Task.STATUS_DONE)

    private fun getTasks(status: Int): LiveData<List<ModelTask>> {
        return taskDao.getTasks(status).map { tasks ->
            tasks.map { task ->
                task.map()
            }
        }
    }

    suspend fun insert(task: ModelTask) {
        taskDao.insert(task.map())
    }

    fun update(task: Task) = taskDao.update(task)

    fun delete(task: Task) = taskDao.delete(task)
}