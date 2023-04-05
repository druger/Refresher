package com.druger.refresher.presentation.main

import android.app.Application
import androidx.lifecycle.*
import com.druger.refresher.data.db.TaskRoomDatabase
import com.druger.refresher.data.repository.TaskRepositoryImpl
import com.druger.refresher.data.storage.DatabaseTaskStorage
import com.druger.refresher.data.storage.TaskStorage
import com.druger.refresher.domain.model.TaskModel
import com.druger.refresher.domain.repository.TaskRepository
import com.druger.refresher.domain.usecase.AddTaskUseCase
import com.druger.refresher.domain.usecase.GetCurrentTasksUseCase
import com.druger.refresher.domain.usecase.GetDoneTasksUseCase
import com.druger.refresher.domain.usecase.UpdateTaskUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val taskRepository: TaskRepository
    private val taskStorage: TaskStorage

    private val getCurrentTasksUseCase: GetCurrentTasksUseCase
    private val getDoneTasksUseCase: GetDoneTasksUseCase
    private val addTaskUseCase: AddTaskUseCase
    private val updateTaskUseCase: UpdateTaskUseCase

    private val tasksMutableLiveData: MutableLiveData<List<TaskModel>> by lazy {
        MutableLiveData<List<TaskModel>>()
    }
    var tasksLiveData: LiveData<List<TaskModel>>? = null

    init {
        val taskDao = TaskRoomDatabase.getDatabase(application).taskDao()
        taskStorage = DatabaseTaskStorage(taskDao)
        taskRepository = TaskRepositoryImpl(taskStorage)

        getCurrentTasksUseCase = GetCurrentTasksUseCase(taskRepository)
        getDoneTasksUseCase = GetDoneTasksUseCase(taskRepository)
        addTaskUseCase = AddTaskUseCase(taskRepository)
        updateTaskUseCase = UpdateTaskUseCase(taskRepository)
    }

    fun getCurrentTasks() {
        tasksLiveData = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(getCurrentTasksUseCase.execute())
        }
    }

    fun getDoneTasks() {
        tasksLiveData = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(getDoneTasksUseCase.execute())
        }
    }

    fun insertTask(task: TaskModel) = viewModelScope.launch(Dispatchers.IO) {
        addTaskUseCase.execute(task)
    }

    fun updateTask(task: TaskModel) = viewModelScope.launch(Dispatchers.IO) {
        updateTaskUseCase.execute(task)
    }
}