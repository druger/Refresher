package com.druger.refresher.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.druger.refresher.domain.model.TaskModel
import com.druger.refresher.domain.usecase.AddTaskUseCase
import com.druger.refresher.domain.usecase.GetCurrentTasksUseCase
import com.druger.refresher.domain.usecase.GetDoneTasksUseCase
import com.druger.refresher.domain.usecase.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCurrentTasksUseCase: GetCurrentTasksUseCase,
    private val getDoneTasksUseCase: GetDoneTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase
) : ViewModel() {

    var tasksLiveData: LiveData<List<TaskModel>>? = null

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