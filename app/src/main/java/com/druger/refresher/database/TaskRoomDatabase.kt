package com.druger.refresher.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.druger.refresher.database.dao.TaskDao
import com.druger.refresher.database.entity.Task

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TaskRoomDatabase: RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {

        @Volatile
        private var INSTANCE: TaskRoomDatabase? = null

        fun getDatabase(context: Context): TaskRoomDatabase {
           val tempInstance = INSTANCE
            if (tempInstance != null) return tempInstance

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskRoomDatabase::class.java,
                    "task_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}