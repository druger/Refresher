package com.druger.refresher.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.druger.refresher.db.TaskRoomDatabase
import com.druger.refresher.models.ModelTask
import com.druger.refresher.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(application: Application): AndroidViewModel(application) {
    private val repository: TaskRepository

    private var tasks: MutableLiveData<List<ModelTask>>? = null

    init {
        val taskDao = TaskRoomDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(taskDao)
    }

    fun getTasks(): LiveData<List<ModelTask>> {
        if (tasks == null) {
            loadTasks()
        }
        return tasks as MutableLiveData<List<ModelTask>>
    }

    private fun loadTasks() {
        tasks = repository.getCurrentTasks() as MutableLiveData<List<ModelTask>>
    }

    fun insertTask(task: ModelTask) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(task)
    }
}