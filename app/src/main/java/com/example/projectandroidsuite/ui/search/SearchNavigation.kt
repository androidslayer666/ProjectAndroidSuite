package com.example.projectandroidsuite.ui.search

import androidx.navigation.NavHostController
import com.example.projectandroidsuite.ui.navigation.ProjectsScreens
import com.example.projectandroidsuite.ui.navigation.navigateTo

class SearchNavigation(
    private val navController: NavHostController
) {

    fun navigateToProjectDetails(projectId: Int) = navigateTo(
        navController = navController,
        screen = ProjectsScreens.ProjectDetail,
        projectId
    )

    fun navigateToTaskDetails(taskId: Int) = navigateTo(
        navController = navController,
        screen = ProjectsScreens.TaskDetail,
        taskId
    )



}