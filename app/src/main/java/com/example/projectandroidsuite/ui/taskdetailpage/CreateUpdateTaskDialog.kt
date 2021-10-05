package com.example.projectandroidsuite.ui.taskdetailpage

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.database.entities.TaskEntity
import com.example.domain.repository.Success
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.logic.PickerType
import com.example.projectandroidsuite.logic.TaskStatus
import com.example.projectandroidsuite.ui.parts.*
import com.example.projectandroidsuite.ui.parts.customitems.CustomButton
import com.example.projectandroidsuite.ui.parts.customitems.CustomDialog
import com.example.projectandroidsuite.ui.parts.customitems.CustomTextField
import com.example.projectandroidsuite.ui.parts.customitems.DialogButtonRow
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CreateUpdateTaskDialog(
    viewModel: TaskCreateEditViewModel,
    closeDialog: () -> Unit,
    task: TaskEntity? = null,
    onTaskDeletedOrEdited: (String) -> Unit = { },
    onDeleteClick: (() -> Unit)? = null
) {
    task?.let { viewModel.setTask(it) }

    val taskUpdatingStatus by viewModel.taskUpdatingStatus.observeAsState()
    val taskCreationStatus by viewModel.taskCreationStatus.observeAsState()

    if (taskCreationStatus is Success<String>) {
        //Log.d("CreateTaskDialog ", "Task creating " + taskCreationStatus.toString())
        onTaskDeletedOrEdited((taskCreationStatus as Success<String>).value)
        closeDialog()
        viewModel.clearInput()
    }

    if (taskUpdatingStatus is Success<String>) {
        //Log.d("CreateTaskDialog ", "Task updating " + taskUpdatingStatus.toString())
        onTaskDeletedOrEdited((taskUpdatingStatus as Success<String>).value)
        closeDialog()
        viewModel.clearInput()
    }

    CustomDialog(
        show = true,
        hide = { closeDialog() },
        text = "Create task",
        onSubmit = {
            if (task == null) {
                viewModel.createTask()
            } else {
                viewModel.updateTask()
            }
        },
        onDeleteClick = onDeleteClick

    ) {
        CreateTaskDialogInput(viewModel)
    }

}

@Composable
fun CreateTaskDialogInput(viewModel: TaskCreateEditViewModel) {
    var showTeamPicker by remember { mutableStateOf(false) }
    var showProjectPicker by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showMilestonePicker by remember { mutableStateOf(false) }

    val title by viewModel.title.observeAsState("")
    val description by viewModel.description.observeAsState("")
    val listProjects by viewModel.projectList.observeAsState()
    val project by viewModel.project.observeAsState()
    val listUsersFlow by viewModel.userList.observeAsState()
    val listChosenUsers by viewModel.chosenUserList.observeAsState(mutableListOf())
    val endDate by viewModel.endDate.observeAsState(Date())
    val projectSearch by viewModel.projectSearchQuery.observeAsState("")
    val userSearch by viewModel.userSearchQuery.observeAsState("")
    val taskStatus by viewModel.taskStatus.observeAsState()
    val milestone by viewModel.milestone.observeAsState()
    val listMilestones by viewModel.milestonesList.observeAsState()
    val priority by viewModel.priority.observeAsState()

    Column(Modifier.defaultMinSize(minHeight = 350.dp)) {
        Row() {
            CustomTextField(
                label = "Title",
                value = title,
                onValueChange = { text -> viewModel.setTitle(text) })
        }
        Row(Modifier.height(100.dp)) {
            CustomTextField(
                label = "Description",
                numberOfLines = 3,
                height = 100,
                value = description,
                onValueChange = { text -> viewModel.setDescription(text) }
            )
        }

        Row(Modifier.padding(vertical = 12.dp)) {
            CustomButton(
                text = "Active",
                clicked = (taskStatus == TaskStatus.ACTIVE),
                onClick = { viewModel.setTaskStatus(TaskStatus.ACTIVE) })
            Spacer(Modifier.size(12.dp))
            CustomButton(
                text = "Complete",
                clicked = (taskStatus == TaskStatus.COMPLETE),
                onClick = { viewModel.setTaskStatus(TaskStatus.COMPLETE) })

        }

        Row(Modifier.padding(vertical = 12.dp)) {
            Text(text = "Priority", modifier = Modifier.weight(2F))
            Checkbox(
                modifier = Modifier.weight(4F),
                checked = priority == 1,
                onCheckedChange = { viewModel.setPriority(if (it) 1 else 0) }
            )
        }

        if (listProjects != null) {

            Row(Modifier.padding(vertical = 12.dp)) {
                Text(
                    text = "Project",
                    Modifier
                        .clickable { showProjectPicker = true }
                        .padding(end = 12.dp)
                        .weight(2F)
                )
                project?.title?.let { it1 ->
                    Text(
                        text = it1, Modifier
                            .weight(4F)
                    )
                }
                if (showProjectPicker) {
                    ProjectPickerDialog(
                        list = listProjects!!,
                        onSubmitList = { },
                        onClick = { project ->
                            run {
                                viewModel.setProject(project = project)
                                showProjectPicker = false
                            }
                        },
                        closeDialog = { showProjectPicker = false },
                        ifChooseResponsibleOrTeam = PickerType.SINGLE,
                        projectSearch,
                        { query -> viewModel.setProjectSearch(query) }
                    )
                }
            }
        } else {
            Row(Modifier.padding(vertical = 12.dp)) {
                Text(
                    text = "",
                )
            }
        }

        if (listUsersFlow != null) {

            Row(Modifier.padding(vertical = 12.dp)) {
                Text(text = "Team",
                    Modifier
                        .clickable { showTeamPicker = true }
                        .align(Alignment.CenterVertically)
                        .weight(2F))

                TeamMemberRow(
                    list = listChosenUsers,
                    Modifier
                        .weight(4F)
                )
            }
            if (showTeamPicker) {
                TeamPickerDialog(
                    list = listUsersFlow!!,
                    onSubmitList = { },
                    onClick = { user ->
                        run {
                            viewModel.addOrRemoveUser(user)
                        }
                    },
                    closeDialog = { showTeamPicker = false },
                    pickerType = PickerType.MULTIPLE,
                    userSearch,
                    { query -> viewModel.setUserSearch(query) }
                )
            }


        } else {
            Row(Modifier.padding(vertical = 12.dp)) {
                Text(
                    text = "",
                )
            }
        }



        if (listMilestones != null) {
            Row(Modifier.padding(vertical = 12.dp)) {
                Text(
                    text = "Milestone",
                    Modifier
                        .clickable { showMilestonePicker = true }
                        .padding(end = 12.dp)
                        .weight(2F)
                )
                milestone?.title?.let { it1 ->
                    Text(
                        text = it1, Modifier
                            .weight(4F)
                    )
                }
                if (showMilestonePicker) {
                    PickerMilestoneDialog(
                        list = listMilestones!!,
                        onClick = { milestone ->
                            run {
                                viewModel.setMilestone(milestone)
                                showMilestonePicker = false
                            }
                        },
                        closeDialog = { showMilestonePicker = false }
                    )
                }
            }
        } else {
            Row(Modifier.padding(vertical = 12.dp)) {
                Text(
                    text = ""
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
