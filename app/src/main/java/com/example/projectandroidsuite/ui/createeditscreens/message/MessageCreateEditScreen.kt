package com.example.projectandroidsuite.ui.createeditscreens.message

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
import com.example.domain.utils.log
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.ui.createeditscreens.ScreenMode
import com.example.projectandroidsuite.ui.parts.*
import com.example.projectandroidsuite.ui.parts.customitems.ButtonRow
import com.example.projectandroidsuite.ui.parts.dialogs.TeamPickerDialog
import com.example.projectandroidsuite.ui.parts.inputs.ChooseTeam
import com.example.projectandroidsuite.ui.parts.inputs.DescriptionInput
import com.example.projectandroidsuite.ui.parts.inputs.TitleInput
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

    log(messageId)

    LaunchedEffect(key1 = projectId) {
        if (projectId != null) viewModel.setProjectId(projectId)
    }

    LaunchedEffect(key1 = messageId) {
        if (messageId != null) viewModel.setMessage(messageId)
    }

    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showTeamPicker by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()


    when {

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

        MessageCreateEditScreenBody(
            uiState = uiState,
            setTitle = { text -> viewModel.setTitle(text) },
            setContent = { text -> viewModel.setContent(text) },
            showTeamPicker = { showTeamPicker = true },
            showDeleteDialog = { showDeleteDialog = true },
            createMessage = { viewModel.createMessage() },
            updateMessage = { viewModel.updateMessage() },
            navigateBack = navigateBack
        )

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

        if (showDeleteDialog) {
            ConfirmationDialog(
                text = stringResource(R.string.do_you_want_to_delete_the_message),
                onSubmit = { viewModel.deleteMessage() },
                { showDeleteDialog = false })
        }
    }
}

@Composable
fun MessageCreateEditScreenBody(
    uiState: MessageCreateState,
    setTitle: (String) -> Unit,
    setContent: (String) -> Unit,
    showTeamPicker: () -> Unit,
    showDeleteDialog: () -> Unit,
    createMessage: () -> Unit,
    updateMessage: () -> Unit,
    navigateBack: () -> Unit
) {
    val focusManager = LocalFocusManager.current


    Column(modifier = Modifier.hideKeyboardOnLoseFocus(focusManager)) {

        TitleInput(
            text = uiState.title,
            onInputChange = { text -> setTitle(text) },
            textIsEmpty = uiState.messageInputState.isTitleEmpty
        )

        DescriptionInput(
            text = uiState.content,
            onInputChange = { text -> setContent(text) },
            textIsEmpty = uiState.messageInputState.isTextEmpty
        )

        ChooseTeam(
            team = uiState.chosenUserList,
            onClick = showTeamPicker,
            teamIsEmpty = uiState.messageInputState.isTeamEmpty
        )

        ButtonRow(
            onSubmit = {
                if (uiState.screenMode == ScreenMode.CREATE) createMessage()
                else updateMessage()
            },
            onDismiss = navigateBack,
            onDelete = showDeleteDialog,
            showDeleteOption = uiState.screenMode == ScreenMode.EDIT
        )
    }
}