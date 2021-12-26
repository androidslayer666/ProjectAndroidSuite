package com.example.projectandroidsuite.ui.taskdetailpage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.ui.parts.ConfirmationDialog
import com.example.projectandroidsuite.ui.parts.DetailHeaderWrapper
import com.example.projectandroidsuite.ui.parts.lists.ListComments
import com.example.projectandroidsuite.ui.parts.lists.ListFiles


@Composable
fun TaskDetailPage(
    taskId: Int?,
    viewModel: TaskDetailViewModel,
    navController: NavHostController
) {

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = taskId) {
        if (taskId != null && uiState.taskId == null) {
            viewModel.setCurrentTask(taskId)
        }
    }

    var tabState by remember { mutableStateOf(0) }

    val navigation = TaskDetailPageNavigation( navController = navController, taskId)

    var showUpdateStatusDialog by remember { mutableStateOf(false) }

    Column {
        Row {
            Column(Modifier.weight(5F)) {
                //Log.d("TaskDetailPage", uiState.currentTask.toString())
                DetailHeaderWrapper(
                    title = uiState.currentTask?.title,
                    description = uiState.currentTask?.description,
                    responsible = null,
                    team = uiState.currentTask?.responsibles,
                    endDate = uiState.currentTask?.deadline,
                    taskStatus = uiState.currentTask?.status,
                    project = uiState.currentTask?.projectOwner?.title,
                    milestone = "",
                    priority = uiState.currentTask?.priority,
                    onEditClick = {
                        if (uiState.currentTask?.canEdit == true) {
                            navigation.navigateToTaskEditScreen()
                        }
                    },
                    onStatusClicked = { showUpdateStatusDialog = true }
                )
            }
        }

        Column {
            TaskTabRow(
                tabState = tabState,
                setTabState = { int -> tabState = int }
            ) {
                navigation.navigateToSubtaskCreationScreen()
            }
            when (tabState) {
                0 -> ListSubtask(uiState.currentTask?.subtasks ?: listOf())
                1 -> ListComments(
                    listComments = uiState.listComments,
                    onReplyClick = { comment -> viewModel.addCommentToTask(comment) },
                    onDeleteClick = { comment -> viewModel.deleteComment(comment) })
                2 -> ListFiles(listFiles = uiState.listFiles ?: listOf())
            }
        }
    }

    if (showUpdateStatusDialog) {
        ConfirmationDialog(
            text = "Do you want to change the task status?",
            onSubmit = { viewModel.changeTaskStatus() },
            closeDialog = { showUpdateStatusDialog = false })
    }

}

@Composable
fun TaskTabRow(
    tabState: Int,
    setTabState: (Int) -> Unit,
    navigateToCreateSubtask: () -> Unit
) {

    val titles = listOf("Subtasks", "Comments", "Files")

    TabRow(selectedTabIndex = tabState) {
        titles.forEachIndexed { index, title ->
            Tab(
                modifier = Modifier
                    .height(50.dp)
                    .background(MaterialTheme.colors.primary),
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(title)
                        if (index == 0 && tabState == 0) IconButton(onClick = {
                            navigateToCreateSubtask()
                        }) {
                            Image(
                                painterResource(id = R.drawable.plus_circle_outline),
                                ""
                            )
                        }
                    }
                },
                selected = tabState == index,
                onClick = { setTabState(index) }
            )
        }
    }
}
