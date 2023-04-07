package com.druger.refresher.presentation.main

import androidx.lifecycle.*
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

    private val mutableState = MutableLiveData<MainState>()
    var state: LiveData<MainState> = mutableState

    init {
        mutableState.value = MainEmptyState()
    }

    fun sendEvent(event: MainEvent) {
        when (event) {
            is GetCurrentTasksEvent -> getCurrentTasks()
            is GetDoneTasksEvent -> getDoneTasks()
            is InsertTaskEvent -> insertTask(event.task)
            is UpdateTaskEvent -> updateTask(event.task)
        }
    }

    private fun getCurrentTasks() {
        loadingState()
        state = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(MainLoadedState(getCurrentTasksUseCase.execute()))
        }
    }

    private fun getDoneTasks() {
        loadingState()
        state = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(MainLoadedState(getDoneTasksUseCase.execute()))
        }
    }

    private fun loadingState() {
        mutableState.value = MainLoadingState
    }

    private fun insertTask(task: TaskModel) = viewModelScope.launch(Dispatchers.IO) {
        addTaskUseCase.execute(task)
    }

    private fun updateTask(task: TaskModel) = viewModelScope.launch(Dispatchers.IO) {
        updateTaskUseCase.execute(task)
    }
}