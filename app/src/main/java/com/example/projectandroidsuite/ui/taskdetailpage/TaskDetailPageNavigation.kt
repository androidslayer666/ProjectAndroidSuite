package com.example.projectandroidsuite.ui.taskdetailpage

import androidx.navigation.NavHostController
import com.example.projectandroidsuite.ui.ProjectsScreens
import com.example.projectandroidsuite.ui.navigateTo

class TaskDetailPageNavigation(
    private val navController: NavHostController,
    private val taskId: Int?
) {

    fun navigateToSubtaskCreationScreen() =
        navigateTo(
            navController = navController,
            screen = ProjectsScreens.CreateSubTask,
            taskId ?: 0
        )

    fun navigateToTaskEditScreen() =
        navigateTo(
            navController = navController,
            ProjectsScreens.CreateEditTask,
            taskId ?: 0
        )

}