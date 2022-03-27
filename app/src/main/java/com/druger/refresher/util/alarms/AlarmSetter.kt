package com.druger.refresher.util.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.druger.refresher.App
import com.druger.refresher.domain.task.model.ModelTask
import javax.inject.Inject

class AlarmSetter : BroadcastReceiver() {

    @Inject
    lateinit var alarmHelper: AlarmHelper

    override fun onReceive(context: Context, intent: Intent) {
        App.getApplicationComponent().inject(this)

        val tasks: MutableList<ModelTask> = mutableListOf()

        for (task in tasks) {
            if (task.reminderDate.toInt() != 0) {
                alarmHelper.setAlarm(task)
            }
        }
    }
}
