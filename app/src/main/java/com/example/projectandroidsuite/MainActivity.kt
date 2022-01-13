package com.example.projectandroidsuite

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import com.example.projectandroidsuite.notifications.NotificationService
import com.example.projectandroidsuite.ui.ProjectAppCompose
import com.example.projectandroidsuite.ui.loginpage.LoginViewModel
import com.example.projectandroidsuite.ui.utils.Constants
import com.example.projectandroidsuite.ui.utils.GlobalUiState
import com.example.projectandroidsuite.ui.utils.LocalGlobalUiState
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // If the activity created from notifications, the main screen will show list overdue tasks.
        val getFromOverdueTasksNotification =
            intent.getBooleanExtra(Constants.GET_FROM_NOTIFICATION, false)

        val authenticated = loginViewModel.checkAuthentication()


        setContent {
            // Providing composition with info regarding authentication and if the intent was from notification
            CompositionLocalProvider(
                LocalGlobalUiState provides
                        GlobalUiState(
                            getFromOverdueTasksNotification,
                            authenticated
                        )
            ) {
                ProjectAppCompose()
            }
        }

        //launch notification service if authenticated
        if (authenticated)
            Intent(this, NotificationService::class.java).also { intent ->
                startService(intent)
            }
    }
}