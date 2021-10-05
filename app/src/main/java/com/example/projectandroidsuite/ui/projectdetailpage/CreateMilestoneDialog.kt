package com.example.projectandroidsuite.ui.parts

import android.app.DatePickerDialog
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
import com.example.database.entities.MilestoneEntity
import com.example.domain.repository.Success
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.logic.PickerType
import com.example.projectandroidsuite.ui.parts.customitems.CustomDialog
import com.example.projectandroidsuite.ui.parts.customitems.CustomTextField
import com.example.projectandroidsuite.ui.parts.customitems.DialogButtonRow
import com.example.projectandroidsuite.ui.projectdetailpage.MilestoneCreateEditViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CreateMilestoneDialog(
    milestone: MilestoneEntity? = null,
    projectId: Int,
    viewModel: MilestoneCreateEditViewModel,
    closeDialog: () -> Unit,
    onMilestoneDeletedOrEdited: (String) -> Unit = { },
    onDeleteClick: (() -> Unit)? = null
) {
    viewModel.setProjectId(projectId)
    if (milestone != null) viewModel.setMilestone(milestone)

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


    CustomDialog(
        show = true,
        hide = { closeDialog() },
        text = "Create task",
        onSubmit = {
            if (milestone == null) {
                viewModel.createMilestone()
            } else {
                viewModel.updateMilestone()
            }
        },
        onDeleteClick = onDeleteClick

    ) {
        CreateMilestoneDialogInput(viewModel)
    }


}

@Composable
fun CreateMilestoneDialogInput(viewModel: MilestoneCreateEditViewModel) {
    var showResponsiblePicker by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    val title by viewModel.title.observeAsState("")
    val description by viewModel.description.observeAsState("")
    val listUsersFlow by viewModel.userListFlow.observeAsState()
    val userSearch by viewModel.userSearchQuery.observeAsState("")
    val responsible by viewModel.responsible.observeAsState()
    val endDate by viewModel.endDate.observeAsState(Date())
    val priority by viewModel.priority.observeAsState()

    Column(
//        Modifier.defaultMinSize(minHeight = 200.dp)
    ) {
        Row {
            CustomTextField(
                value = title,
                label = "Title",
                onValueChange = { text -> viewModel.setTitle(text) })
        }

        Row {
            CustomTextField(
                label = "Description",
                numberOfLines = 3,
                height = 100,
                value = description,
                onValueChange = { text ->
                    viewModel.setDescription(text)
                })
        }

        Row (
            Modifier
                .padding(vertical = 12.dp)
                ) {
            Text(text = "Key milestone", modifier = Modifier.weight(2F))
            Checkbox(
                modifier = Modifier.weight(4F),
                checked = priority ?: false,
                onCheckedChange = { viewModel.setPriority(it) }
            )
        }

        Row(
            Modifier
                .padding(vertical = 12.dp)
                .clickable { showResponsiblePicker = true }) {
            Text(
                text = "Responsible",
                Modifier.weight(2F)
            )
            responsible?.let { user ->
                Row(
                    Modifier.weight(4F)
                ) {
                    TeamMemberCard(user = user)
                }
            }
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
                    pickerType = PickerType.SINGLE,
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
                DatePicker({ date -> viewModel.setDate(date) }, { showDatePicker = false })
            }
        }
    }
}
