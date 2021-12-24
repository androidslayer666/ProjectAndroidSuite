package com.example.projectandroidsuite.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.projectandroidsuite.ui.navigation.ProjectNavHost
import com.example.projectandroidsuite.ui.scaffold.CustomScaffold

@Composable
fun ProjectAppCompose() {

    val navController = rememberNavController()
    var showScaffold by rememberSaveable { mutableStateOf(true) }

    ProjectTheme {
        CustomScaffold(
            navController = navController,
            viewModel = hiltViewModel(),
            showScaffold = showScaffold
            ) {
            ProjectNavHost(
                loginViewModel = hiltViewModel(),
                navController = navController,
                setShowScaffold = { bool -> showScaffold = bool}
            )
        }
    }
}