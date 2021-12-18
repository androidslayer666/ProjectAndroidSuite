package com.example.projectandroidsuite.ui.scaffold

import androidx.navigation.NavHostController
import com.example.projectandroidsuite.ui.ProjectsScreens
import com.example.projectandroidsuite.ui.navigateTo

class ScaffoldNavigation(
    private val navController: NavHostController
) {
    fun navigateToCreateProject() = navigateTo(
        navController = navController,
        screen = ProjectsScreens.CreateEditProject,
        0
    )

    fun navigateToCreateTask() = navigateTo(
        navController = navController,
        screen = ProjectsScreens.CreateEditTask,
        0
    )

}