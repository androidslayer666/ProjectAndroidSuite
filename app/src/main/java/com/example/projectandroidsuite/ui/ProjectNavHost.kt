package com.example.projectandroidsuite.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.projectandroidsuite.ui.loginpage.LoginPage
import com.example.projectandroidsuite.ui.loginpage.LoginViewModel
import com.example.projectandroidsuite.ui.projectdetailpage.ProjectDetailPage
import com.example.projectandroidsuite.ui.projectpage.ProjectsPage
import com.example.projectandroidsuite.ui.taskdetailpage.TaskDetailPage


enum class ProjectsScreens() {
    Projects, Login, Project, Task, CreateEditMilestone
}

@Composable
fun ProjectNavHost(
    navController: NavHostController,
    loginViewModel: LoginViewModel
) {
    NavHost(
        navController = navController,
        startDestination = if (loginViewModel.checkAuthentication()) ProjectsScreens.Projects.name
        else ProjectsScreens.Login.name
    ) {

        composable(route = ProjectsScreens.Login.name) {
            LoginPage(hiltViewModel(), navController)
        }

        composable(route = ProjectsScreens.Projects.name) {
            ProjectsPage(navController)
        }

        composable(
            route = ProjectsScreens.Project.name + "/{id}",
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
            route = ProjectsScreens.Task.name + "/{id}",
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

        composable(
            route = ProjectsScreens.CreateEditMilestone.name + "?milestoneId={milestoneId}",
            arguments = listOf(
                navArgument("milestoneId") {
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

