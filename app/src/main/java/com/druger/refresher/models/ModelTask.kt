package com.druger.refresher.models

import com.druger.refresher.db.entity.Task
import java.util.*

data class ModelTask(
    val title: String,
    val timeStamp: Long = Date().time,
    var status: Int = Task.STATUS_CURRENT,
    val reminderDate: Long = 0,
    val id: Int = 0
)

fun ModelTask.map() = Task(id, title, timeStamp, status, reminderDate)