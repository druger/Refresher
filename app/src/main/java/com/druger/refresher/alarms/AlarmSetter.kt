package com.druger.refresher.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.druger.refresher.App
import com.druger.refresher.database.RealmHelper
import com.druger.refresher.models.ModelTask
import javax.inject.Inject

/**
* Created by druger on 29.09.2015.
*/
class AlarmSetter : BroadcastReceiver() {

    @Inject
    lateinit var alarmHelper: AlarmHelper
    @Inject
    lateinit var realmHelper: RealmHelper


    override fun onReceive(context: Context, intent: Intent) {
        App.getAppComponent().inject(this)

        val tasks: MutableList<ModelTask> = mutableListOf()
        tasks.addAll(realmHelper.tasksByAnyStatus)

        for (task in tasks) {
            if (task.date.toInt() != 0) {
                alarmHelper.setAlarm(task)
            }
        }
    }
}
