package com.druger.refresher.alarms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.druger.refresher.App;
import com.druger.refresher.database.DBHelper;
import com.druger.refresher.models.ModelTask;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by druger on 29.09.2015.
 */
public class AlarmSetter extends BroadcastReceiver {

    @Inject
    AlarmHelper alarmHelper;
    @Inject
    DBHelper dbHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        App.getAppComponent().inject(this);

        List<ModelTask> tasks = new ArrayList<>();
        tasks.addAll(dbHelper.getTasksByAnyStatus());

        for (ModelTask task : tasks) {
            if (task.getDate() != 0){
                alarmHelper.setAlarm(task);
            }
        }
    }
}
