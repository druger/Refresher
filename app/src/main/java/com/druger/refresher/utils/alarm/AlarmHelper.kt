package com.druger.refresher.utils.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

class AlarmHelper(
    private var alarmManager: AlarmManager,
    private var context: Context
) {

    fun setAlarm(task: com.druger.refresher.domain.model.TaskModel) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(EXTRA_TITLE, task.title)
            putExtra(EXTRA_REMINDER_DATE, task.reminderDate)
        }

        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(
            context,
            task.reminderDate.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.set(AlarmManager.RTC_WAKEUP, task.reminderDate, pendingIntent)
    }

    fun removeAlarm(reminderDate: Long) {
        val intent = Intent(context, AlarmReceiver::class.java)

        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(
            context, reminderDate.toInt(),
            intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.cancel(pendingIntent)
    }

    companion object {
        const val EXTRA_TITLE = "title"
        const val EXTRA_REMINDER_DATE = "reminder_date"
    }
}
