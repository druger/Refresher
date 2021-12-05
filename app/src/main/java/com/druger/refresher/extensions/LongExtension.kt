package com.druger.refresher.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Long.getDate(): String {
    val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
    return dateFormat.format(this)
}

fun Long.getTime(): String {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(this)
}

fun Long.getFullDate(): String {
    val fullDateFormat = SimpleDateFormat("dd.MM.yy HH:mm", Locale.getDefault())
    return fullDateFormat.format(this)
}
