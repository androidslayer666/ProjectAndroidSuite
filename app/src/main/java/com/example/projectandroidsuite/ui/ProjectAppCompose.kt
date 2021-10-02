package com.example.projectandroidsuite.ui

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController

@Composable
fun ProjectAppCompose(isAuthenticated: Boolean) {

    ProjectTheme() {
        val navController = rememberNavController()
        ProjectNavHost(
            navController = navController,
            isAuthenticated = isAuthenticated
        )
    }
}
