package com.example.projectandroidsuite.ui.parts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.database.entities.MessageEntity
import com.example.domain.repository.Success
import com.example.projectandroidsuite.logic.PickerType
import com.example.projectandroidsuite.ui.parts.customitems.CustomTextField
import com.example.projectandroidsuite.ui.projectdetailpage.MessageCreateEditViewModel

@Composable
fun CreateMessageDialog(
    projectId: Int,
    viewModel: MessageCreateEditViewModel,
    closeDialog: () -> Unit,
    message: MessageEntity? = null,
    onMessageDeletedOrEdited: (String) -> Unit = { }
) {
    viewModel.setProjectId(projectId)

    val messageUpdatingStatus by viewModel.subtaskUpdatingStatus.observeAsState()
    val messageCreationStatus by viewModel.subtaskCreationStatus.observeAsState()

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

    AlertDialog(
        onDismissRequest = {
            closeDialog()
            //viewModel.clearInput()
        },
        title = {
            if (message == null) Text(text = "Create message") else Text(text = "Update message")
        },
        text = {
            CreateMessageDialogInput(viewModel)
        },
        confirmButton = {
            Button(
                onClick = {
                    closeDialog()
                    //viewModel.clearInput()
                    if (message != null) viewModel.updateMessage() else viewModel.createMessage()
                }, modifier = Modifier.width(100.dp)
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    closeDialog()
                    viewModel.clearInput()
                }, modifier = Modifier.width(100.dp)
            ) {
                Text("Dismiss")
            }
        }
    )
}

@Composable
fun CreateMessageDialogInput(viewModel: MessageCreateEditViewModel) {
    var showTeamPicker by remember { mutableStateOf(false) }

    val title by viewModel.title.observeAsState("")
    val description by viewModel.description.observeAsState("")
    val listUsersFlow by viewModel.userListFlow.observeAsState()
    val userSearch by viewModel.userSearchQuery.observeAsState("")
    val chosenUserList by viewModel.chosenUserList.observeAsState()

    Column(Modifier.defaultMinSize(minHeight = 250.dp)) {
        Row(Modifier.padding(vertical = 12.dp)) {
            Text(text = "Title", modifier = Modifier.weight(2F))
            CustomTextField(modifier = Modifier
                .fillMaxWidth()
                .weight(4F),
                value = title,
                onValueChange = { text -> viewModel.setTitle(text) })
        }

        Row(Modifier.padding(vertical = 12.dp)) {
            Text(text = "Description", modifier = Modifier.weight(2F))
            CustomTextField(modifier = Modifier
                .fillMaxWidth()
                .weight(4F),
                value = description, onValueChange = { text ->
                    run {
                        viewModel.setDescription(text)
                    }
                })
        }

        listUsersFlow?.let {
            Row(Modifier.padding(vertical = 12.dp)) {
                Text(text = "Team",
                    Modifier
                        .clickable { showTeamPicker = true }
                        .align(Alignment.CenterVertically)
                        .weight(2F))

                chosenUserList?.let { it -> TeamMemberRow(it) }
                if (showTeamPicker) {
                    TeamPickerDialog(
                        list = it,
                        onSubmitList = { },
                        onClick = { user ->
                            run {
                                viewModel.addOrRemoveUser(user)
                            }
                        },
                        closeDialog = { showTeamPicker = false },
                        ifChooseResponsibleOrTeam = PickerType.MULTIPLE,
                        userSearch,
                        { query -> viewModel.setUserSearch(query) }
                    )
                }
            }
        }
    }
}
