package com.druger.refresher.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent

import com.druger.refresher.App
import com.druger.refresher.models.ModelTask

/**
* Created by druger on 29.09.2015.
*/
class AlarmHelper(private var alarmManager: AlarmManager,
                  private var app: App) {

    fun setAlarm(task: ModelTask) {
        val intent = Intent(app, AlarmReceiver::class.java)
        intent.putExtra("title", task.title)
        intent.putExtra("time_stamp", task.timeStamp)
        intent.putExtra("color", task.getPriorityColor())

        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(app,
                task.timeStamp.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.set(AlarmManager.RTC_WAKEUP, task.date, pendingIntent)
    }

    fun removeAlarm(taskTimeStamp: Long) {
        val intent = Intent(app, AlarmReceiver::class.java)

        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(app, taskTimeStamp.toInt(),
                intent, PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.cancel(pendingIntent)
    }
}
