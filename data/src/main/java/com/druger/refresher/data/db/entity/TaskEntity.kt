package com.druger.refresher.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.druger.refresher.domain.model.TaskModel
import java.util.*

@Entity(tableName = "task_table")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "time_stamp") val timeStamp: Long =  Date().time,
    @ColumnInfo(name = "status") val status: Int = STATUS_CURRENT,
    @ColumnInfo(name = "reminder_date") val reminderDate: Long = 0
) {
    companion object {
        // TODO make enum
        const val STATUS_OVERDUE = 0
        const val STATUS_CURRENT = 1
        const val STATUS_DONE = 2
    }
}

fun TaskEntity.map() =
    TaskModel(
        title,
        timeStamp,
        status,
        reminderDate,
        id
    )