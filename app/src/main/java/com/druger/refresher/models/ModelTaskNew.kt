package com.druger.refresher.models

import com.druger.refresher.database.entity.Task
import java.util.*

data class ModelTaskNew(
    val id: Int,
    val title: String,
    val timeStamp: Long = Date().time,
    val status: Int = -1,
    val priority: Int = 1,
    val reminderDate: Long = 0
)

fun ModelTaskNew.map() = Task(id, title, timeStamp, status, priority, reminderDate)