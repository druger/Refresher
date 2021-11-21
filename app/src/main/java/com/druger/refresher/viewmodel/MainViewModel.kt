package com.druger.refresher.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.druger.refresher.db.TaskRoomDatabase
import com.druger.refresher.models.ModelTask
import com.druger.refresher.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TaskRepository

    lateinit var tasks: LiveData<List<ModelTask>>

    init {
        val taskDao = TaskRoomDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(taskDao)
    }

    fun getCurrentTasks() {
        tasks = repository.getCurrentTasks()
    }

    fun getDoneTasks() {
        tasks = repository.getDoneTasks()
    }

    fun insertTask(task: ModelTask) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(task)
    }

    fun updateTask(task: ModelTask) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(task)
    }
}