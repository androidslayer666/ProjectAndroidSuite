package com.example.projectandroidsuite.ui.loginpage

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.domain.repository.Failure
import com.example.domain.repository.Success
import com.example.projectandroidsuite.logic.makeToast
import com.example.projectandroidsuite.ui.parts.*
import com.example.projectandroidsuite.ui.taskdetailpage.ListSubtask
import com.example.projectandroidsuite.ui.taskdetailpage.TaskDetailViewModel


@Composable
fun TaskDetailPage(
    taskId: Int?,
    viewModel: TaskDetailViewModel,
    toggleSearch: () -> Unit,
    navController: NavHostController,
    toggleFab: Pair<Boolean, () -> Unit>,
    onTaskDeletedOrEdited: (String) -> Unit = { }
) {

    if (taskId != null && viewModel.taskId.value == null) {
        viewModel.setCurrentTask(taskId)
    }

    val context = LocalContext.current

    var state by remember { mutableStateOf(0) }
    val titles = listOf("Messages", "Files", "Subtasks")

    var showUpdateTaskDialog by remember { mutableStateOf(false) }
    var expandButtons by remember { mutableStateOf(false) }
    var showCreateSubtaskDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }


    val task by viewModel.currentTask.observeAsState()
    val comments by viewModel.listComments.observeAsState()
    val files by viewModel.filesForTask.observeAsState(listOf())
    val taskDeletionStatus by viewModel.taskDeletionStatus.observeAsState()

    ScaffoldTopBarWrapper(
        false,
        {},
        toggleSearch
    ) {
        Column(if (toggleFab.first || expandButtons)
            Modifier.clickable {
                expandButtons = false
                toggleFab.second()
            } else Modifier.padding()) {
            Row() {
                Column(Modifier.weight(5F)) {
                    DetailHeaderWrapper(task?.title, task?.description, null, task?.responsibles)
                }
            }

            Column {
                TabRow(selectedTabIndex = state) {
                    titles.forEachIndexed { index, title ->
                        Tab(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(title)
                                    if (index == 2 && state ==2) IconButton(onClick = {
                                        showCreateSubtaskDialog = true
                                    }) {
                                        Icon(Icons.Default.Add,"")
                                    }
                                }
                            },
                            selected = state == index,
                            onClick = { state = index }
                        )
                    }
                }
                when (state) {
                    0 -> ListComments(
                        listComments = comments,
                        onReplyClick = { comment -> viewModel.addCommentToTask(comment) },
                        onDeleteClick = {comment -> viewModel.deleteComment(comment)})
                    1 -> ListFiles(listFiles = files)
                    2 -> ListSubtask(task?.subtasks ?: listOf())
                }
            }
        }
        Row() {
            Column(Modifier.weight(5F)) {

            }
            Column(Modifier.weight(1F)) {
                ExpandableButtons(
                    expandButtons,
                    { expandButtons = !expandButtons },
                    { showUpdateTaskDialog = true },
                    { showDeleteDialog = true },
                    viewModel.currentTask.value?.canEdit,
                    viewModel.currentTask.value?.canDelete
                )
            }
        }
        if (showUpdateTaskDialog) {
            CreateTaskDialog(
                hiltViewModel(),
                { showUpdateTaskDialog = false },
                viewModel.currentTask.value,
                { string -> makeToast(string, context) }
            )
        }
        if (taskDeletionStatus != null) {
            when (taskDeletionStatus) {
                is Success -> {
                    Log.d("delete task", taskDeletionStatus.toString())
                    onTaskDeletedOrEdited((taskDeletionStatus as Success<String>).value)
                    viewModel.resetState()
                    navController.popBackStack()

                    //navigate("Projects")
                }
                is Failure -> {
                    Log.d("delete task", taskDeletionStatus.toString())
                    makeToast((taskDeletionStatus as Failure<String>).reason, context)
                }
            }
        }

        if (showCreateSubtaskDialog) {
            task?.id?.let {
                CreateSubtaskDialog(
                    taskId = it,
                    viewModel = hiltViewModel(),
                    closeDialog = { showCreateSubtaskDialog = false })
            }
        }

        if (showDeleteDialog) {
            ConfirmationDialog(
                text = "Do you want to delete the task?",
                onSubmit = { viewModel.deleteTask() },
                { showDeleteDialog = false })
        }
    }
}


