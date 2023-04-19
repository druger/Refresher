package com.druger.refresher.data.storage

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.druger.refresher.data.db.TaskRoomDatabase
import com.druger.refresher.data.db.dao.TaskDao
import com.druger.refresher.data.db.entity.TaskEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class DatabaseTaskStorageTest {

    private lateinit var taskDao: TaskDao
    private lateinit var db: TaskRoomDatabase
    private lateinit var storage: DatabaseTaskStorage

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, TaskRoomDatabase::class.java).build()
        taskDao = db.taskDao()
        storage = DatabaseTaskStorage(taskDao)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun shouldInsertTask() = runTest {
        val task = TaskEntity(id = 1, title = "Test insert")
        storage.insert(task)

        val tasks = storage.getCurrentTasks()
        assertThat(tasks, hasItem(task))
    }

    @Test
    @Throws(Exception::class)
    fun shouldUpdateTask() = runTest {
        val task = TaskEntity(id = 2, title = "Test update")
        storage.insert(task)

        val tasks = storage.getCurrentTasks()
        val oldTask = tasks.find { it.id == 2 }
        val updatedTask = oldTask?.copy(title = "New update")
        updatedTask?.let { storage.update(it) }

        val updatedTasks = storage.getCurrentTasks()

        assertThat(updatedTasks, hasItem(updatedTask))
    }

    @Test
    @Throws(Exception::class)
    fun shouldDeleteTask() = runTest {
        val task = TaskEntity(id = 3, title = "Test delete")
        storage.insert(task)
        storage.delete(task)

        val tasks = storage.getCurrentTasks()
        assertThat(tasks, not(hasItem(task)))
    }

    @Test
    @Throws(Exception::class)
    fun shouldReturnCurrentTasks() = runTest {
        val task1 = TaskEntity(id = 1, title = "Task1")
        val task2 = TaskEntity(id = 2, title = "Task2", status = TaskEntity.STATUS_DONE)
        val task3 = TaskEntity(id = 3, title = "Task3")
        val task4 = TaskEntity(id = 4, title = "Task4", status = TaskEntity.STATUS_DONE)

        storage.insert(task1)
        storage.insert(task2)
        storage.insert(task3)
        storage.insert(task4)

        val tasks = storage.getCurrentTasks()
        assertThat(tasks, hasItems(task1, task3))
    }

    @Test
    @Throws(Exception::class)
    fun shouldReturnDoneTasks() = runTest {
        val task1 = TaskEntity(id = 1, title = "Task1")
        val task2 = TaskEntity(id = 2, title = "Task2", status = TaskEntity.STATUS_DONE)
        val task3 = TaskEntity(id = 3, title = "Task3")
        val task4 = TaskEntity(id = 4, title = "Task4", status = TaskEntity.STATUS_DONE)

        storage.insert(task1)
        storage.insert(task2)
        storage.insert(task3)
        storage.insert(task4)

        val tasks = storage.getDoneTasks()
        assertThat(tasks, hasItems(task2, task4))
    }
}