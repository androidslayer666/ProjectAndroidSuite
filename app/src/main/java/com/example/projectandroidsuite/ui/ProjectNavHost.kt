package com.example.projectandroidsuite.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import com.example.domain.SessionManager
import com.example.projectandroidsuite.ui.loginpage.LoginPage
import com.example.projectandroidsuite.ui.projectpage.ProjectsPage
import com.example.projectandroidsuite.ui.loginpage.TaskDetailPage
import com.example.projectandroidsuite.ui.projectdetailpage.ProjectDetailPage


enum class ProjectsScreens() {
    Projects(), Login()
}

@Composable
fun ProjectNavHost(
    navController: NavHostController,
    sessionManager: SessionManager,
    toggleSearch: () -> Unit,
    toggleFab: Pair<Boolean, () -> Unit>,
    onDeletedOrEdited: (String) -> Unit = { }
) {

    NavHost(
        navController = navController,
        startDestination = if (sessionManager.isAuthenticated()) ProjectsScreens.Projects.name
        else ProjectsScreens.Login.name
    ) {

        composable(route = ProjectsScreens.Login.name) {
            LoginPage(hiltViewModel(), navController)
        }

        composable(route = ProjectsScreens.Projects.name) {
            ProjectsPage(navController, toggleSearch, toggleFab)
        }

        composable(
            route = "projects/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                }
            ),
        ) { entry ->
            ProjectDetailPage(
                projectId=entry.arguments?.getInt("id"),
                viewModel=hiltViewModel(),
                navController= navController,
                toggleSearch= toggleSearch,
                toggleFab= toggleFab,
                onProjectDeletedOrEdited= { string -> onDeletedOrEdited(string) }
            )
        }

        composable(
            route = "tasks/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                }
            ),
        ) { entry ->
            //if(showFilters) toggleFilter()
            TaskDetailPage(
                taskId = entry.arguments?.getInt("id"),
                viewModel = hiltViewModel(),
                toggleSearch = toggleSearch,
                navController = navController,
                toggleFab = toggleFab,
                onTaskDeletedOrEdited = { string -> onDeletedOrEdited(string) }
            )
        }
    }
}

