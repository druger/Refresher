package com.druger.refresher.domain.usecase

import com.druger.refresher.domain.model.TaskModel
import com.druger.refresher.domain.repository.TaskRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

@OptIn(ExperimentalCoroutinesApi::class)
class GetCurrentTasksUseCaseTest {

    private val taskRepository = mock<TaskRepository>()

    @Test
    fun getCorrectData() = runTest {
        Mockito.`when`(taskRepository.getCurrentTasks())
            .thenReturn(listOf(TaskModel(
                title = "test title",
                timeStamp = 1L
            )))

        val useCase = GetCurrentTasksUseCase(taskRepository)
        val actual = useCase.execute()
        val expected = listOf(TaskModel(title = "test title", timeStamp = 1L))
        assertEquals(expected, actual)
    }
}