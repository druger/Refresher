package com.druger.refresher.di.modules

import com.druger.refresher.domain.repository.TaskRepository
import com.druger.refresher.domain.usecase.AddTaskUseCase
import com.druger.refresher.domain.usecase.GetCurrentTasksUseCase
import com.druger.refresher.domain.usecase.GetDoneTasksUseCase
import com.druger.refresher.domain.usecase.UpdateTaskUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideGetCurrentTasksUseCase(taskRepository: TaskRepository): GetCurrentTasksUseCase =
        GetCurrentTasksUseCase(taskRepository)

    @Provides
    fun provideGetDoneTasksUseCase(taskRepository: TaskRepository): GetDoneTasksUseCase =
        GetDoneTasksUseCase(taskRepository)

    @Provides
    fun provideAddTaskUseCase(taskRepository: TaskRepository): AddTaskUseCase =
        AddTaskUseCase(taskRepository)

    @Provides
    fun provideUpdateTaskUseCase(taskRepository: TaskRepository): UpdateTaskUseCase =
        UpdateTaskUseCase(taskRepository)
}