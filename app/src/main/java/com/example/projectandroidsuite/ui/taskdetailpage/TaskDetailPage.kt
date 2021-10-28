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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.domain.utils.Failure
import com.example.domain.utils.Success
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.ui.parts.ConfirmationDialog
import com.example.projectandroidsuite.ui.parts.DetailHeaderWrapper
import com.example.projectandroidsuite.ui.parts.ListComments
import com.example.projectandroidsuite.ui.parts.ListFiles
import com.example.projectandroidsuite.ui.scaffold.CustomScaffold
import com.example.projectandroidsuite.ui.utils.makeToast


@Composable
fun TaskDetailPage(
    taskId: Int?,
    viewModel: TaskDetailViewModel,
    navController: NavHostController
) {

    if (taskId != null && viewModel.taskId.value == null) {
        viewModel.setCurrentTask(taskId)
    }



    val context = LocalContext.current

    var state by remember { mutableStateOf(0) }
    val titles = listOf( "Subtasks","Comments", "Files" )

    var showUpdateTaskDialog by remember { mutableStateOf(false) }
    var showCreateSubtaskDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showUpdateStatusDialog by remember { mutableStateOf(false) }

    val task by viewModel.currentTask.collectAsState(null)
    val comments by viewModel.listComments.collectAsState(listOf())
    val files by viewModel.filesForTask.collectAsState(listOf())
    val taskDeletionStatus by viewModel.taskDeletionStatus.collectAsState()
    val taskMilestone by viewModel.taskMilestone.collectAsState(null)

    CustomScaffold(navController = navController, viewModel = hiltViewModel()) {
        Column {
            Row {
                Column(Modifier.weight(5F)) {
                    DetailHeaderWrapper(
                        title = task?.title,
                        description = task?.description,
                        responsible = null,
                        team = task?.responsibles,
                        endDate = task?.deadline,
                        taskStatus = task?.status,
                        project = task?.projectOwner?.title,
                        milestone = taskMilestone?.title,
                        priority = task?.priority,
                        onEditClick = {
                            if (task?.canEdit == true) {
                                showUpdateTaskDialog = true
                            }
                        },
                        onStatusClicked = { showUpdateStatusDialog = true }
                    )
                }
            }

            Column {
                TabRow(selectedTabIndex = state) {
                    titles.forEachIndexed { index, title ->
                        Tab(
                            modifier = Modifier
                                .height(50.dp)
                                .background(MaterialTheme.colors.primary),
                            text = {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(title)
                                    if (index == 0 && state == 0) IconButton(onClick = {
                                        showCreateSubtaskDialog = true
                                    }) {
                                        Image(
                                            painterResource(id = R.drawable.plus_circle_outline),
                                            ""
                                        )
                                    }
                                }
                            },
                            selected = state == index,
                            onClick = { state = index }
                        )
                    }
                }
                when (state) {
                    0 -> ListSubtask(task?.subtasks ?: listOf())
                    1 -> ListComments(
                        listComments = comments,
                        onReplyClick = { comment -> viewModel.addCommentToTask(comment) },
                        onDeleteClick = { comment -> viewModel.deleteComment(comment) })
                    2 -> ListFiles(listFiles = files)
                }
            }
        }

        if (showUpdateTaskDialog) {
            CreateUpdateTaskDialog(
                viewModel = hiltViewModel(),
                closeDialog = { showUpdateTaskDialog = false },
                task = task,
                onTaskDeletedOrEdited = { string -> makeToast(string, context) },
                onDeleteClick = { if (task?.canDelete == true) showDeleteDialog = true }
            )
        }
        if (taskDeletionStatus != null) {
            when (taskDeletionStatus) {
                is Success -> {
                    //Log.d("delete task", taskDeletionStatus.toString())
                    showDeleteDialog = false
                    showUpdateTaskDialog = false
                    makeToast((taskDeletionStatus as Success<String>).value, context)
                    viewModel.resetState()
                    navController.popBackStack()
                }
                is Failure -> {
                    //Log.d("delete task", taskDeletionStatus.toString())
                    makeToast((taskDeletionStatus as Failure<String>).reason, context)
                }
            }
        }

        if (showCreateSubtaskDialog) {
            task?.id?.let {
                CreateSubtaskDialog(
                    taskId = it,
                    viewModel = hiltViewModel(),
                    closeDialog = { showCreateSubtaskDialog = false }) }
        }

        if (showDeleteDialog) {
            ConfirmationDialog(
                text = "Do you want to delete the task?",
                onSubmit = {
                    viewModel.deleteTask()
                },
                { showDeleteDialog = false })
        }

        if (showUpdateStatusDialog) {
            ConfirmationDialog(
                text = "Do you want to change the task status?",
                onSubmit = { viewModel.changeTaskStatus()
                    showUpdateStatusDialog = false},
                { showUpdateStatusDialog = false })
        }
    }
}


