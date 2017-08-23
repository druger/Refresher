package com.druger.refresher.alarms;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.druger.refresher.App;
import com.druger.refresher.R;
import com.druger.refresher.activities.MainActivity;

/**
 * Created by druger on 29.09.2015.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String title = intent.getStringExtra("title");
        long timeStamp = intent.getLongExtra("time_stamp", 0);
        int color = intent.getIntExtra("color", 0);

        Intent resultIntent = new Intent(context, MainActivity.class);

        if (App.isActivityVisible()){
            resultIntent = intent;
        }

        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) timeStamp, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        setupNotification(context, title, (int) timeStamp, color, pendingIntent);
    }

    private void setupNotification(Context context, String title, int timeStamp, int color, PendingIntent pendingIntent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("Reminder");
        builder.setContentText(title);
        builder.setColor(ContextCompat.getColor(context, color));
        builder.setSmallIcon(R.drawable.ic_check_white_48dp);

        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) context.
                getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(timeStamp, notification);
    }
}
