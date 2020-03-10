package com.druger.refresher.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.druger.refresher.database.TaskRoomDatabase
import com.druger.refresher.models.ModelTaskNew
import com.druger.refresher.repository.TaskRepository

class TaskViewModel(application: Application): AndroidViewModel(application) {
    private val repository: TaskRepository

    private var tasks: MutableLiveData<List<ModelTaskNew>>? = null

    init {
        val taskDao = TaskRoomDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(taskDao)
    }

    fun getTasks(): LiveData<List<ModelTaskNew>> {
        if (tasks == null) {
            loadTasks()
        }
        return tasks as MutableLiveData<List<ModelTaskNew>>
    }

    private fun loadTasks() {
        tasks = repository.getCurrentTasks() as MutableLiveData<List<ModelTaskNew>>
    }
}