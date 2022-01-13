package com.example.projectandroidsuite.ui.createeditscreens.milestone

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import com.example.domain.utils.Failure
import com.example.domain.utils.Success
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.ui.createeditscreens.ScreenMode
import com.example.projectandroidsuite.ui.parts.*
import com.example.projectandroidsuite.ui.parts.customitems.ButtonRow
import com.example.projectandroidsuite.ui.parts.dialogs.TeamPickerDialog
import com.example.projectandroidsuite.ui.parts.inputs.ChooseUser
import com.example.projectandroidsuite.ui.parts.inputs.DatePickerRow
import com.example.projectandroidsuite.ui.parts.inputs.DescriptionInput
import com.example.projectandroidsuite.ui.parts.inputs.TitleInput
import com.example.projectandroidsuite.ui.utils.PickerType
import com.example.projectandroidsuite.ui.utils.hideKeyboardOnLoseFocus
import com.example.projectandroidsuite.ui.utils.makeToast

@Composable
fun MilestoneCreateEditScreen(
    viewModel: MilestoneCreateEditViewModel,
    projectId: Int?,
    milestoneId: Int?,
    navigateBack: () -> Unit
) {

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

        MilestoneCreateEditScreenBody(
            uiState = uiState,
            setTitle = { text -> viewModel.setTitle(text) },
            setDescription = { text -> viewModel.setDescription(text) },
            setPriority = { priority -> viewModel.setPriority(priority) },
            createMilestone = { viewModel.createMilestone() },
            updateMilestone = { viewModel.updateMilestone() },
            showResponsiblePicker = { showResponsiblePicker = true },
            showDatePicker = {  showDatePicker = !showDatePicker  },
            showDeleteDialog = {showDeleteDialog = true },
            navigateBack = navigateBack
        )

        if (showDatePicker) {
            DatePicker(
                onDateSelected = { date -> viewModel.setDate(date) },
                onDismissRequest = { showDatePicker = !showDatePicker })
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
                text = stringResource(R.string.do_you_want_to_delete_the_milestone),
                onSubmit = {
                    viewModel.deleteMilestone()
                },
                closeDialog = { showDeleteDialog = false })
        }
    }
}


@Composable
fun MilestoneCreateEditScreenBody(
    uiState: MilestoneCreateState,
    setTitle: (String) -> Unit = {},
    setDescription: (String) -> Unit = {},
    setPriority: (Boolean) -> Unit = {},
    createMilestone: () -> Unit = {},
    updateMilestone: () -> Unit = {},
    showResponsiblePicker: () -> Unit = {},
    showDatePicker: () -> Unit = {},
    showDeleteDialog: () -> Unit = {},
    navigateBack: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.hideKeyboardOnLoseFocus(focusManager)) {

        TitleInput(
            text = uiState.title,
            onInputChange = { text -> setTitle(text) },
            textIsEmpty = uiState.milestoneInputState.isTitleEmpty
        )

        DescriptionInput(
            text = uiState.description,
            onInputChange = { text -> setDescription(text) }
        )

        SetMilestonePriority(
            priority = uiState.priority,
            onPriorityToggled = { priority -> setPriority(priority) }
        )

        ChooseUser(
            responsible = uiState.responsible,
            onClick = showResponsiblePicker,
            userIsEmpty = uiState.milestoneInputState.isResponsibleEmpty
        )

        DatePickerRow(
            toggleDatePicker = showDatePicker,
            endDate = uiState.endDate,
        )

        ButtonRow(
            onSubmit = {
                if (uiState.screenMode == ScreenMode.CREATE) createMilestone()
                else updateMilestone()
            },
            onDismiss = navigateBack,
            onDelete = showDeleteDialog,
            showDeleteOption = uiState.screenMode == ScreenMode.EDIT
        )
    }
}