package com.example.projectandroidsuite.ui.navigation

import android.util.Log
import androidx.navigation.NavHostController

fun navigateTo(navController: NavHostController, screen: ProjectsScreens, vararg ids: Int?) {
    Log.d("navigateTo", "$screen    ${ids[0]}")
    when (screen) {
        ProjectsScreens.CreateEditMessage -> navController.navigate(
            ProjectsScreens.CreateEditMessage.name +
                    "?${ArgumentStrings.messageId}=${ids[0]}&${ArgumentStrings.projectId}=${ids[1]}"
        )

        ProjectsScreens.CreateEditMilestone -> navController.navigate(
            ProjectsScreens.CreateEditMilestone.name +
                    "?${ArgumentStrings.milestoneId}=${ids[0]}&${ArgumentStrings.projectId}=${ids[1]}"
        )

        ProjectsScreens.CreateEditProject -> navController.navigate(
            ProjectsScreens.CreateEditProject.name +
                    "?${ArgumentStrings.projectId}=${ids[0]}"
        )

        ProjectsScreens.CreateEditTask -> navController.navigate(
            ProjectsScreens.CreateEditTask.name +
                    "?${ArgumentStrings.taskId}=${ids[0]}"
        )

        ProjectsScreens.ProjectDetail -> navController.navigate(
            ProjectsScreens.ProjectDetail.name +
                    "/${ids[0]}"
        )

        ProjectsScreens.TaskDetail -> navController.navigate(
            ProjectsScreens.TaskDetail.name +
                    "/${ids[0]}"
        )

        ProjectsScreens.CreateSubTask -> navController.navigate(
            ProjectsScreens.CreateSubTask.name +
                    "?${ArgumentStrings.id}=${ids[0]}"
        )

        ProjectsScreens.Search -> navController.navigate(
            ProjectsScreens.Search.name
        )

        ProjectsScreens.Login -> navController.navigate(
            ProjectsScreens.Login.name
        )

        ProjectsScreens.Main -> navController.navigate(
            ProjectsScreens.Main.name
        )
    }
}
