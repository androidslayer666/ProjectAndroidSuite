package com.example.projectandroidsuite.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.projectandroidsuite.ui.ArgumentLists.messageList
import com.example.projectandroidsuite.ui.ArgumentLists.milestoneList
import com.example.projectandroidsuite.ui.ArgumentLists.projectDetailList
import com.example.projectandroidsuite.ui.ArgumentLists.projectList
import com.example.projectandroidsuite.ui.ArgumentLists.subtaskList
import com.example.projectandroidsuite.ui.ArgumentLists.taskDetailList
import com.example.projectandroidsuite.ui.ArgumentLists.taskList
import com.example.projectandroidsuite.ui.ArgumentStrings.id
import com.example.projectandroidsuite.ui.ArgumentStrings.idString
import com.example.projectandroidsuite.ui.ArgumentStrings.messageId
import com.example.projectandroidsuite.ui.ArgumentStrings.messageString
import com.example.projectandroidsuite.ui.ArgumentStrings.milestoneId
import com.example.projectandroidsuite.ui.ArgumentStrings.milestoneString
import com.example.projectandroidsuite.ui.ArgumentStrings.projectDetailString
import com.example.projectandroidsuite.ui.ArgumentStrings.projectId
import com.example.projectandroidsuite.ui.ArgumentStrings.subtaskString
import com.example.projectandroidsuite.ui.ArgumentStrings.taskDetailString
import com.example.projectandroidsuite.ui.ArgumentStrings.taskId
import com.example.projectandroidsuite.ui.createeditscreens.message.MessageCreateEditScreen
import com.example.projectandroidsuite.ui.createeditscreens.milestone.MilestoneCreateEditScreen
import com.example.projectandroidsuite.ui.createeditscreens.project.ProjectCreateEditScreen
import com.example.projectandroidsuite.ui.createeditscreens.subtask.SubtaskCreateEditScreen
import com.example.projectandroidsuite.ui.createeditscreens.task.TaskCreateEditScreen
import com.example.projectandroidsuite.ui.loginpage.LoginPage
import com.example.projectandroidsuite.ui.loginpage.LoginViewModel
import com.example.projectandroidsuite.ui.mainscreen.ProjectsPage
import com.example.projectandroidsuite.ui.projectdetailpage.ProjectDetailPage
import com.example.projectandroidsuite.ui.taskdetailpage.TaskDetailPage


enum class ProjectsScreens() {
    Projects, Login, ProjectDetail, TaskDetail, CreateEditMilestone, CreateEditMessage, CreateEditProject, CreateEditTask, CreateSubTask
}

@Composable
fun ProjectNavHost(
    loginViewModel: LoginViewModel,
    navController: NavHostController,
) {

    NavHost(
        navController = navController,
        startDestination = if (loginViewModel.checkAuthentication()) ProjectsScreens.Projects.name
        else ProjectsScreens.Login.name
    ) {


        composable(route = ProjectsScreens.Login.name) {
            LoginPage(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }

        composable(route = ProjectsScreens.Projects.name) {
            ProjectsPage(
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
                messageId = entry.arguments?.getInt(milestoneId),
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
    }
}

fun navigateTo(navController: NavHostController, screen: ProjectsScreens, vararg ids: Int?) {
    when (screen) {
        ProjectsScreens.CreateEditMessage -> navController.navigate(
            ProjectsScreens.CreateEditMessage.name +
                    "?$messageId=${ids[0]}&$projectId=${ids[1]}"
        )

        ProjectsScreens.CreateEditMilestone -> navController.navigate(
            ProjectsScreens.CreateEditMilestone.name +
                    "?$milestoneId=${ids[0]}&$projectId=${ids[1]}"
        )

        ProjectsScreens.CreateEditProject -> navController.navigate(
            ProjectsScreens.CreateEditProject.name +
                    "?$projectId=${ids[0]}"
        )

        ProjectsScreens.CreateEditTask -> navController.navigate(
            ProjectsScreens.CreateEditTask.name +
                    "?$taskId=${ids[0]}"
        )

        ProjectsScreens.TaskDetail -> navController.navigate(
            ProjectsScreens.TaskDetail.name +
                    "?$id=${ids[0]}"
        )

        ProjectsScreens.CreateSubTask -> navController.navigate(
            ProjectsScreens.CreateSubTask.name +
                    "?$id=${ids[0]}"
        )

        else -> {}
    }
}

private object ArgumentLists {
    val milestoneList = listOf(
        navArgument(milestoneId) {
            type = NavType.IntType
            defaultValue = 0
        },
        navArgument(projectId) {
            type = NavType.IntType
            defaultValue = 0
        })
    val projectDetailList = listOf(
        navArgument(id) {
            type = NavType.IntType
        }
    )
    val taskDetailList = listOf(
        navArgument(id) {
            type = NavType.IntType
        }
    )
    val messageList = listOf(
        navArgument(messageId) {
            type = NavType.IntType
            defaultValue = 0
        },
        navArgument(projectId) {
            type = NavType.IntType
            defaultValue = 0
        })

    val projectList = listOf(
        navArgument(projectId) {
            type = NavType.IntType
            defaultValue = 0
        })
    val taskList = listOf(
        navArgument(taskId) {
            type = NavType.IntType
            defaultValue = 0
        })
    val subtaskList = listOf(
        navArgument(taskId) {
            type = NavType.IntType
            defaultValue = 0
        })

}

private object ArgumentStrings {
    const val milestoneId = "milestoneId"
    const val messageId = "messageId"
    const val projectId = "projectId"
    const val taskId = "taskId"
    const val id = "id"
    const val milestoneString = "?$milestoneId={$milestoneId}&$projectId={$projectId}"
    const val messageString = "?$messageId={$messageId}&$projectId={$projectId}"
    const val projectDetailString = "?$projectId={$projectId}"
    const val taskDetailString = "?$taskId={$taskId}"
    const val subtaskString = "?$taskId={$taskId}"
    const val idString = "/{id}"
}
