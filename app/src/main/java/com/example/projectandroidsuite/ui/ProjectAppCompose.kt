package com.example.projectandroidsuite.ui

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController

@Composable
fun ProjectAppCompose() {

    ProjectTheme() {
        val navController = rememberNavController()
        ProjectNavHost(
            navController = navController,
            loginViewModel = hiltViewModel()
        )
    }
}
