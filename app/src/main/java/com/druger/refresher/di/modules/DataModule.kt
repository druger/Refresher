package com.druger.refresher.di.modules

import android.content.Context
import com.druger.refresher.data.db.TaskRoomDatabase
import com.druger.refresher.data.db.dao.TaskDao
import com.druger.refresher.data.repository.TaskRepositoryImpl
import com.druger.refresher.data.storage.DatabaseTaskStorage
import com.druger.refresher.data.storage.TaskStorage
import com.druger.refresher.domain.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideTaskDao(@ApplicationContext context: Context): TaskDao =
        TaskRoomDatabase.getDatabase(context).taskDao()

    @Provides
    @Singleton
    fun provideTaskStorage(taskDao: TaskDao): TaskStorage =
        DatabaseTaskStorage(taskDao)


    @Provides
    @Singleton
    fun provideTaskRepository(taskStorage: TaskStorage): TaskRepository =
        TaskRepositoryImpl(taskStorage)
}