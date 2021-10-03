package com.example.projectandroidsuite.ui.projectdetailpage

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.domain.repository.Failure
import com.example.domain.repository.Success
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.logic.makeToast
import com.example.projectandroidsuite.ui.parts.*
import com.example.projectandroidsuite.ui.scaffold.CustomScaffold

@Composable
fun ProjectDetailPage(
    projectId: Int?,
    viewModel: ProjectDetailViewModel,
    navController: NavHostController
) {

    if (projectId != null && viewModel.projectId.value == null) {
        viewModel.setProject(projectId)
    }

    val context = LocalContext.current

    var state by remember { mutableStateOf(0) }
    val titles = listOf("Milestones", "Messages", "Files")

    var showUpdateProjectDialog by remember { mutableStateOf(false) }
    var showCreateMessageDialog by remember { mutableStateOf(false) }
    var showCreateMilestoneDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val project by viewModel.currentProject.observeAsState()
    val listTasksAndMilestones by viewModel.taskAndMilestones.observeAsState()
    val listMessages by viewModel.listDiscussions.observeAsState(null)
    val listFiles by viewModel.listFiles.observeAsState(listOf())
    val projectDeletionStatus by viewModel.projectDeletionStatus.observeAsState()

    CustomScaffold(navController = navController, viewModel = hiltViewModel()) {
        Column {
            Row {
                Column(Modifier.weight(5F)) {
                    DetailHeaderWrapper(
                        title = project?.title,
                        description = project?.description,
                        responsible = project?.responsible,
                        team = project?.team,
                        projectStatus = project?.status,
                        onEditClick = {
                            if (project?.canEdit == true) {
                                showUpdateProjectDialog = true
                            }
                        }
                    )
                }
            }
            Column {
                TabRow(selectedTabIndex = state) {
                    titles.forEachIndexed { index, title ->
                        Tab(
                            modifier = Modifier.height(50.dp),
                            text = {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(title)
                                    if (index == 1 && state == 1) IconButton(onClick = {
                                        showCreateMessageDialog = true
                                    }) {
                                        Image(
                                            painterResource(id = R.drawable.plus_circle_outline),
                                            ""
                                        )
                                    }
                                    if (index == 0 && state == 0) IconButton(onClick = {
                                        showCreateMilestoneDialog = true
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
                    0 -> ListTasksMilestones(
                        listTasksAndMilestones,
                        navController
                    ) { milestone -> viewModel.deleteMilestone(milestone) }
                    1 -> ListMessages(
                        listMessages,
                        { comment -> viewModel.addCommentToMessage(comment) },
                        { message -> viewModel.deleteMessage(message) },
                        { comment -> viewModel.deleteComment(comment) })
                    2 -> ListFiles(listFiles = listFiles)
                }
            }
        }

        if (showUpdateProjectDialog) {
            CreateUpdateProjectDialog(
                hiltViewModel(),
                { showUpdateProjectDialog = false },
                viewModel.currentProject.value,
                { string -> makeToast(string, context) },
                onDeleteClick = { if (project?.canDelete == true) showDeleteDialog = true }
            )
        }
        if (projectDeletionStatus != null) {
            when (projectDeletionStatus) {
                is Success -> {
                    Log.d("deleteProject", projectDeletionStatus.toString())
                    makeToast((projectDeletionStatus as Success<String>).value, context)
                    viewModel.resetState()
                    navController.navigate("Projects")
                }
                is Failure -> {
                    Log.d("deleteProject", projectDeletionStatus.toString())
                    makeToast((projectDeletionStatus as Failure<String>).reason, context)
                }
            }
        }

        if (showCreateMessageDialog) {
            if (projectId != null) {
                CreateMessageDialog(
                    projectId = projectId,
                    viewModel = hiltViewModel(),
                    closeDialog = { showCreateMessageDialog = false }
                )
            }
        }

        if (showCreateMilestoneDialog) {
            if (projectId != null) {
                CreateMilestoneDialog(
                    projectId = projectId,
                    viewModel = hiltViewModel(),
                    closeDialog = { showCreateMilestoneDialog = false
                        navController.popBackStack()
                    }
                )
            }
        }

        if (showDeleteDialog) {
            ConfirmationDialog(
                text = "Do you want to delete the project?",
                onSubmit = {
                    viewModel.deleteProject()
                    navController.popBackStack()
                },
                { showDeleteDialog = false })
        }
    }
}


