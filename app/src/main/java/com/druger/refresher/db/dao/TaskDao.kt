package com.druger.refresher.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.druger.refresher.db.entity.Task

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Query("SELECT * from task_table WHERE status = :status ORDER BY time_stamp DESC")
    fun getTasks(status: Int): LiveData<List<Task>>

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)
}