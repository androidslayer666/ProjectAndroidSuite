package com.example.projectandroidsuite.ui.createeditscreens.project

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.domain.utils.Failure
import com.example.domain.utils.Success
import com.example.projectandroidsuite.ui.parts.*
import com.example.projectandroidsuite.ui.parts.customitems.ButtonRow
import com.example.projectandroidsuite.ui.utils.PickerType
import com.example.projectandroidsuite.ui.utils.makeToast

@Composable
fun ProjectCreateEditScreen(
    viewModel: ProjectCreateEditViewModelNew,
    projectId: Int?,
    navigateBack: () -> Unit
) {


    if (projectId != null) viewModel.setProject(projectId)


    var showResponsiblePicker by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showTeamPicker by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsState()


    when {
        uiState.projectInputState.isTitleEmpty == true -> makeToast(
            "Please enter project title",
            LocalContext.current
        )
        uiState.projectInputState.isTeamEmpty == true -> makeToast(
            "Please choose project team",
            LocalContext.current
        )
        uiState.projectInputState.isResponsibleEmpty == true -> makeToast(
            "Please enter project responsible",
            LocalContext.current
        )
        uiState.projectInputState.serverResponse is Success -> {
            viewModel.clearInput()
            navigateBack()
        }
        uiState.projectInputState.serverResponse is Failure -> {
            viewModel.clearInput()
            makeToast(
                (uiState.projectInputState.serverResponse as Failure<String>).reason,
                LocalContext.current
            )
        }
        uiState.projectDeletionStatus is Success -> {
            makeToast(
                (uiState.projectDeletionStatus as Success<String>).value,
                LocalContext.current
            )
            navigateBack()
        }
    }

    Box {
        Column {


            TitleInput(
                text = uiState.title,
                onInputChange = { text -> viewModel.setTitle(text) }
            )

            DescriptionInput(
                text = uiState.description,
                onInputChange = { text -> viewModel.setDescription(text) }
            )

            ChooseUser(
                responsible = uiState.responsible,
                onClick = { showResponsiblePicker = true }
            )

            ChooseTeam(
                team = uiState.chosenUserList,
                onClick = { showTeamPicker = true }
            )

            ChooseProjectStatus(
                status = uiState.projectStatus,
                setStatus = { status -> viewModel.setProjectStatus(status) }
            )

            ButtonRow(
                onSubmit = { if (projectId == 0) viewModel.createProject() else viewModel.updateProject() },
                onDelete = { showDeleteDialog = true },
                onDismiss = { navigateBack() }

            )
        }
        if (showResponsiblePicker) {
            TeamPickerDialog(
                list = uiState.users,
                //onSubmit = { },
                onClick = { user ->
                    run {
                        viewModel.setResponsible(user)
                        showResponsiblePicker = false
                    }
                },
                closeDialog = { showResponsiblePicker = false },
                pickerType = PickerType.SINGLE,
                searchString = uiState.userSearchQuery,
                onSearchChanged = { query -> viewModel.setUserSearch(query) }
            )
        }
        if (showTeamPicker) {
            TeamPickerDialog(
                list = uiState.users,
                //onSubmit = { viewModel.updateChosenUsers() },
                onClick = { user ->
                    viewModel.addOrRemoveUser(user)
                },
                closeDialog = { showTeamPicker = false },
                pickerType = PickerType.MULTIPLE,
                searchString = uiState.userSearchQuery,
                onSearchChanged = { query -> viewModel.setUserSearch(query) }
            )
        }
        if (showDeleteDialog) {

            ConfirmationDialog(
                text = "Do you want to delete the message?",
                onSubmit = {
                    viewModel.deleteProject()
                },
                { showDeleteDialog = false })
        }
    }
}
