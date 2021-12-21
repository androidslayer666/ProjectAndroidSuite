package com.example.projectandroidsuite.ui.createeditscreens.subtask

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.domain.utils.Failure
import com.example.domain.utils.Success
import com.example.projectandroidsuite.ui.parts.ChooseUser
import com.example.projectandroidsuite.ui.parts.TeamPickerDialog
import com.example.projectandroidsuite.ui.parts.TitleInput
import com.example.projectandroidsuite.ui.parts.customitems.ButtonRow
import com.example.projectandroidsuite.ui.utils.PickerType
import com.example.projectandroidsuite.ui.utils.makeToast

@Composable
fun SubtaskCreateEditScreen(
    viewModel: SubtaskCreateEditViewModel,
    taskId: Int?,
    navigateBack: () -> Unit
) {


    val context = LocalContext.current

    var showTeamPicker by remember { mutableStateOf(false) }
    var showResponsiblePicker by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.subtaskInputState.isTitleEmpty == true -> LaunchedEffect(key1 = uiState.subtaskInputState) {
            makeToast("Please enter title", context)
        }
        uiState.subtaskInputState.isResponsibleEmpty == true -> LaunchedEffect(key1 = uiState.subtaskInputState) {
            makeToast("Please choose responsible", context)
        }
        uiState.subtaskInputState.serverResponse is Success -> {
            makeToast((uiState.subtaskInputState.serverResponse as Success<String>).value, context)
            viewModel.clearInput()
        }
        uiState.subtaskInputState.serverResponse is Failure -> {
            LaunchedEffect(key1 = uiState.subtaskInputState) {
                makeToast("Something went wrong with the server request", context)
            }
        }
    }

    Column {

        TitleInput(
            text = uiState.title,
            onInputChange = { text -> viewModel.setTitle(text) }
        )

        ChooseUser(
            responsible = uiState.responsible,
            onClick = { showResponsiblePicker = true }
        )

        ButtonRow(
            onSubmit = { viewModel.createSubtask() },
            onDismiss = navigateBack,
        )
    }

    if (showTeamPicker) {
        if (showResponsiblePicker) {
            TeamPickerDialog(
                list = uiState.users,
                //onSubmit = { },
                onClick = { user -> viewModel.setResponsible(user) },
                closeDialog = { showResponsiblePicker = false },
                pickerType = PickerType.SINGLE,
                searchString = uiState.userSearchQuery,
                onSearchChanged = { query -> viewModel.setUserSearch(query) }
            )
        }
    }
}

