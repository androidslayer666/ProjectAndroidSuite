package com.example.projectandroidsuite.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

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
    primary = Color(0xFF435F7D),
    secondary = Color(0xFFD54127),
    primaryVariant = Color(0xFFABCDFF),
    error = Color(0xFFA43F6C),
    background = Color(0xFFFFFCF9)
)

private val DarkColors = darkColors(

)