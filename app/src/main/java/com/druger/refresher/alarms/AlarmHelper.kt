package com.druger.refresher.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent

import com.druger.refresher.App
import com.druger.refresher.models.ModelTask

class AlarmHelper(
    private var alarmManager: AlarmManager,
    private var app: App
) {

    fun setAlarm(task: ModelTask) {
        val intent = Intent(app, AlarmReceiver::class.java).apply {
            putExtra(EXTRA_TITLE, task.title)
            putExtra(EXTRA_REMINDER_DATE, task.reminderDate)
        }

        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(
            app,
            task.reminderDate.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.set(AlarmManager.RTC_WAKEUP, task.reminderDate, pendingIntent)
    }

    fun removeAlarm(reminderDate: Long) {
        val intent = Intent(app, AlarmReceiver::class.java)

        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(
            app, reminderDate.toInt(),
            intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.cancel(pendingIntent)
    }

    companion object {
        const val EXTRA_TITLE = "title"
        const val EXTRA_REMINDER_DATE = "reminder_date"
    }
}
