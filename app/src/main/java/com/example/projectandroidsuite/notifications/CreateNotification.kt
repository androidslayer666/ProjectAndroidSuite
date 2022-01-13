package com.example.projectandroidsuite.notifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.domain.utils.log
import com.example.projectandroidsuite.MainActivity
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.ui.notificationactivity.NotificationActivity
import com.example.projectandroidsuite.ui.utils.Constants.GET_FROM_NOTIFICATION

const val notificationId = 101

fun createNotification(
    context: Context,
    numberOfOverdueTasks: Int
) {

    log("creating notification")
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    intent.putExtra(GET_FROM_NOTIFICATION, true)

    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(context.getString(R.string.projects))
        .setContentText("You have $numberOfOverdueTasks overdue tasks")
        .setContentIntent(pendingIntent)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    with(NotificationManagerCompat.from(context)) {
        notify(
            notificationId,
            builder.build()
        )
    }
}