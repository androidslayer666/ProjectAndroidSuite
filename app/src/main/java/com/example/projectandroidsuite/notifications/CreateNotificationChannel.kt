package com.example.projectandroidsuite.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

const val CHANNEL_ID = "Channel_01"

fun createNotificationChannelDailyTaskNotifications(context: Context, notificationService: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "notification_name"
        val descriptionText = "notification_description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(notificationService) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}