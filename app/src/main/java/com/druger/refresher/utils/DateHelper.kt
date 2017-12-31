package com.druger.refresher.utils

import java.text.SimpleDateFormat
import java.util.*

/**
* Created by druger on 15.09.2015.
*/
class DateHelper {

    companion object {
        fun getDate(date: Long): String {
            val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
            return dateFormat.format(date)
        }

        fun getTime(time: Long): String {
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            return timeFormat.format(time)
        }

        fun getFullDate(date: Long): String {
            val fullDateFormat = SimpleDateFormat("dd.MM.yy HH:mm", Locale.getDefault())
            return fullDateFormat.format(date)
        }
    }
}
