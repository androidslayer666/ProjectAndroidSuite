package com.example.projectandroidsuite.ui.createeditscreens.message

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import com.example.domain.utils.Failure
import com.example.domain.utils.Success
import com.example.projectandroidsuite.ui.createeditscreens.ScreenMode
import com.example.projectandroidsuite.ui.parts.*
import com.example.projectandroidsuite.ui.parts.customitems.ButtonRow
import com.example.projectandroidsuite.ui.utils.PickerType
import com.example.projectandroidsuite.ui.utils.hideKeyboardOnLoseFocus
import com.example.projectandroidsuite.ui.utils.makeToast


@Composable
fun MessageCreateEditScreen(
    viewModel: MessageCreateEditViewModel,
    projectId: Int?,
    messageId: Int?,
    navigateBack: () -> Unit
) {

    Log.d("MessageCreateEditScreen", messageId.toString())

    LaunchedEffect(key1 = projectId) {
        if (projectId != null) viewModel.setProjectId(projectId)
    }

    LaunchedEffect(key1 = messageId) {
        if (messageId != null) viewModel.setMessage(messageId)
    }

    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showTeamPicker by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    val uiState by viewModel.uiState.collectAsState()


    when {

        uiState.messageInputState.isTitleEmpty == true -> LaunchedEffect(key1 = uiState.messageInputState) {
            makeToast("Please enter message title", context)
        }

        uiState.messageInputState.isTeamEmpty == true -> LaunchedEffect(key1 = uiState.messageInputState) {
            makeToast("Please choose team", context)
        }
        uiState.messageInputState.isTextEmpty == true -> LaunchedEffect(key1 = uiState.messageInputState) {
            makeToast("Please choose task responsible", context)
        }
        uiState.messageInputState.serverResponse is Success -> {
            LaunchedEffect(key1 = uiState.messageInputState) {
                viewModel.clearInput()
                navigateBack()
            }
        }
        uiState.messageInputState.serverResponse is Failure -> {
            LaunchedEffect(key1 = uiState.messageInputState) {
                makeToast("Something went wrong with the server request", context)
            }
        }
        uiState.messageDeletionStatus is Success -> {
            LaunchedEffect(key1 = uiState.messageInputState) {
                makeToast("Message deleted successfully", context)
                navigateBack()
            }
        }
    }


    Box {
        Column(modifier = Modifier.hideKeyboardOnLoseFocus(focusManager)) {

            TitleInput(
                text = uiState.title,
                onInputChange = { text -> viewModel.setTitle(text) }
            )

            DescriptionInput(
                text = uiState.content,
                onInputChange = { text -> viewModel.setContent(text) }
            )

            ChooseTeam(
                team = uiState.chosenUserList,
                onClick = { showTeamPicker = true }
            )

            ButtonRow(
                onSubmit = {
                    if (uiState.screenMode == ScreenMode.CREATE) viewModel.createMessage()
                    else viewModel.updateMessage()
                },
                onDismiss = navigateBack,
                onDelete = { showDeleteDialog = true }
            )

        }

        if (showDeleteDialog) {
            ConfirmationDialog(
                text = "Do you want to delete the message?",
                onSubmit = { viewModel.deleteMessage() },
                { showDeleteDialog = false })
        }

        uiState.users?.let {
            if (showTeamPicker) {
                TeamPickerDialog(
                    list = it,
                    //onSubmit = { viewModel.updateChosenUsers() },
                    onClick = { user -> viewModel.addOrRemoveUser(user) },
                    closeDialog = { showTeamPicker = false },
                    pickerType = PickerType.MULTIPLE,
                    searchString = uiState.userSearchQuery,
                    onSearchChanged = { query -> viewModel.setUserSearch(query) }
                )
            }
        }
    }
}

