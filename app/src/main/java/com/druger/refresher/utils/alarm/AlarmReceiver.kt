package com.druger.refresher.utils.alarm

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.app.NotificationCompat
import com.druger.refresher.R
import com.druger.refresher.presentation.main.MainActivity

class AlarmReceiver : BroadcastReceiver() {

    @OptIn(ExperimentalAnimationApi::class)
    override fun onReceive(context: Context, intent: Intent) {

        val title = intent.getStringExtra(AlarmHelper.EXTRA_TITLE)
        val reminderDate = intent.getLongExtra(AlarmHelper.EXTRA_REMINDER_DATE, 0)

        val resultIntent = Intent(context, MainActivity::class.java)

        resultIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, reminderDate.toInt(),
            resultIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        title?.let { setupNotification(context, it, reminderDate.toInt(), pendingIntent) }
    }

    private fun setupNotification(
        context: Context,
        title: String,
        timeStamp: Int,
        pendingIntent: PendingIntent
    ) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setContentTitle(CONTENT_TITLE)
            setContentText(title)
            setSmallIcon(R.drawable.ic_done)
            setDefaults(Notification.DEFAULT_ALL)
            setContentIntent(pendingIntent)
        }
        val notification = builder.build().apply { flags = Notification.FLAG_AUTO_CANCEL }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(timeStamp, notification)
    }

    companion object {
        private const val CHANNEL_ID = "1"
        private const val CONTENT_TITLE = "Reminder"
    }
}
