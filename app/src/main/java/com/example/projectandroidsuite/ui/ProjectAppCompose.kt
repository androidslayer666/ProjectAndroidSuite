package com.example.projectandroidsuite.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.domain.SessionManager
import com.example.projectandroidsuite.logic.makeToast
import com.example.projectandroidsuite.ui.parts.CreateProjectDialog
import com.example.projectandroidsuite.ui.parts.CreateTaskDialog
import com.example.projectandroidsuite.ui.parts.customitems.ScaffoldFabWrapper
import com.example.projectandroidsuite.ui.search.SearchDialog

@Composable
fun ProjectAppCompose(sessionManager: SessionManager) {

    ProjectTheme() {
        val navController = rememberNavController()
        var showFabOptions by remember { mutableStateOf(false) }
        var showCreateTaskDialog by remember { mutableStateOf(false) }
        var showCreateProjectDialog by remember { mutableStateOf(false) }
        var showSearch by remember { mutableStateOf(false) }
        val context = LocalContext.current

        ScaffoldFabWrapper(
            showTaskDialog = {showCreateTaskDialog = true},
            showProjectDialog = {showCreateProjectDialog = true}
        ){
            ProjectNavHost(
                navController = navController,
                sessionManager = sessionManager,
                toggleSearch = { showSearch = !showSearch },
                toggleFab = Pair(showFabOptions, { showFabOptions = false }),
                onDeletedOrEdited = { string -> makeToast(string, context) }
            )

            if (showCreateProjectDialog) {
                CreateProjectDialog(
                    viewModel = hiltViewModel(),
                    closeDialog = { showCreateProjectDialog = false },
                    onSuccessProjectCreation = { string -> makeToast(string, context) })
            }

            if (showCreateTaskDialog) {
                CreateTaskDialog(
                    viewModel = hiltViewModel(),
                    closeDialog = { showCreateTaskDialog = false },
                    onTaskDeletedOrEdited = { string -> makeToast(string, context) })

            }
            if (showSearch) {
                SearchDialog(
                    viewModel = hiltViewModel(),
                    closeDialog = { showSearch = false })
            }
        }
    }
}
