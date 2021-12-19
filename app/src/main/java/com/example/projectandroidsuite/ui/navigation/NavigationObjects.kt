package com.example.projectandroidsuite.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.projectandroidsuite.ui.navigation.ArgumentStrings.id
import com.example.projectandroidsuite.ui.navigation.ArgumentStrings.messageId
import com.example.projectandroidsuite.ui.navigation.ArgumentStrings.milestoneId
import com.example.projectandroidsuite.ui.navigation.ArgumentStrings.projectId
import com.example.projectandroidsuite.ui.navigation.ArgumentStrings.taskId


object ArgumentLists {
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

object ArgumentStrings {
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
