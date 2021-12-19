package com.example.projectandroidsuite.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.projectandroidsuite.newui.ProjectTheme
import com.example.projectandroidsuite.ui.navigation.ProjectNavHost
import com.example.projectandroidsuite.ui.scaffold.CustomScaffold

@Composable
fun ProjectAppCompose() {

    val navController = rememberNavController()

    ProjectTheme {
        CustomScaffold(
            navController = navController,
            viewModel = hiltViewModel(),
            ) {
            ProjectNavHost(
                loginViewModel = hiltViewModel(),
                navController = navController,
            )
        }
    }
}
