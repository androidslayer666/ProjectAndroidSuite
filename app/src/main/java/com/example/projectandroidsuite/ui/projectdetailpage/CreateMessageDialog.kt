package com.example.projectandroidsuite.ui.projectdetailpage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.model.Message
import com.example.domain.utils.Success
import com.example.projectandroidsuite.ui.parts.RowTeamMember
import com.example.projectandroidsuite.ui.parts.TeamPickerDialog
import com.example.projectandroidsuite.ui.parts.customitems.ButtonUsers
import com.example.projectandroidsuite.ui.parts.customitems.CustomDialog
import com.example.projectandroidsuite.ui.parts.customitems.CustomTextField
import com.example.projectandroidsuite.ui.utils.PickerType

@Composable
fun CreateMessageDialog(
    projectId: Int,
    viewModel: MessageCreateEditViewModel,
    closeDialog: () -> Unit,
    message: Message? = null,
    onMessageDeletedOrEdited: (String) -> Unit = { },
    onDeleteClick: (() -> Unit)? = null
) {
    viewModel.setProjectId(projectId)

    if (message != null) viewModel.setMessage(message)

    val messageUpdatingStatus by viewModel.updatingStatus.collectAsState()
    val messageCreationStatus by viewModel.creationStatus.collectAsState()
    val listUsersFlow by viewModel.users.collectAsState()
    var showTeamPicker by remember { mutableStateOf(false) }
    val userSearch by viewModel.userSearchQuery.collectAsState()


    if (messageCreationStatus is Success<String>) {
        onMessageDeletedOrEdited((messageCreationStatus as Success<String>).value)
        viewModel.clearInput()
        closeDialog()
    }

    if (messageUpdatingStatus is Success<String>) {
        onMessageDeletedOrEdited((messageUpdatingStatus as Success<String>).value)
        viewModel.clearInput()
        closeDialog()
    }
    CustomDialog(
        show = true,
        hide = { closeDialog() },
        text = if (message == null) "Create message" else "Update message",
        onSubmit = {
            if (message == null) {
                viewModel.createMessage()
            } else {
                viewModel.updateMessage()
            }
        },
        onDeleteClick = onDeleteClick

    ) {
        CreateMessageDialogInput(viewModel, { showTeamPicker = true })
    }

    listUsersFlow?.let {
        if (showTeamPicker) {
            TeamPickerDialog(
                list = it,
                onSubmit = {
                    showTeamPicker = false
                    viewModel.updateChosenUsers()
                },
                onClick = { user ->
                    viewModel.addOrRemoveUser(user)

                },
                closeDialog = { showTeamPicker = false },
                pickerType = PickerType.MULTIPLE,
                userSearch,
                { query -> viewModel.setUserSearch(query) }
            )
        }
    }
}

@Composable
fun CreateMessageDialogInput(
    viewModel: MessageCreateEditViewModel,
    showTeamPicker: () -> Unit
) {
    val title by viewModel.title.collectAsState("")
    val description by viewModel.content.collectAsState("")
    val chosenUserList by viewModel.chosenUserList.collectAsState()

    Column(Modifier.defaultMinSize(minHeight = 250.dp)) {
        Row(Modifier.padding(vertical = 12.dp)) {
            CustomTextField(
                value = title,
                label = "Title",
                onValueChange = { text -> viewModel.setTitle(text) })
        }

        Row(Modifier.padding(vertical = 12.dp)) {
            CustomTextField(
                label = "Content",
                numberOfLines = 3,
                height = 100,
                value = description,
                onValueChange = { text ->
                    viewModel.setContent(text)
                })
        }
        Row(Modifier.padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            ButtonUsers(
                singleUser = false,
                onClicked = { showTeamPicker() }
            )
            chosenUserList?.let { it -> RowTeamMember(it, modifier = Modifier.weight(4F)) }
        }

    }
}
