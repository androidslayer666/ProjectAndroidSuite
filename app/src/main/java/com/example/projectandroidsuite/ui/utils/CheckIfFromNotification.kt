package com.example.projectandroidsuite.ui.utils

import androidx.compose.runtime.compositionLocalOf

data class GlobalUiState(
    val fromNotification: Boolean = false,
    val authenticated: Boolean = false
    )

val LocalGlobalUiState = compositionLocalOf { GlobalUiState() }