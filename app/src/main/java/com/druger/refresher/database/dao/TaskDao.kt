package com.druger.refresher.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.druger.refresher.database.entity.Task

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Task)

    @Query("SELECT * from task_table WHERE status = :status ORDER BY time_stamp DESC")
    fun getTasks(status: Int): LiveData<List<Task>>

    @Update
    fun update(task: Task)

    @Delete
    fun delete(task: Task)
}