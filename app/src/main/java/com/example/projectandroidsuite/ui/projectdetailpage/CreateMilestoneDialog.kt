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
import com.example.database.entities.MilestoneEntity
import com.example.domain.repository.Success
import com.example.projectandroidsuite.logic.PickerType
import com.example.projectandroidsuite.ui.parts.customitems.CustomTextField
import com.example.projectandroidsuite.ui.projectdetailpage.MessageCreateEditViewModel
import com.example.projectandroidsuite.ui.projectdetailpage.MilestoneCreateEditViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CreateMilestoneDialog(
    projectId: Int,
    viewModel: MilestoneCreateEditViewModel,
    closeDialog: () -> Unit,
    message: MilestoneEntity? = null,
    onMilestoneDeletedOrEdited: (String) -> Unit = { }
) {
    viewModel.setProjectId(projectId)

    val milestoneUpdatingStatus by viewModel.subtaskUpdatingStatus.observeAsState()
    val milestoneCreationStatus by viewModel.subtaskCreationStatus.observeAsState()

    if (milestoneCreationStatus is Success<String>) {
        onMilestoneDeletedOrEdited((milestoneCreationStatus as Success<String>).value)
        viewModel.clearInput()
        closeDialog()
    }

    if (milestoneUpdatingStatus is Success<String>) {
        onMilestoneDeletedOrEdited((milestoneUpdatingStatus as Success<String>).value)
        viewModel.clearInput()
        closeDialog()
    }

    AlertDialog(
        onDismissRequest = {
            closeDialog()
            //viewModel.clearInput()
        },
        title = {
            if (message == null) Text(text = "Create milestone") else Text(text = "Update milestone")
        },
        text = {
            CreateMilestoneDialogInput(viewModel)
        },
        confirmButton = {
            Button(
                onClick = {
                    closeDialog()
                    //viewModel.clearInput()
                    if (message != null) viewModel.updateMilestone() else viewModel.createMilestone()
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
fun CreateMilestoneDialogInput(viewModel: MilestoneCreateEditViewModel) {
    var showTeamPicker by remember { mutableStateOf(false) }
    var showResponsiblePicker by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    val title by viewModel.title.observeAsState("")
    val description by viewModel.description.observeAsState("")
    val listUsersFlow by viewModel.userListFlow.observeAsState()
    val userSearch by viewModel.userSearchQuery.observeAsState("")
    val responsible by viewModel.responsible.observeAsState()
    val endDate by viewModel.endDate.observeAsState(Date())


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

        Row(Modifier.padding(vertical = 12.dp)) {
            Text(
                text = "Choose responsible",
                Modifier
                    .clickable { showResponsiblePicker = true }
                    .weight(2F)
            )
            responsible?.let { user -> Row( Modifier
                .weight(4F) ){TeamMemberCard(user = user) }}
        }
        listUsersFlow?.let {

            if (showResponsiblePicker) {
                TeamPickerDialog(
                    list = it,
                    onSubmitList = { },
                    onClick = { user ->
                        run {
                            viewModel.setResponsible(user)
                            showResponsiblePicker = false
                        }
                    },
                    closeDialog = { showResponsiblePicker = false },
                    ifChooseResponsibleOrTeam = PickerType.SINGLE,
                    userSearch,
                    { query -> viewModel.setUserSearch(query) }
                )
            }
        }
        Row(Modifier.padding(vertical = 12.dp)) {
            Text(
                text = "End date",
                Modifier
                    .clickable { showDatePicker = true }
                    .padding(vertical = 12.dp)
                    .weight(2F)
            )
            Text(
                text = SimpleDateFormat("dd/MM/yyyy").format(endDate),
                Modifier
                    .clickable { showDatePicker = true }
                    .padding(12.dp, 12.dp)
                    .weight(4F)
            )
            if (showDatePicker) {
                DatePickerDialog(
                    date = viewModel.endDate.value ?: Date(),
                    true,
                    { showDatePicker = false },
                    { date -> viewModel.setDate(date) })
            }
        }
    }
}
