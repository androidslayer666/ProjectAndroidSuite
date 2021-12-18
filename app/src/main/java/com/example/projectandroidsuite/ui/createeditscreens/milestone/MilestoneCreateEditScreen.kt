package com.example.projectandroidsuite.ui.createeditscreens.milestone

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.domain.utils.Failure
import com.example.domain.utils.Success
import com.example.projectandroidsuite.ui.createeditscreens.ScreenMode
import com.example.projectandroidsuite.ui.parts.*
import com.example.projectandroidsuite.ui.parts.customitems.ButtonRow
import com.example.projectandroidsuite.ui.parts.customitems.DescriptionInput
import com.example.projectandroidsuite.ui.parts.customitems.TitleInput
import com.example.projectandroidsuite.ui.utils.PickerType
import com.example.projectandroidsuite.ui.utils.makeToast

@Composable
fun MilestoneCreateEditScreen(
    viewModel: MilestoneCreateEditViewModelNew,
    projectId: Int?,
    milestoneId: Int?,
    navigateBack: () -> Unit
) {

    Log.d("CreateMilestonePage", milestoneId.toString())

    if (projectId != null) viewModel.setProjectId(projectId)
    if (milestoneId != null) viewModel.setMilestone(milestoneId)
    LaunchedEffect(key1 = milestoneId) {
        if (milestoneId != null) viewModel.setMilestone(milestoneId)
    }
    LaunchedEffect(key1 = projectId) {
        if (projectId != null) viewModel.setProjectId(projectId)
    }

    val context = LocalContext.current

    var showResponsiblePicker by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()


    when {

        uiState.milestoneInputState.isTitleEmpty == true -> LaunchedEffect(key1 = uiState.milestoneInputState) {
            makeToast("Please enter title", context)
        }

        uiState.milestoneInputState.isResponsibleEmpty == true -> LaunchedEffect(key1 = uiState.milestoneInputState) {
            makeToast("Please choose responsible", context)
        }

        uiState.milestoneInputState.serverResponse is Success -> {
            LaunchedEffect(key1 = uiState.milestoneInputState) {
                navigateBack()
                viewModel.clearInput()
            }
        }

        uiState.milestoneInputState.serverResponse is Failure -> {
            LaunchedEffect(key1 = uiState.milestoneInputState) {
                makeToast("Something went wrong with the server request", context)
            }
        }

        uiState.milestoneDeletionStatus is Success -> {
            LaunchedEffect(key1 = uiState.milestoneDeletionStatus) {
                makeToast(
                    (uiState.milestoneDeletionStatus as Success<String>).value,
                    context
                )
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

            SetMilestonePriority(
                priority = uiState.priority,
                onPriorityToggled = { priority -> viewModel.setPriority(priority) }
            )

            ChooseUser(
                responsible = uiState.responsible,
                onClick = { showResponsiblePicker = true }
            )

            DatePickerRow(
                toggleDatePicker = { showDatePicker = !showDatePicker },
                endDate = uiState.endDate,
            )

            if (showDatePicker) {
                DatePicker(
                    onDateSelected = { date -> viewModel.setDate(date) },
                    onDismissRequest = { showDatePicker = !showDatePicker })
            }
            ButtonRow(
                onSubmit = {
                    if (uiState.screenMode == ScreenMode.CREATE) viewModel.createMilestone()
                    else viewModel.updateMilestone()
                },
                onDismiss = navigateBack,
                onDelete = { showDeleteDialog = true }
            )

        }

        if (showResponsiblePicker) {
            TeamPickerDialog(
                list = uiState.users,
                onClick = { user -> viewModel.setResponsible(user) },
                closeDialog = { showResponsiblePicker = !showResponsiblePicker },
                pickerType = PickerType.SINGLE,
                searchString = uiState.userSearchQuery,
                onSearchChanged = { query -> viewModel.setUserSearch(query) }
            )
        }
        if (showDeleteDialog) {
            ConfirmationDialog(
                text = "Do you want to delete the milestone?",
                onSubmit = {
                    viewModel.deleteMilestone()
                },
                closeDialog = { showDeleteDialog = false })
        }
    }
}