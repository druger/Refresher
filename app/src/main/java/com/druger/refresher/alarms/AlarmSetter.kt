package com.druger.refresher.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.druger.refresher.App
import com.druger.refresher.models.ModelTaskNew
import javax.inject.Inject

/**
* Created by druger on 29.09.2015.
*/
class AlarmSetter : BroadcastReceiver() {

    @Inject
    lateinit var alarmHelper: AlarmHelper

    override fun onReceive(context: Context, intent: Intent) {
        App.getApplicationComponent().inject(this)

        val tasks: MutableList<ModelTaskNew> = mutableListOf()

        for (task in tasks) {
            if (task.reminderDate.toInt() != 0) {
                alarmHelper.setAlarm(task)
            }
        }
    }
}
