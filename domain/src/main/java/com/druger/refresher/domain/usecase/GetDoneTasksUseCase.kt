package com.druger.refresher.domain.usecase

import com.druger.refresher.domain.model.TaskModel
import com.druger.refresher.domain.repository.TaskRepository

class GetDoneTasksUseCase(private val taskRepository: TaskRepository) {

    suspend fun execute(): List<TaskModel> {
        return taskRepository.getDoneTasks()
    }
}