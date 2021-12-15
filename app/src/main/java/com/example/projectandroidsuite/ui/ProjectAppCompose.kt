package com.example.projectandroidsuite.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.projectandroidsuite.newui.ProjectTheme

@Composable
fun ProjectAppCompose() {

    ProjectTheme {
        val navController = rememberNavController()
        ProjectNavHost(
            navController = navController,
            loginViewModel = hiltViewModel()
        )
    }
}
