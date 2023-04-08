package com.druger.refresher.domain.usecase

import com.druger.refresher.domain.model.TaskModel
import com.druger.refresher.domain.repository.TaskRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

@OptIn(ExperimentalCoroutinesApi::class)
class UpdateTaskUseCaseTest {

    private val taskRepository = mock<TaskRepository>()

    @Test
    fun checkOnlyOneUpdateCompleted() = runTest {
        val useCase = UpdateTaskUseCase(taskRepository)
        val task = TaskModel(title = "test")
        useCase.execute(task)
        Mockito.verify(taskRepository, Mockito.only()).update(task)
    }
}