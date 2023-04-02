package com.druger.refresher.presentation.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.druger.refresher.data.db.TaskRoomDatabase
import com.druger.refresher.data.repository.TaskRepositoryImpl
import com.druger.refresher.domain.task.model.ModelTask
import com.druger.refresher.domain.task.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TaskRepository

    lateinit var tasks: LiveData<List<ModelTask>>

    init {
        val taskDao = TaskRoomDatabase.getDatabase(application).taskDao()
        repository = TaskRepositoryImpl(taskDao)
    }

    fun getCurrentTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            tasks = repository.getCurrentTasks()
        }
    }

    fun getDoneTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            tasks = repository.getDoneTasks()
        }
    }

    fun insertTask(task: ModelTask) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(task)
    }

    fun updateTask(task: ModelTask) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(task)
    }
}