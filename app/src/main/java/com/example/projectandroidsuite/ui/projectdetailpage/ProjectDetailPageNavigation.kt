package com.example.projectandroidsuite.ui.projectdetailpage

import androidx.navigation.NavHostController
import com.example.projectandroidsuite.ui.navigation.ProjectsScreens
import com.example.projectandroidsuite.ui.navigation.navigateTo


class ProjectDetailPageNavigation(
    private val navController: NavHostController,
    private val projectId: Int?
) {
    // 0 in "ids" parameters indicates creation mode of the screen
    fun navigateToMilestoneCreationScreen() = navigateTo(
        navController = navController,
        screen = ProjectsScreens.CreateEditMilestone,
        0,
        projectId ?: 0
    )

    // 0 in "ids" parameters indicates creation mode of the screen
    fun navigateToMessageCreationScreen() = navigateTo(
        navController = navController,
        screen = ProjectsScreens.CreateEditMessage,
        0,
        projectId ?: 0
    )

    fun navigateToMilestoneEditingScreen(milestoneId: Int) =
        navigateTo(
            navController = navController,
            screen = ProjectsScreens.CreateEditMilestone,
            milestoneId,
            projectId ?: 0
        )

    fun navigateToMessageEditingScreen(messageId: Int) =
        navigateTo(
            navController = navController,
            screen = ProjectsScreens.CreateEditMessage,
            messageId,
            projectId ?: 0
        )
    fun navigateToTaskDetailScreen(taskId: Int) =
        navigateTo(
            navController = navController,
            screen = ProjectsScreens.CreateEditMessage,
            taskId
        )

}