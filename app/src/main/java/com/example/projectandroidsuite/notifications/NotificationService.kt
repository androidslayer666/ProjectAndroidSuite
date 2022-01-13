package com.example.projectandroidsuite.notifications

import android.app.Service
import android.content.Intent

import android.os.IBinder
import com.example.domain.utils.log


class NotificationService : Service() {

    override fun onCreate() {
        super.onCreate()
        log("Service is created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        setRepeatedAlarmsDailyTaskNotifications(this)
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}