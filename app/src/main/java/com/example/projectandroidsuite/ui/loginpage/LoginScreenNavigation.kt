package com.example.projectandroidsuite.ui.loginpage

import androidx.navigation.NavHostController
import com.example.projectandroidsuite.ui.navigation.ProjectsScreens
import com.example.projectandroidsuite.ui.navigation.navigateTo

class LoginScreenNavigation(
    private val navController: NavHostController
) {

    fun navigateToMainScreen() = navigateTo(
        navController = navController,
        screen = ProjectsScreens.Main
    )
}