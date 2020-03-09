package com.druger.refresher.repository

import androidx.lifecycle.LiveData
import com.druger.refresher.database.dao.TaskDao
import com.druger.refresher.database.entity.Task

class TaskRepository(private val taskDao: TaskDao) {

    fun getTasksByStatus(status: Int): LiveData<List<Task>> = taskDao.getTasksByStatus(status)

    fun insert(task: Task) = taskDao.insert(task)

    fun update(task: Task) = taskDao.update(task)

    fun delete(task: Task) = taskDao.delete(task)
}