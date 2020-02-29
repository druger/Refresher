package com.druger.refresher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.druger.refresher.models.ModelTask

class TaskViewModel: ViewModel() {
    private var tasks: MutableLiveData<List<ModelTask>>? = null

    fun getTasks(): LiveData<List<ModelTask>> {
        if (tasks == null) {
            tasks = MutableLiveData()
            loadTasks()
        }
        return tasks as MutableLiveData<List<ModelTask>>
    }

    private fun loadTasks() {

    }
}