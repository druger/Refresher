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

    @Override
    public void onReceive(Context context, Intent intent) {
        DBHelper dbHelper = new DBHelper(context);

        App.getAppComponent().inject(this);

        List<ModelTask> tasks = new ArrayList<>();
        tasks.addAll(dbHelper.query().getTasks(DBHelper.SELECTION_STATUS + " OR "
                        + DBHelper.SELECTION_STATUS,
                new String[]{Integer.toString(ModelTask.STATUS_CURRENT), Integer.toString(ModelTask.STATUS_OVERDUE)},
                DBHelper.TASK_DATE_COLUMN));

        for (ModelTask task : tasks) {
            if (task.getDate() != 0){
                alarmHelper.setAlarm(task);
            }
        }
    }
}
