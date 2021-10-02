package com.example.projectandroidsuite.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import com.example.projectandroidsuite.ui.loginpage.LoginPage
import com.example.projectandroidsuite.ui.projectpage.ProjectsPage
import com.example.projectandroidsuite.ui.taskdetailpage.TaskDetailPage
import com.example.projectandroidsuite.ui.projectdetailpage.ProjectDetailPage


enum class ProjectsScreens() {
    Projects(), Login()
}

@Composable
fun ProjectNavHost(
    navController: NavHostController,
    isAuthenticated: Boolean
) {

    NavHost(
        navController = navController,
        startDestination = if (isAuthenticated) ProjectsScreens.Projects.name
        else ProjectsScreens.Login.name
    ) {

        composable(route = ProjectsScreens.Login.name) {
            LoginPage(hiltViewModel(), navController)
        }

        composable(route = ProjectsScreens.Projects.name) {
            ProjectsPage(navController)
        }

        composable(route = ProjectsScreens.Projects.name + "taskTab") {
            ProjectsPage(navController, 1)
        }

        composable(
            route = "project/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                }
            ),
        ) { entry ->
            ProjectDetailPage(
                projectId=entry.arguments?.getInt("id"),
                viewModel=hiltViewModel(),
                navController= navController
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
            TaskDetailPage(
                taskId = entry.arguments?.getInt("id"),
                viewModel = hiltViewModel(),
                navController = navController
            )
        }
    }
}

