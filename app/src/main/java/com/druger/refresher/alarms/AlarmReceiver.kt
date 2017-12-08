package com.druger.refresher.alarms

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat

import com.druger.refresher.App
import com.druger.refresher.R
import com.druger.refresher.activities.MainActivity

/**
 * Created by druger on 29.09.2015.
 */
class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val title = intent.getStringExtra("title")
        val timeStamp = intent.getLongExtra("time_stamp", 0)
        val color = intent.getIntExtra("color", 0)

        var resultIntent = Intent(context, MainActivity::class.java)

        if (App.isActivityVisible()) {
            resultIntent = intent
        }

        resultIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, timeStamp.toInt(),
                resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        setupNotification(context, title, timeStamp.toInt(), color, pendingIntent)
    }

    private fun setupNotification(context: Context, title: String, timeStamp: Int, color: Int,
                                  pendingIntent: PendingIntent) {
        val builder = NotificationCompat.Builder(context, "1")
        builder.setContentTitle("Reminder")
        builder.setContentText(title)
        builder.color = ContextCompat.getColor(context, color)
        builder.setSmallIcon(R.drawable.ic_check_white_48dp)

        builder.setDefaults(Notification.DEFAULT_ALL)
        builder.setContentIntent(pendingIntent)

        val notification = builder.build()
        notification.flags = Notification.FLAG_AUTO_CANCEL

        val notificationManager = context.
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(timeStamp, notification)
    }
}
