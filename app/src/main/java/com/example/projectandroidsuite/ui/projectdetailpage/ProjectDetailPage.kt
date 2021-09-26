package com.example.projectandroidsuite.ui.projectdetailpage

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

@Composable
fun ProjectDetailPage(
    projectId: Int?,
    viewModel: ProjectDetailViewModel,
    navController: NavHostController,
    toggleSearch: () -> Unit,
    toggleFab: Pair<Boolean, () -> Unit>,
    onProjectDeletedOrEdited: (String) -> Unit = { }
) {

    if (projectId != null && viewModel.projectId.value == null) {
        viewModel.setProject(projectId)
    }

    //Log.d("deleteProject", "Recompose ProjectDetailPage")

    val context = LocalContext.current

    var state by remember { mutableStateOf(0) }
    val titles = listOf("Milestones", "Messages", "Files")

    var showUpdateProjectDialog by remember { mutableStateOf(false) }
    var expandButtons by remember { mutableStateOf(false) }
    var showCreateMessageDialog by remember { mutableStateOf(false) }
    var showCreateMilestoneDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val project by viewModel.currentProject.observeAsState()
    val listTasksAndMilestones by viewModel.taskAndMilestones.observeAsState()
    val listMessages by viewModel.listDiscussions.observeAsState(null)
    val listFiles by viewModel.listFiles.observeAsState(listOf())
    val projectDeletionStatus by viewModel.projectDeletionStatus.observeAsState()

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
                    DetailHeaderWrapper(
                        project?.title,
                        project?.description,
                        project?.responsible,
                        project?.team
                    )
                }
            }

            Column {
                TabRow(selectedTabIndex = state) {
                    titles.forEachIndexed { index, title ->
                        Tab(

                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(title)
                                    if (index == 1 && state == 1) IconButton(onClick = {
                                        showCreateMessageDialog = true
                                    }) {
                                        Icon(Icons.Default.Add, "")
                                    }
                                    if (index == 0 && state == 0) IconButton(onClick = {
                                        showCreateMilestoneDialog = true
                                    }) {
                                        Icon(Icons.Default.Add, "")
                                    }
                                }
                            },
                            selected = state == index,
                            onClick = { state = index }
                        )
                    }
                }
                when (state) {
                    0 -> ListTasksMilestones(listTasksAndMilestones, navController)
                    1 -> ListMessages(listMessages) { comment ->
                        viewModel.addCommentToMessage(
                            comment
                        )
                    }
                    2 -> ListFiles(listFiles = listFiles)
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
                    { showUpdateProjectDialog = true },
                    { showDeleteDialog = true })
            }
        }
        if (showUpdateProjectDialog) {
            CreateProjectDialog(
                hiltViewModel(),
                { showUpdateProjectDialog = false },
                viewModel.currentProject.value,
                { string -> onProjectDeletedOrEdited(string) }
            )
        }
        if (projectDeletionStatus != null) {
            when (projectDeletionStatus) {
                is Success -> {
                    Log.d("deleteProject", projectDeletionStatus.toString())
                    onProjectDeletedOrEdited((projectDeletionStatus as Success<String>).value)
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
                    closeDialog = { showCreateMessageDialog = false })
            }
        }
        if (showCreateMilestoneDialog) {
            if (projectId != null) {
                CreateMilestoneDialog(
                    projectId = projectId,
                    viewModel = hiltViewModel(),
                    closeDialog = { showCreateMilestoneDialog = false })
            }
        }


        if (showDeleteDialog) {
            ConfirmationDialog(
                text = "Do you want to delete the project?",
                onSubmit = { viewModel.deleteProject() },
                { showDeleteDialog = false })
        }
    }
}


