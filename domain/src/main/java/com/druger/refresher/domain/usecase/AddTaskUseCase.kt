package com.druger.refresher.domain.usecase

import com.druger.refresher.domain.model.TaskModel
import com.druger.refresher.domain.repository.TaskRepository

class AddTaskUseCase(private val taskRepository: TaskRepository) {

    suspend fun execute(task: TaskModel) {
        taskRepository.insert(task)
    }
}