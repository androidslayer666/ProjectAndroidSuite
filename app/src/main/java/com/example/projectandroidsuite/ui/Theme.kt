package com.example.projectandroidsuite.ui

import android.graphics.Color.parseColor
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.core.graphics.toColor
import androidx.core.graphics.toColorLong
import com.example.projectandroidsuite.R


@Composable
fun ProjectTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colors = if (darkTheme) DarkColors else LightColors,
        /*...*/
        content = content
    )
}

private val LightColors = lightColors(
    primary = Color(0xFF10446c),
    secondary = Color(0xFFeb7304),
    primaryVariant = Color(0xFF4B4B4B),
    error = Color(0xFFA43F6C),
    background = Color(0xFFFFFCF9),
    onPrimary = Color(0xFFCECECE),
    onBackground = Color(0xFF383838)
)

private val DarkColors = darkColors(

)