package com.example.projectandroidsuite.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.annotation.CallSuper
import com.example.domain.interactor.task.GetOverdueTasks
import com.example.domain.utils.log
import com.example.projectandroidsuite.ui.utils.Constants.DAILY_TASK_NOTIFICATION
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DailyNotificationAlarmReceiver: HiltBroadcastReceiver() {

    @Inject
    lateinit var getOverdueTasks: GetOverdueTasks

    override fun onReceive(context: Context, intent: Intent) {
        // this thing solving the Hilt injection bug
        super.onReceive(context, intent)

        log("received an alarm")

        //checking if we get the right alarm
        if (intent.action == DAILY_TASK_NOTIFICATION) {

            createNotificationChannelDailyTaskNotifications(context, Context.NOTIFICATION_SERVICE)

            CoroutineScope(IO).launch {
                getOverdueTasks().collectLatest {
                    log(it)
                }
            }
            createNotification(context, 5)
        }
    }
}

// wrapper for solving Hilt injection bug. Somehow 2.36 doesn't solve the bug for current project.
abstract class HiltBroadcastReceiver : BroadcastReceiver() {
    @CallSuper
    override fun onReceive(context: Context, intent: Intent) {}
}