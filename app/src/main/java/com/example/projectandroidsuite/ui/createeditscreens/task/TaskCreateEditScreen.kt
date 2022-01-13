package com.example.projectandroidsuite.ui.createeditscreens.task

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import com.example.domain.filters.project.ProjectStatus
import com.example.domain.filters.task.TaskStatus
import com.example.domain.model.TaskPriority
import com.example.domain.utils.Failure
import com.example.domain.utils.Success
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.ui.createeditscreens.ScreenMode
import com.example.projectandroidsuite.ui.parts.*
import com.example.projectandroidsuite.ui.parts.customitems.ButtonRow
import com.example.projectandroidsuite.ui.parts.dialogs.TeamPickerDialog
import com.example.projectandroidsuite.ui.parts.inputs.ChooseTeam
import com.example.projectandroidsuite.ui.parts.inputs.DatePickerRow
import com.example.projectandroidsuite.ui.parts.inputs.DescriptionInput
import com.example.projectandroidsuite.ui.parts.inputs.TitleInput
import com.example.projectandroidsuite.ui.taskdetailpage.ProjectPickerDialog
import com.example.projectandroidsuite.ui.utils.PickerType
import com.example.projectandroidsuite.ui.utils.hideKeyboardOnLoseFocus
import com.example.projectandroidsuite.ui.utils.makeToast


@Composable
fun TaskCreateEditScreen(
    viewModel: TaskCreateEditViewModelNew,
    taskId: Int?,
    navigateBack: () -> Unit
) {

    if (taskId != 0) LaunchedEffect(key1 = taskId) { viewModel.setTask(taskId!!) }

    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsState()
    val listUsersFlow by viewModel.userListState.collectAsState(listOf())

    var showDatePicker by remember { mutableStateOf(false) }
    var showTeamPicker by remember { mutableStateOf(false) }
    var showProjectPicker by remember { mutableStateOf(false) }
    var showMilestonePicker by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }


    if (uiState.taskInputState.serverResponse is Success) {
        LaunchedEffect(key1 = uiState.taskInputState) {
            viewModel.clearInput()
            navigateBack()
        }
    }
    if (uiState.taskInputState.serverResponse is Failure) {
        LaunchedEffect(key1 = uiState.taskInputState) {
            makeToast("Something went wrong with the server request", context)
        }
    }
    if (uiState.taskDeletionStatus is Success) {
        LaunchedEffect(key1 = uiState.taskDeletionStatus) {
            makeToast((uiState.taskDeletionStatus as Success<String>).value, context)
            //to skip the project details screen which is empty now
            navigateBack()
            navigateBack()
        }
    }

    Box {

        TaskCreateEditScreenBody(
            uiState = uiState,
            setTitle = { text -> viewModel.setTitle(text) },
            setDescription = { text -> viewModel.setDescription(text) },
            setTaskStatus = { status -> viewModel.setTaskStatus(status) },
            setPriority = { taskPriority -> viewModel.setPriority(taskPriority) },
            createTask = { viewModel.createTask() },
            updateTask = { viewModel.updateTask() },
            showProjectPicker = { showProjectPicker = true },
            showMilestonePicker = { showMilestonePicker = true },
            showTeamPicker = { showTeamPicker = true },
            showDatePicker = { showDatePicker = true },
            showDeleteDialog = { showDeleteDialog = true },
            navigateBack = navigateBack
        )

        if (showProjectPicker) {
            ProjectPickerDialog(
                list = uiState.projectList,
                onSubmit = { showProjectPicker = false },
                onClick = { projectClicked -> viewModel.setProject(project = projectClicked) },
                closeDialog = { showProjectPicker = false },
                ifChooseResponsibleOrTeam = PickerType.SINGLE,
                searchString = uiState.projectSearchQuery,
                onSearchChanged = { query ->
                    viewModel.setStringForFilteringProjects(
                        query
                    )
                }
            )
        }

        if (showTeamPicker) {
            TeamPickerDialog(
                list = listUsersFlow,
                onClick = { user -> viewModel.addOrRemoveUser(user) },
                closeDialog = { showTeamPicker = false },
                pickerType = PickerType.MULTIPLE,
                searchString = uiState.userSearchQuery,
                onSearchChanged = { query -> viewModel.setUserSearch(query) }
            )
        }

        if (uiState.milestonesList != null ){
            if (showMilestonePicker && uiState.milestonesList?.isNotEmpty() == true) {
                DialogPickerMilestone(
                    list = uiState.milestonesList,
                    onClick = { milestone -> viewModel.setMilestone(milestone) },
                    closeDialog = { showMilestonePicker = false }
                )
            }
        }

        if (showDatePicker) {
            DatePicker(
                onDateSelected = { date -> viewModel.setDate(date) },
                onDismissRequest = { showDatePicker = !showDatePicker })
        }

        if (showDeleteDialog) {
            ConfirmationDialog(
                text = stringResource(R.string.do_you_want_to_delete_the_task),
                onSubmit = {
                    viewModel.deleteTask()
                },
                { showDeleteDialog = false })
        }

    }
}

@Composable
fun TaskCreateEditScreenBody(
    uiState: TaskCreateState,
    setTitle: (String) -> Unit = {},
    setDescription: (String) -> Unit = {},
    setTaskStatus: (TaskStatus) -> Unit = {},
    setPriority: (TaskPriority) -> Unit = {},
    createTask: () -> Unit = {},
    updateTask: () -> Unit = {},
    showProjectPicker: () -> Unit = {},
    showMilestonePicker: () -> Unit = {},
    showTeamPicker: () -> Unit = {},
    showDatePicker: () -> Unit = {},
    showDeleteDialog: () -> Unit = {},
    navigateBack: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current


    Column(modifier = Modifier.hideKeyboardOnLoseFocus(focusManager)) {

        TitleInput(
            text = uiState.title,
            onInputChange = { text -> setTitle(text) },
            textIsEmpty = uiState.taskInputState.isTitleEmpty
        )

        DescriptionInput(
            text = uiState.description,
            onInputChange = { text -> setDescription(text) }
        )

        ChooseTaskStatus(
            taskStatus = uiState.taskStatus,
            setStatus = { status -> setTaskStatus(status) }
        )

        ChooseTaskPriority(
            priority = uiState.priority,
            setPriority = { taskPriority -> setPriority(taskPriority) }
        )

        ChooseProject(
            onClick = showProjectPicker,
            project = uiState.project,
            projectIsEmpty = uiState.taskInputState.isProjectEmpty
        )

        ChooseTeam(
            team = uiState.chosenUserList,
            onClick = showTeamPicker,
            teamIsEmpty = uiState.taskInputState.isTeamEmpty
        )

        ChooseTaskMilestone(
            onClick = showMilestonePicker,
            milestone = uiState.milestone
        )

        DatePickerRow(
            toggleDatePicker = showDatePicker,
            endDate = uiState.endDate,
        )


        ButtonRow(
            onSubmit = {
                if (uiState.screenMode == ScreenMode.CREATE) createTask()
                else updateTask()
            },
            onDismiss = navigateBack,
            onDelete = showDeleteDialog,
            showDeleteOption = uiState.screenMode == ScreenMode.EDIT
        )
    }
}