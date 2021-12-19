package com.example.projectandroidsuite.ui.scaffold

import androidx.navigation.NavHostController
import com.example.projectandroidsuite.ui.navigation.ProjectsScreens
import com.example.projectandroidsuite.ui.navigation.navigateTo

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

    fun navigateToSearch() = navigateTo(
        navController = navController,
        screen = ProjectsScreens.Search
    )

    fun navigateToLogin() = navigateTo(
        navController = navController,
        screen = ProjectsScreens.Login
    )

}