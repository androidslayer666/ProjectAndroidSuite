package com.example.projectandroidsuite.ui.projectdetailpage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.database.entities.MessageEntity
import com.example.database.entities.MilestoneEntity
import com.example.domain.model.Message
import com.example.domain.model.Milestone
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.logic.makeToast
import com.example.projectandroidsuite.logic.showResultToast
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
    var showEditMessageDialog by remember { mutableStateOf(false) }
    var showCreateMilestoneDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var rememberMilestone by remember { mutableStateOf(Milestone(id = 0)) }
    var rememberMessage by remember { mutableStateOf(Message(id = 0)) }
    var showEditMilestoneDialog by remember { mutableStateOf(false) }
    var showDeleteMilestoneDialog by remember { mutableStateOf(false) }
    var showDeleteMessageDialog by remember { mutableStateOf(false) }


    val project by viewModel.currentProject.observeAsState()
    val listTasksAndMilestones by viewModel.taskAndMilestones.observeAsState()
    val listMessages by viewModel.listDiscussions.observeAsState(null)
    val listFiles by viewModel.listFiles.observeAsState(listOf())
    val projectDeletionStatus by viewModel.projectDeletionStatus.observeAsState()
    val milestoneDeletionStatus by viewModel.milestoneDeletionStatus.observeAsState()
    val messageDeletionStatus by viewModel.messageDeletionStatus.observeAsState()
    val commentDeletionStatus by viewModel.commentDeletionStatus.observeAsState()

    showResultToast(
        result = milestoneDeletionStatus,
        onSuccess = {
            showEditMilestoneDialog = false
            showDeleteMilestoneDialog = false
                    },
        context = context
    )

    showResultToast(
        result = commentDeletionStatus,
        onSuccess = {},
        context = context
    )

    showResultToast(
        result = messageDeletionStatus,
        onSuccess = {showDeleteMessageDialog = false
                    showEditMessageDialog = false
                    },
        context = context
    )

    showResultToast(
        result = projectDeletionStatus,
        onSuccess = {
            viewModel.resetState()
            navController.navigate("Projects")
        },
        context = context
    )

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
                            modifier = Modifier
                                .height(50.dp)
                                .background(MaterialTheme.colors.primary),
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
                    ) { milestone ->
                        if (milestone != null) {
                            rememberMilestone = milestone
                            showEditMilestoneDialog = true
                        }
                    }
                    1 -> ListMessages(
                        listMessages = listMessages,
                        onReplyClick = { comment -> viewModel.addCommentToMessage(comment) },
                        onEditMessageClick = { message -> rememberMessage = message
                            showEditMessageDialog = true
                        },
                        onDeleteCommentClick = { comment -> viewModel.deleteComment(comment) })
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


        if (showCreateMessageDialog) {
            if (projectId != null) {
                CreateMessageDialog(
                    projectId = projectId,
                    viewModel = hiltViewModel(),
                    closeDialog = { showCreateMessageDialog = false },
                    onMessageDeletedOrEdited = { string -> makeToast(string, context) }
                )
            }
        }

        if (showEditMessageDialog) {
            if (projectId != null) {
                CreateMessageDialog(
                    message = rememberMessage,
                    projectId = projectId,
                    viewModel = hiltViewModel(),
                    closeDialog = { showEditMessageDialog = false },
                    onMessageDeletedOrEdited = { string -> makeToast(string, context) },
                    onDeleteClick = {showDeleteMessageDialog = true}
                )
            }
        }

        if (showCreateMilestoneDialog) {
            if (projectId != null) {
                CreateMilestoneDialog(
                    projectId = projectId,
                    viewModel = hiltViewModel(),
                    closeDialog = { showCreateMilestoneDialog = false },
                    onMilestoneDeletedOrEdited = { string -> makeToast(string, context) }
                )
            }
        }

        if (showEditMilestoneDialog) {
            if (projectId != null) {
                CreateMilestoneDialog(
                    milestone = rememberMilestone,
                    projectId = projectId,
                    viewModel = hiltViewModel(),
                    closeDialog = { showEditMilestoneDialog = false },
                    onMilestoneDeletedOrEdited = { string -> makeToast(string, context) },
                    onDeleteClick = { showDeleteMilestoneDialog = true }
                )
            }
        }



        if (showDeleteDialog) {
            ConfirmationDialog(
                text = "Do you want to delete the project?",
                onSubmit = {
                    viewModel.deleteProject()
                },
                { showDeleteDialog = false })
        }

        if (showDeleteMilestoneDialog) {
            ConfirmationDialog(
                text = "Do you want to delete the project?",
                onSubmit = {
                    viewModel.deleteMilestone(rememberMilestone)
                },
                { showDeleteMilestoneDialog = false })
        }

        if (showDeleteMessageDialog) {
            ConfirmationDialog(
                text = "Do you want to delete the message?",
                onSubmit = {
                    viewModel.deleteMessage(rememberMessage)
                },
                { showDeleteMessageDialog = false })
        }

    }
}


