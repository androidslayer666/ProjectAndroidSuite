package com.example.projectandroidsuite.ui.projectdetailpage

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.database.entities.MessageEntity
import com.example.domain.model.Message
import com.example.domain.repository.Success
import com.example.projectandroidsuite.logic.PickerType
import com.example.projectandroidsuite.ui.parts.RowTeamMember
import com.example.projectandroidsuite.ui.parts.TeamPickerDialog
import com.example.projectandroidsuite.ui.parts.customitems.ButtonUsers
import com.example.projectandroidsuite.ui.parts.customitems.CustomDialog
import com.example.projectandroidsuite.ui.parts.customitems.CustomTextField

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

    if(message != null) viewModel.setMessage(message)

    val messageUpdatingStatus by viewModel.updatingStatus.observeAsState()
    val messageCreationStatus by viewModel.creationStatus.observeAsState()
    val listUsersFlow by viewModel.userListFlow.observeAsState()
    var showTeamPicker by remember { mutableStateOf(false) }
    val userSearch by viewModel.userSearchQuery.observeAsState("")


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
        CreateMessageDialogInput(viewModel, {showTeamPicker = true})
    }

    listUsersFlow?.let {
            if (showTeamPicker) {
                TeamPickerDialog(
                    list = it,
                    onSubmit = {  showTeamPicker = false },
                    onClick = { user ->
                        viewModel.addOrRemoveUser(user)

                    },
                    closeDialog = { showTeamPicker = false },
                    pickerType = PickerType.MULTIPLE,
                    userSearch,
                    { query -> viewModel.setUserSearch(query)}
                )
        }
    }
}

@Composable
fun CreateMessageDialogInput(
    viewModel: MessageCreateEditViewModel,
    showTeamPicker: () -> Unit
) {
    val title by viewModel.title.observeAsState("")
    val description by viewModel.content.observeAsState("")
    val chosenUserList by viewModel.chosenUserList.observeAsState()

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
