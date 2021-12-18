package com.example.projectandroidsuite.ui.projectdetailpage

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.ui.ProjectsScreens
import com.example.projectandroidsuite.ui.navigateTo
import com.example.projectandroidsuite.ui.parts.*
import com.example.projectandroidsuite.ui.scaffold.CustomScaffold

@Composable
fun ProjectDetailPage(
    projectId: Int?,
    viewModel: ProjectDetailViewModel,
    navController: NavHostController
) {

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = projectId) {
        if (projectId != null && uiState.projectId == null) {
            viewModel.setProject(projectId)
        }
    }

    var state by rememberSaveable { mutableStateOf(0) }

    var showDeleteDialog by remember { mutableStateOf(false) }

    val navigation = ProjectDetailPageNavigation(navController, projectId)

    CustomScaffold(navController = navController, viewModel = hiltViewModel()) {
        Column {
            DetailHeaderWrapper(
                title = uiState.currentProject?.title,
                description = uiState.currentProject?.description,
                responsible = uiState.currentProject?.responsible,
                team = uiState.currentProject?.team,
                projectStatus = uiState.currentProject?.status,
                onEditClick = {
                    if (uiState.currentProject?.canEdit == true) {
                        navigateTo(
                            navController = navController,
                            screen = ProjectsScreens.CreateEditProject,
                            projectId ?: 0
                        )
                    }
                }
            )

            Column {
                ProjectDetailTabRow(
                    state = state,
                    changeState = { newState -> state = newState },
                    navigation = navigation
                )

                when (state) {
                    0 -> ListTasksMilestones(
                        listTasksAndMilestones = uiState.taskAndMilestones,
                        navigateToTaskId = { id ->
                            navigation.navigateToTaskDetailScreen(id)
                        },
                        onEditMilestone = { milestone ->
                            navigation.navigateToMilestoneEditingScreen(milestone?.id ?: 0)
                        })
                    1 -> ListMessages(
                        listMessages = uiState.messages,
                        onReplyClick = { comment -> viewModel.addCommentToMessage(comment) },
                        onEditMessageClick = { message ->
                            navigation.navigateToMessageEditingScreen(message.id)
                        },
                        onDeleteCommentClick = { comment -> viewModel.deleteComment(comment) })
                    2 -> ListFiles(listFiles = uiState.listFiles ?: listOf())
                }
            }
        }

        if (showDeleteDialog) {
            ConfirmationDialog(
                text = stringResource(R.string.do_you_want_to_delete_the_project),
                onSubmit = { viewModel.deleteProject() },
                closeDialog = { showDeleteDialog = false })
        }
    }
}

