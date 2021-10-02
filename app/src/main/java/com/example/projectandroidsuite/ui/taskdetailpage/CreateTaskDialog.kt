package com.example.projectandroidsuite.ui.parts

import android.util.Log
import android.view.ContextThemeWrapper
import android.widget.CalendarView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.database.entities.TaskEntity
import com.example.database.entities.UserEntity
import com.example.domain.repository.Success
import com.example.projectandroidsuite.logic.PickerType
import com.example.projectandroidsuite.logic.ProjectStatus
import com.example.projectandroidsuite.logic.TaskStatus
import com.example.projectandroidsuite.ui.parts.customitems.CustomTextField
import com.example.projectandroidsuite.ui.taskdetailpage.TaskCreateEditViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun CreateTaskDialog(
    viewModel: TaskCreateEditViewModel,
    closeDialog: () -> Unit,
    task: TaskEntity? = null,
    onTaskDeletedOrEdited: (String) -> Unit = { }
) {
    //viewModel.clearInput()
    task?.let { viewModel.setTask(it) }

    val taskUpdatingStatus by viewModel.taskUpdatingStatus.observeAsState()
    val taskCreationStatus by viewModel.taskCreationStatus.observeAsState()

    if (taskCreationStatus is Success<String>) {
        //Log.d("CreateTaskDialog ", "Task creating " + taskCreationStatus.toString())
        onTaskDeletedOrEdited((taskCreationStatus as Success<String>).value)
        closeDialog()
        viewModel.clearInput()
    }

    if (taskUpdatingStatus != null) {
        //Log.d("CreateTaskDialog ", "Task updating " + taskUpdatingStatus.toString())
        onTaskDeletedOrEdited((taskUpdatingStatus as Success<String>).value)
        closeDialog()
        viewModel.clearInput()
    }

    AlertDialog(
        onDismissRequest = {
            closeDialog()
        },
        title = {
            if (task == null) Text(text = "Create Task") else Text(text = "Update Task")
        },
        text = {
            CreateTaskDialogInput(viewModel)
        },
        confirmButton = {
            Button(
                onClick = {
                    //closeDialog()
                    if (task == null) {
                        viewModel.createTask()
                    } else {
                        viewModel.updateTask()
                    }
                }, modifier = Modifier.width(100.dp)
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    closeDialog()
                }, modifier = Modifier.width(100.dp)
            ) {
                Text("Dismiss")
            }
        }
    )
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
    val listUsersFlow by viewModel.userListFlow.observeAsState()
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
            Text(text = "Status", modifier = Modifier.weight(2F))
            Column(
                Modifier
                    .selectableGroup()
                    .weight(4F)
            ) {
                Row() {
                    RadioButton(
                        (taskStatus == TaskStatus.ACTIVE),
                        { viewModel.setTaskStatus(TaskStatus.ACTIVE) }
                    )
                    Text(text = "Active")
                }
                Row() {
                    RadioButton(
                        (taskStatus == TaskStatus.COMPLETE),
                        { viewModel.setTaskStatus(TaskStatus.COMPLETE) }
                    )
                    Text(text = "Complete")
                }
            }
        }

        Row(Modifier.padding(vertical = 12.dp)) {
            Text(text = "Priority", modifier = Modifier.weight(2F))
            Checkbox(
                modifier = Modifier.weight(4F),
                checked = priority == 1,
                onCheckedChange = { viewModel.setPriority(if (it) 1 else 0) }
            )
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
