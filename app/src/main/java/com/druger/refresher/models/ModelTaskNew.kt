package com.druger.refresher.models

import java.util.*

class ModelTaskNew(
    val id: Int,
    val title: String,
    val timeStamp: Long = Date().time,
    val status: Int = -1,
    val priority: Int = 1,
    val reminderDate: Long?
)