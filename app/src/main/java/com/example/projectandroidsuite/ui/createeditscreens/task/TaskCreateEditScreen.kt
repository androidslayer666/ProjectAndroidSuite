package com.example.projectandroidsuite.ui.createeditscreens.task

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.domain.utils.Failure
import com.example.domain.utils.Success
import com.example.projectandroidsuite.ui.createeditscreens.ScreenMode
import com.example.projectandroidsuite.ui.parts.*
import com.example.projectandroidsuite.ui.parts.customitems.ButtonRow
import com.example.projectandroidsuite.ui.taskdetailpage.ProjectPickerDialog
import com.example.projectandroidsuite.ui.utils.PickerType
import com.example.projectandroidsuite.ui.utils.makeToast


@Composable
fun TaskCreateEditScreen(
    viewModel: TaskCreateEditViewModelNew,
    taskId: Int?,
    navigateBack: () -> Unit
) {

    if (taskId != 0) LaunchedEffect(key1 = taskId) {viewModel.setTask(taskId!!)}


    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsState()
    val listUsersFlow by viewModel.userListState.collectAsState(listOf())

    var showDatePicker by remember { mutableStateOf(false) }
    var showTeamPicker by remember { mutableStateOf(false) }
    var showProjectPicker by remember { mutableStateOf(false) }
    var showMilestonePicker by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }



    when {
        uiState.taskInputState.isTitleEmpty == true -> LaunchedEffect(key1 = uiState.taskInputState) {
            makeToast("Please enter project title", context)
        }
        uiState.taskInputState.isTeamEmpty == true -> LaunchedEffect(key1 = uiState.taskInputState) {
            makeToast("Please choose project", context)
        }
        uiState.taskInputState.isProjectEmpty == true -> LaunchedEffect(key1 = uiState.taskInputState) {
            makeToast("Please enter task responsible", context)
        }
        uiState.taskInputState.serverResponse is Success -> {
            LaunchedEffect(key1 = uiState.taskInputState) {
                viewModel.clearInput()
                navigateBack()
            }
        }
        uiState.taskInputState.serverResponse is Failure -> {
            LaunchedEffect(key1 = uiState.taskInputState) {
                makeToast("Something went wrong with the server request", context)
            }
        }
        uiState.taskDeletionStatus is Success -> {
            LaunchedEffect(key1 = uiState.taskDeletionStatus) {
                makeToast((uiState.taskDeletionStatus as Success<String>).value, context)
                //to skip the empty project details screen
                navigateBack()
                navigateBack()
            }
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

            ChooseTaskStatus(
                taskStatus = uiState.taskStatus,
                setStatus = { status -> viewModel.setTaskStatus(status) }
            )

            ChooseTaskPriority(
                priority = uiState.priority,
                setPriority = { taskPriority -> viewModel.setPriority(taskPriority) }
            )

            ChooseProject(
                onClick = { showProjectPicker = true },
                project = uiState.project
            )

            ChooseTeam(
                team = uiState.chosenUserList,
                onClick = { showTeamPicker = true }
            )

            ChooseTaskMilestone(
                onClick = { showMilestonePicker = true },
                milestone = uiState.milestone
            )

            DatePickerRow(
                toggleDatePicker = { showDatePicker = true },
                endDate = uiState.endDate,
            )


            ButtonRow(
                onSubmit = {
                    if (uiState.screenMode == ScreenMode.CREATE) viewModel.createTask()
                    else viewModel.updateTask()
                           },
                onDismiss = navigateBack,
                onDelete = { showDeleteDialog = true }
            )
        }

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
                //onSubmit = { showTeamPicker = false },
                onClick = { user -> viewModel.addOrRemoveUser(user) },
                closeDialog = { showTeamPicker = false },
                pickerType = PickerType.MULTIPLE,
                searchString = uiState.userSearchQuery,
                onSearchChanged = { query -> viewModel.setUserSearch(query) }
            )
        }

        uiState.milestonesList.let {
            if (showMilestonePicker && it?.isNotEmpty() == true) {
                DialogPickerMilestone(
                    list = it,
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
                text = "Do you want to delete the task?",
                onSubmit = {
                    viewModel.deleteTask()
                },
                { showDeleteDialog = false })
        }

    }
}