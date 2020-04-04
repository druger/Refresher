package com.druger.refresher.models

import com.druger.refresher.database.entity.Task
import java.util.*

data class ModelTaskNew(
    val title: String,
    val timeStamp: Long = Date().time,
    val status: Int = Task.STATUS_CURRENT,
    val reminderDate: Long = 0,
    val id: Int = 0
)

fun ModelTaskNew.map() = Task(id, title, timeStamp, status, reminderDate)