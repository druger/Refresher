package com.druger.refresher.presentation.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.druger.refresher.domain.model.TaskModel
import com.druger.refresher.domain.usecase.AddTaskUseCase
import com.druger.refresher.domain.usecase.GetCurrentTasksUseCase
import com.druger.refresher.domain.usecase.GetDoneTasksUseCase
import com.druger.refresher.domain.usecase.UpdateTaskUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Rule
import org.junit.jupiter.api.*
import org.mockito.Mockito
import org.mockito.Mockito.mock

@OptIn(ExperimentalCoroutinesApi::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MainViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()

    private val getCurrentTasksUseCase = mock<GetCurrentTasksUseCase>()
    private val getDoneTasksUseCase = mock<GetDoneTasksUseCase>()
    private val addTaskUseCase = mock<AddTaskUseCase>()
    private val updateTaskUseCase = mock<UpdateTaskUseCase>()

    private lateinit var viewModel: MainViewModel

    @AfterEach
    fun clear() {
        Mockito.reset(getCurrentTasksUseCase)
        Mockito.reset(getDoneTasksUseCase)
        Mockito.reset(addTaskUseCase)
        Mockito.reset(updateTaskUseCase)
    }

    @AfterAll
    fun reset() {
        Dispatchers.resetMain()
    }

    @BeforeAll
    fun init() {
        Dispatchers.setMain(dispatcher)
    }

    @BeforeEach
    fun setup() {
        viewModel = MainViewModel(
            getCurrentTasksUseCase,
            getDoneTasksUseCase,
            addTaskUseCase,
            updateTaskUseCase
        )
    }

    @Test
    fun shouldGetCurrentTasks() = runTest {
        val tasks = listOf(TaskModel(
            title = "test title",
            timeStamp = 1L
        ))

        viewModel.sendEvent(GetCurrentTasksEvent)

        Mockito.`when`(getCurrentTasksUseCase.execute())
            .thenReturn(tasks)

        val expected = MainLoadedState(tasks)
        val actual = viewModel.state.value

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldGetDoneTasks() {

    }

    @Test
    fun shouldInsertTask() {

    }

    @Test
    fun shouldUpdateTask() {

    }
}