package com.druger.refresher.domain.model

import java.util.*

data class TaskModel(
    val title: String,
    val timeStamp: Long = Date().time,
    var status: Int = 0,
    val reminderDate: Long = 0,
    val id: Int = 0
)