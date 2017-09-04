package com.druger.refresher.alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;

import com.druger.refresher.App;
import com.druger.refresher.models.ModelTask;

/**
 * Created by druger on 29.09.2015.
 */
public class AlarmHelper {
    private App app;
    private AlarmManager alarmManager;

    public AlarmHelper(AlarmManager alarmManager, App app) {
        this.alarmManager = alarmManager;
        this.app = app;
    }

    public void setAlarm(ModelTask task){
        Intent intent = new Intent(app, AlarmReceiver.class);
        intent.putExtra("title", task.getTitle());
        intent.putExtra("time_stamp", task.getTimeStamp());
        intent.putExtra("color", task.getPriorityColor());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(app,
                (int) task.getTimeStamp(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, task.getDate(), pendingIntent);
    }

    public void removeAlarm(long taskTimeStamp){
        Intent intent = new Intent(app, AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(app, (int) taskTimeStamp,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }
}
