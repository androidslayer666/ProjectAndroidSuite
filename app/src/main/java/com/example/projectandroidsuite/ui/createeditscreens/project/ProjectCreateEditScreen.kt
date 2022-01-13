package com.example.projectandroidsuite.ui.createeditscreens.project

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.domain.filters.project.ProjectStatus
import com.example.domain.utils.Failure
import com.example.domain.utils.Success
import com.example.domain.utils.log
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.ui.ProjectTheme
import com.example.projectandroidsuite.ui.createeditscreens.ScreenMode
import com.example.projectandroidsuite.ui.parts.*
import com.example.projectandroidsuite.ui.parts.customitems.ButtonRow
import com.example.projectandroidsuite.ui.parts.dialogs.TeamPickerDialog
import com.example.projectandroidsuite.ui.parts.inputs.ChooseTeam
import com.example.projectandroidsuite.ui.parts.inputs.ChooseUser
import com.example.projectandroidsuite.ui.parts.inputs.DescriptionInput
import com.example.projectandroidsuite.ui.parts.inputs.TitleInput
import com.example.projectandroidsuite.ui.utils.PickerType
import com.example.projectandroidsuite.ui.utils.hideKeyboardOnLoseFocus
import com.example.projectandroidsuite.ui.utils.makeToast

@Composable
fun ProjectCreateEditScreen(
    viewModel: ProjectCreateEditViewModel,
    projectId: Int?,
    navigateBack: () -> Unit
) {

    if (projectId != null)
        LaunchedEffect(key1 = projectId) { viewModel.setProject(projectId) }

    val context = LocalContext.current
    var showResponsiblePicker by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showTeamPicker by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsState()

    log(uiState.projectInputState)


    if (uiState.projectInputState.serverResponse is Success) {
        viewModel.clearInput()
        navigateBack()
    }

    if (uiState.projectInputState.serverResponse is Failure) {
        LaunchedEffect(key1 = uiState.projectInputState.serverResponse) {
            viewModel.clearInput()
            makeToast(
                (uiState.projectInputState.serverResponse as Failure<String>).reason,
                context
            )
        }
    }

    if (uiState.projectDeletionStatus is Success) {
        LaunchedEffect(key1 = uiState.projectDeletionStatus) {
            makeToast(
                (uiState.projectDeletionStatus as Success<String>).value,
                context
            )
            navigateBack()
            navigateBack()
        }
    }


    Box {

        ProjectCreateEditScreenBody(
            uiState = uiState,
            setTitle = { text -> viewModel.setTitle(text) },
            setDescription = { text -> viewModel.setDescription(text) },
            setProjectStatus = { status -> viewModel.setProjectStatus(status) },
            createProject = { viewModel.createProject() },
            updateProject = { viewModel.updateProject() },
            showResponsiblePicker = { showResponsiblePicker = true },
            showTeamPicker = { showTeamPicker = true },
            showDeleteDialog = { showDeleteDialog = true },
            navigateBack = navigateBack
        )

        if (showResponsiblePicker) {
            TeamPickerDialog(
                list = uiState.users,
                onClick = { user -> viewModel.setResponsible(user) },
                closeDialog = { showResponsiblePicker = false },
                pickerType = PickerType.SINGLE,
                searchString = uiState.userSearchQuery,
                onSearchChanged = { query -> viewModel.setUserSearch(query) }
            )
        }

        if (showTeamPicker) {
            TeamPickerDialog(
                list = uiState.users,
                onClick = { user -> viewModel.addOrRemoveUser(user) },
                closeDialog = { showTeamPicker = false },
                pickerType = PickerType.MULTIPLE,
                searchString = uiState.userSearchQuery,
                onSearchChanged = { query -> viewModel.setUserSearch(query) }
            )
        }

        if (showDeleteDialog) {
            ConfirmationDialog(
                text = stringResource(R.string.do_you_want_to_delete_the_project),
                onSubmit = {
                    viewModel.deleteProject()
                },
                { showDeleteDialog = false })
        }
    }
}

@Composable
fun ProjectCreateEditScreenBody(
    uiState: ProjectCreateState,
    setTitle: (String) -> Unit = {},
    setDescription: (String) -> Unit = {},
    setProjectStatus: (ProjectStatus) -> Unit = {},
    createProject: () -> Unit = {},
    updateProject: () -> Unit = {},
    showResponsiblePicker: () -> Unit = {},
    showTeamPicker: () -> Unit = {},
    showDeleteDialog: () -> Unit = {},
    navigateBack: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current


    Column(modifier = Modifier.hideKeyboardOnLoseFocus(focusManager)) {

        TitleInput(
            text = uiState.title,
            onInputChange = { text -> setTitle(text) },
            textIsEmpty = uiState.projectInputState.isTitleEmpty
        )

        DescriptionInput(
            text = uiState.description,
            onInputChange = { text -> setDescription(text) }
        )

        ChooseUser(
            responsible = uiState.responsible,
            onClick = { showResponsiblePicker() },
            userIsEmpty = uiState.projectInputState.isResponsibleEmpty
        )

        ChooseTeam(
            team = uiState.chosenUserList,
            onClick = { showTeamPicker() },
            teamIsEmpty = uiState.projectInputState.isTeamEmpty
        )

        ChooseProjectStatus(
            status = uiState.projectStatus,
            setStatus = { status -> setProjectStatus(status) }
        )

        ButtonRow(
            onSubmit = {
                if (uiState.screenMode == ScreenMode.CREATE) createProject()
                else updateProject()
            },
            onDelete = showDeleteDialog,
            onDismiss = { navigateBack() },
            showDeleteOption = uiState.screenMode == ScreenMode.EDIT
        )
    }
}

@Preview
@Composable
fun CreateProjectScreenPreview() {
    ProjectTheme {
        ProjectCreateEditScreenBody(
            uiState = ProjectCreateState(
                screenMode = ScreenMode.CREATE,
                projectStatus = ProjectStatus.STOPPED,
                title = "Title",
                description = "Description",
                responsible = null
            )) {
        }
    }
}