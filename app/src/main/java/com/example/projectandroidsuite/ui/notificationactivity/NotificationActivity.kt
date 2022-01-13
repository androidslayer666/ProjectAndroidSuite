package com.example.projectandroidsuite.ui.notificationactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import com.example.projectandroidsuite.MainActivity
import com.example.projectandroidsuite.ui.utils.BackPressedHandler

class NotificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            NotificationLanding()
        }
    }
}

@Composable
fun NotificationLanding() {

    val context = LocalContext.current

    BackPressedHandler(true) {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(context,intent, null)
    }
    Text("Notification")
}