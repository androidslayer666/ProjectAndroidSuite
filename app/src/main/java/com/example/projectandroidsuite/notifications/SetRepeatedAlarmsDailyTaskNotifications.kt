package com.example.projectandroidsuite.notifications

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.domain.utils.log
import com.example.projectandroidsuite.ui.utils.Constants.DAILY_TASK_NOTIFICATION
import java.util.*

const val REQUEST_CODE = 100

fun setRepeatedAlarmsDailyTaskNotifications(
    context: Context
){

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    // Creating the pending intent
    val intent = Intent(context, DailyNotificationAlarmReceiver::class.java)
    intent.action = DAILY_TASK_NOTIFICATION
    val pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    //setting time
    val calendar = Calendar.getInstance()
    calendar.run {
        timeInMillis = System.currentTimeMillis()
        set(Calendar.HOUR_OF_DAY, 10)
        set(Calendar.MINUTE, 1)
    }

    //postpone for a day if the current time is later than the alarm
    var startUpTime = calendar.timeInMillis;
    if (System.currentTimeMillis() > startUpTime) {
        startUpTime += AlarmManager.INTERVAL_DAY;
    }

    // setting alarm
    alarmManager.setInexactRepeating(
        AlarmManager.RTC,
        startUpTime,
        AlarmManager.INTERVAL_DAY,
        pendingIntent
    )
}