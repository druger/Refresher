package com.druger.refresher.data.db.dao

import androidx.room.*
import com.druger.refresher.data.db.entity.TaskEntity

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskEntity)

    @Query("SELECT * from task_table WHERE status = :status ORDER BY time_stamp DESC")
    fun getTasks(status: Int): List<TaskEntity>

    @Update
    suspend fun update(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)
}