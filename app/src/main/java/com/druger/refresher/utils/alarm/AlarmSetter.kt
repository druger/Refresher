package com.druger.refresher.utils.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.druger.refresher.App
import javax.inject.Inject

class AlarmSetter : BroadcastReceiver() {

    @Inject
    lateinit var alarmHelper: AlarmHelper

    override fun onReceive(context: Context, intent: Intent) {
        App.getApplicationComponent().inject(this)

        val tasks: MutableList<com.druger.refresher.domain.model.TaskModel> = mutableListOf()

        for (task in tasks) {
            if (task.reminderDate.toInt() != 0) {
                alarmHelper.setAlarm(task)
            }
        }
    }
}
