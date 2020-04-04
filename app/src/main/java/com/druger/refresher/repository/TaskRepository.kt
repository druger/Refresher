package com.druger.refresher.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.druger.refresher.database.dao.TaskDao
import com.druger.refresher.database.entity.Task
import com.druger.refresher.database.entity.map
import com.druger.refresher.models.ModelTaskNew
import com.druger.refresher.models.map

class TaskRepository(private val taskDao: TaskDao) {

    fun getCurrentTasks(): LiveData<List<ModelTaskNew>> =
        getTasks(Task.STATUS_CURRENT)

    fun getDoneTasks(): LiveData<List<ModelTaskNew>> = getTasks(Task.STATUS_DONE)

    private fun getTasks(status: Int): LiveData<List<ModelTaskNew>> {
        return taskDao.getTasks(status).map { tasks ->
            tasks.map { task ->
                task.map()
            }
        }
    }

    suspend fun insert(task: ModelTaskNew) {
        taskDao.insert(task.map())
    }

    fun update(task: Task) = taskDao.update(task)

    fun delete(task: Task) = taskDao.delete(task)
}