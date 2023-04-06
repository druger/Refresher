package com.druger.refresher.utils.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmSetter : BroadcastReceiver() {

    @Inject
    lateinit var alarmHelper: AlarmHelper

    override fun onReceive(context: Context, intent: Intent) {
        val tasks: MutableList<com.druger.refresher.domain.model.TaskModel> = mutableListOf()

        for (task in tasks) {
            if (task.reminderDate.toInt() != 0) {
                alarmHelper.setAlarm(task)
            }
        }
    }
}
