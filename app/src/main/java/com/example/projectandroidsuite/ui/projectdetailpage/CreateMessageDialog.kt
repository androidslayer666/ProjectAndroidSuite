package com.example.projectandroidsuite.ui.projectdetailpage

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.database.entities.MessageEntity
import com.example.domain.repository.Success
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.logic.PickerType
import com.example.projectandroidsuite.ui.parts.TeamMemberRow
import com.example.projectandroidsuite.ui.parts.TeamPickerDialog
import com.example.projectandroidsuite.ui.parts.customitems.ButtonUsers
import com.example.projectandroidsuite.ui.parts.customitems.CustomDialog
import com.example.projectandroidsuite.ui.parts.customitems.CustomTextField
import com.example.projectandroidsuite.ui.parts.customitems.DialogButtonRow

@Composable
fun CreateMessageDialog(
    projectId: Int,
    viewModel: MessageCreateEditViewModel,
    closeDialog: () -> Unit,
    message: MessageEntity? = null,
    onMessageDeletedOrEdited: (String) -> Unit = { },
    onDeleteClick: (() -> Unit)? = null
) {
    viewModel.setProjectId(projectId)

    val messageUpdatingStatus by viewModel.updatingStatus.observeAsState()
    val messageCreationStatus by viewModel.creationStatus.observeAsState()

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
        text = "Create task",
        onSubmit = {
            if (message == null) {
                viewModel.createMessage()
            } else {
                viewModel.updateMessage()
            }
        },
        onDeleteClick = onDeleteClick

    ) {
        CreateMessageDialogInput(viewModel)
    }
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
            CustomTextField(
                value = title,
                label = "Title",
                onValueChange = { text -> viewModel.setTitle(text) })
        }

        Row(Modifier.padding(vertical = 12.dp)) {
            CustomTextField(
                label = "Description",
                numberOfLines = 3,
                height = 100,
                value = description,
                onValueChange = { text ->
                    viewModel.setDescription(text)
                })
        }

        listUsersFlow?.let {
            Row(Modifier.padding(vertical = 12.dp)) {

                ButtonUsers(
                    singleUser = false,
                    onClicked = { showTeamPicker = true }
                )

                chosenUserList?.let { it -> TeamMemberRow(it, modifier = Modifier.weight(4F)) }
                if (showTeamPicker) {
                    TeamPickerDialog(
                        list = it,
                        onSubmitList = { },
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
    }
}
