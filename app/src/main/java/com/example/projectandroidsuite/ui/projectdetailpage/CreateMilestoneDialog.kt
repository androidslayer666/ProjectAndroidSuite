package com.example.projectandroidsuite.ui.projectdetailpage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.domain.model.Milestone
import com.example.domain.utils.Failure
import com.example.domain.utils.Success
import com.example.projectandroidsuite.ui.utils.Constants.FORMAT_SHOW_DATE
import com.example.projectandroidsuite.ui.utils.PickerType
import com.example.projectandroidsuite.ui.parts.CardTeamMember
import com.example.projectandroidsuite.ui.parts.DatePicker
import com.example.projectandroidsuite.ui.parts.TeamPickerDialog
import com.example.projectandroidsuite.ui.parts.customitems.ButtonUsers
import com.example.projectandroidsuite.ui.parts.customitems.CustomDialog
import com.example.projectandroidsuite.ui.parts.customitems.CustomTextField
import com.example.projectandroidsuite.ui.utils.makeToast
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CreateMilestoneDialog(
    milestone: Milestone? = null,
    projectId: Int,
    viewModel: MilestoneCreateEditViewModel,
    closeDialog: () -> Unit,
    onMilestoneDeletedOrEdited: (String) -> Unit = { },
    onDeleteClick: (() -> Unit)? = null
) {
    viewModel.setProjectId(projectId)
    if (milestone != null) viewModel.setMilestone(milestone)

    val context = LocalContext.current

    var showResponsiblePicker by remember { mutableStateOf(false) }
    val listUsersFlow by viewModel.users.collectAsState()
    val userSearch by viewModel.userSearchQuery.collectAsState()
    val milestoneInputState by viewModel.milestoneInputState.collectAsState()


    when {
        milestoneInputState.isTitleEmpty == true -> LaunchedEffect(key1 = milestoneInputState) {
            makeToast("Please enter title", context )
        }
        milestoneInputState.isResponsibleEmpty == true -> LaunchedEffect(key1 = milestoneInputState) {
            makeToast("Please choose responsible", context )
        }
        milestoneInputState.serverResponse is Success -> {
            onMilestoneDeletedOrEdited((milestoneInputState.serverResponse as Success<String>).value)
            closeDialog()
            viewModel.clearInput()
        }
        milestoneInputState.serverResponse is Failure -> {
            LaunchedEffect(key1 = milestoneInputState) {
                makeToast( "Something went wrong with the server request", context )
            }
        }
    }


    Box {
        CustomDialog(
            show = true,
            hide = { closeDialog() },
            text = if (milestone == null) "Create milestone" else "Update milestone",
            onSubmit = {
                if (milestone == null) {
                    viewModel.createMilestone()
                } else {
                    viewModel.updateMilestone()
                }
            },
            onDeleteClick = onDeleteClick
        ) {
            CreateMilestoneDialogInput(viewModel) { showResponsiblePicker = true }
        }


        listUsersFlow.let {
            if (showResponsiblePicker) {
                TeamPickerDialog(
                    list = it,
                    onSubmit = { },
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
    }
}

@Composable
fun CreateMilestoneDialogInput(
    viewModel: MilestoneCreateEditViewModel,
    showResponsiblePicker: ()-> Unit

) {

    var showDatePicker by remember { mutableStateOf(false) }
    val title by viewModel.title.collectAsState("")
    val description by viewModel.description.collectAsState("")
    val responsible by viewModel.responsible.collectAsState()
    val endDate by viewModel.endDate.collectAsState(Date())
    val priority by viewModel.priority.collectAsState()

    Column{
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
                .clickable { showResponsiblePicker() }) {
            ButtonUsers(
                singleUser = true,
                onClicked = {showResponsiblePicker()}
            )
            responsible?.let { user ->
                Row(
                    Modifier.weight(4F)
                ) {
                    CardTeamMember(user = user)
                }
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
                text = SimpleDateFormat(FORMAT_SHOW_DATE, Locale.getDefault()).format(endDate),
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
