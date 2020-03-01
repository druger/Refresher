package com.druger.refresher.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "time_stamp") val timeStamp: Long =  Date().time,
    @ColumnInfo(name = "status") val status: Int = -1,
    @ColumnInfo(name = "priority") val priority: Int,
    @ColumnInfo(name = "reminder_date") val reminderDate: Long
)