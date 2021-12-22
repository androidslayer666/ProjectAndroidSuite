package com.example.projectandroidsuite.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.projectandroidsuite.ui.createeditscreens.message.MessageCreateEditScreen
import com.example.projectandroidsuite.ui.createeditscreens.milestone.MilestoneCreateEditScreen
import com.example.projectandroidsuite.ui.createeditscreens.project.ProjectCreateEditScreen
import com.example.projectandroidsuite.ui.createeditscreens.subtask.SubtaskCreateEditScreen
import com.example.projectandroidsuite.ui.createeditscreens.task.TaskCreateEditScreen
import com.example.projectandroidsuite.ui.loginpage.LoginPage
import com.example.projectandroidsuite.ui.loginpage.LoginViewModel
import com.example.projectandroidsuite.ui.mainscreen.MainScreen
import com.example.projectandroidsuite.ui.navigation.ArgumentLists.messageList
import com.example.projectandroidsuite.ui.navigation.ArgumentLists.milestoneList
import com.example.projectandroidsuite.ui.navigation.ArgumentLists.projectDetailList
import com.example.projectandroidsuite.ui.navigation.ArgumentLists.projectList
import com.example.projectandroidsuite.ui.navigation.ArgumentLists.subtaskList
import com.example.projectandroidsuite.ui.navigation.ArgumentLists.taskDetailList
import com.example.projectandroidsuite.ui.navigation.ArgumentLists.taskList
import com.example.projectandroidsuite.ui.navigation.ArgumentStrings.idString
import com.example.projectandroidsuite.ui.navigation.ArgumentStrings.messageId
import com.example.projectandroidsuite.ui.navigation.ArgumentStrings.messageString
import com.example.projectandroidsuite.ui.navigation.ArgumentStrings.milestoneId
import com.example.projectandroidsuite.ui.navigation.ArgumentStrings.milestoneString
import com.example.projectandroidsuite.ui.navigation.ArgumentStrings.projectDetailString
import com.example.projectandroidsuite.ui.navigation.ArgumentStrings.projectId
import com.example.projectandroidsuite.ui.navigation.ArgumentStrings.subtaskString
import com.example.projectandroidsuite.ui.navigation.ArgumentStrings.taskDetailString
import com.example.projectandroidsuite.ui.navigation.ArgumentStrings.taskId
import com.example.projectandroidsuite.ui.projectdetailpage.ProjectDetailPage
import com.example.projectandroidsuite.ui.search.SearchScreen
import com.example.projectandroidsuite.ui.taskdetailpage.TaskDetailPage



@Composable
fun ProjectNavHost(
    loginViewModel: LoginViewModel,
    navController: NavHostController,
) {

    NavHost(
        navController = navController,
        startDestination = if (loginViewModel.checkAuthentication()) ProjectsScreens.Main.name
        else ProjectsScreens.Login.name
    ) {

        composable(route = ProjectsScreens.Login.name) {
            LoginPage(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }

        composable(route = ProjectsScreens.Main.name) {
            MainScreen(
                navController = navController
            )
        }

        composable(
            route = ProjectsScreens.ProjectDetail.name + idString,
            arguments = projectDetailList,
        ) { entry ->
            ProjectDetailPage(
                projectId = entry.arguments?.getInt(ArgumentStrings.id),
                viewModel = hiltViewModel(),
                navController = navController
            )
        }

        composable(
            route = ProjectsScreens.TaskDetail.name + idString,
            arguments = taskDetailList,
        ) { entry ->
            TaskDetailPage(
                taskId = entry.arguments?.getInt(ArgumentStrings.id),
                viewModel = hiltViewModel(),
                navController = navController
            )
        }

        composable(
            route = ProjectsScreens.CreateEditMilestone.name + milestoneString,
            arguments = milestoneList,
        ) { entry ->
            MilestoneCreateEditScreen(
                milestoneId = entry.arguments?.getInt(milestoneId),
                projectId = entry.arguments?.getInt(projectId),
                viewModel = hiltViewModel(),
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = ProjectsScreens.CreateEditMessage.name + messageString,
            arguments = messageList,
        ) { entry ->
            MessageCreateEditScreen(
                messageId = entry.arguments?.getInt(messageId),
                projectId = entry.arguments?.getInt(projectId),
                viewModel = hiltViewModel(),
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = ProjectsScreens.CreateEditProject.name + projectDetailString,
            arguments = projectList,
        ) { entry ->
            ProjectCreateEditScreen(
                projectId = entry.arguments?.getInt(projectId),
                viewModel = hiltViewModel(),
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = ProjectsScreens.CreateEditTask.name + taskDetailString,
            arguments = taskList,
        ) { entry ->
            TaskCreateEditScreen(
                taskId = entry.arguments?.getInt(taskId),
                viewModel = hiltViewModel(),
                navigateBack = { navController.popBackStack() }
            )
        }
        composable(
            route = ProjectsScreens.CreateSubTask.name + subtaskString,
            arguments = subtaskList,
        ) { entry ->
            SubtaskCreateEditScreen(
                taskId = entry.arguments?.getInt(taskId),
                viewModel = hiltViewModel(),
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = ProjectsScreens.Search.name
        ) {
            SearchScreen(
                viewModel = hiltViewModel(),
                navController = navController,
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}
