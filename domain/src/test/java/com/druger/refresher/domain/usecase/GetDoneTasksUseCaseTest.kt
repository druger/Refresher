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
class GetDoneTasksUseCaseTest {

    private val taskRepository = mock<TaskRepository>()

    @Test
    fun getCorrectData() = runTest {
        val task = TaskModel(
            title = "test title",
            timeStamp = 1L,
            status = 2
        )
        Mockito.`when`(taskRepository.getDoneTasks())
            .thenReturn(listOf(task))

        val useCase = GetDoneTasksUseCase(taskRepository)
        val actual = useCase.execute()
        val expected = listOf(task)
        assertEquals(expected, actual)
    }
}