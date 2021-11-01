package com.example.projectandroidsuite.ui.taskdetailpage

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.domain.model.Task
import com.example.domain.utils.Success
import com.example.domain.utils.TaskStatus
import com.example.projectandroidsuite.ui.utils.Constants.FORMAT_SHOW_DATE
import com.example.projectandroidsuite.ui.parts.*
import com.example.projectandroidsuite.ui.parts.customitems.ButtonUsers
import com.example.projectandroidsuite.ui.parts.customitems.CustomButton
import com.example.projectandroidsuite.ui.parts.customitems.CustomDialog
import com.example.projectandroidsuite.ui.parts.customitems.CustomTextField
import com.example.projectandroidsuite.ui.utils.PickerType
import com.example.projectandroidsuite.ui.utils.makeToast
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CreateUpdateTaskDialog(
    viewModel: TaskCreateEditViewModel,
    closeDialog: () -> Unit,
    task: Task? = null,
    onTaskDeletedOrEdited: (String) -> Unit = { },
    onDeleteClick: (() -> Unit)? = null
) {
    LaunchedEffect(key1 = task) {
        task?.let { viewModel.setTask(it) }
    }


    val taskUpdatingStatus by viewModel.taskUpdatingStatus.collectAsState()
    val taskCreationStatus by viewModel.taskCreationStatus.collectAsState()

    var showTeamPicker by remember { mutableStateOf(false) }
    var showProjectPicker by remember { mutableStateOf(false) }
    var showMilestonePicker by remember { mutableStateOf(false) }
    val listMilestones by viewModel.milestonesList.collectAsState(listOf())
    val listProjects by viewModel.projectList.collectAsState()
    val listUsersFlow by viewModel.userList.collectAsState(listOf())
    val project by viewModel.project.collectAsState()

    val projectSearch by viewModel.projectSearchQuery.collectAsState()
    val userSearch by viewModel.userSearchQuery.collectAsState()

    if (taskCreationStatus is Success) {
        Log.d("CreateTaskDialog ", "Task creating " + taskCreationStatus.toString())
        onTaskDeletedOrEdited((taskCreationStatus as Success<String>).value)
        closeDialog()
        viewModel.clearInput()
    }

    if (taskUpdatingStatus is Success) {
        Log.d("CreateTaskDialog ", "Task updating " + taskUpdatingStatus.toString())
        onTaskDeletedOrEdited((taskUpdatingStatus as Success<String>).value)
        closeDialog()
        viewModel.clearInput()
    }

    CustomDialog(
        show = true,
        hide = { closeDialog()
               viewModel.clearInput()
               },
        text = if (task == null) "Create task" else "Update task",
        onSubmit = {
            if (task == null) {
                viewModel.createTask()
                viewModel.clearInput()
            } else {
                viewModel.updateTask()
                viewModel.clearInput()
            }
        },
        onDeleteClick = onDeleteClick
    ) {
        CreateTaskDialogInput(
            viewModel,
            { showProjectPicker = true },
            { showTeamPicker = true },
            { showMilestonePicker = true }
        )
    }

    if (showProjectPicker) {
        ProjectPickerDialog(
            list = listProjects,
            onSubmit = { showProjectPicker = false },
            onClick = { projectClicked ->
                run {
                    viewModel.setProject(project = projectClicked)
                }
            },
            closeDialog = { showProjectPicker = false },
            ifChooseResponsibleOrTeam = PickerType.SINGLE,
            searchString = projectSearch,
            onSearchChanged = { query -> viewModel.setStringForFilteringProjects(query) }
        )
    }

    if (showTeamPicker) {
        TeamPickerDialog(
            list = listUsersFlow!!,
            onSubmit = { showTeamPicker = false},
            onClick = { user ->
                    viewModel.addOrRemoveUser(user)
            },
            closeDialog = { showTeamPicker = false },
            pickerType = PickerType.MULTIPLE,
            searchString = userSearch,
            onSearchChanged = { query -> viewModel.setUserSearch(query)}
        )
    }

//    if (showMilestonePicker && project != null){
//        makeToast("the project doesn't have milestones", LocalContext.current)
//        showMilestonePicker = false
//    }

    listMilestones.let {
        if (showMilestonePicker && listMilestones?.isNotEmpty() == true) {
            DialogPickerMilestone(
                list = listMilestones,
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




}

@Composable
fun CreateTaskDialogInput(
    viewModel: TaskCreateEditViewModel,
    showProjectPicker: () -> Unit,
    showTeamPicker: () -> Unit,
    showMilestonePicker: () -> Unit
) {

    var showDatePicker by remember { mutableStateOf(false) }



    val title by viewModel.title.collectAsState("")
    val description by viewModel.description.collectAsState("")
    val listProjects by viewModel.projectList.collectAsState()
    val project by viewModel.project.collectAsState()
    val listUsersFlow by viewModel.userList.collectAsState(listOf())
    val listChosenUsers by viewModel.chosenUserList.collectAsState(null)
    val endDate by viewModel.endDate.collectAsState(Date())

    val taskStatus by viewModel.taskStatus.collectAsState()
    val milestone by viewModel.milestone.collectAsState()
    val listMilestones by viewModel.milestonesList.collectAsState(listOf())
    val priority by viewModel.priority.collectAsState()

    Column(Modifier.defaultMinSize(minHeight = 350.dp)) {
        Row {
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

        if (listProjects.isNotEmpty()) {

            Row(Modifier.padding(vertical = 12.dp)) {
                Text(
                    text = "Project",
                    Modifier
                        .clickable { showProjectPicker() }
                        .padding(end = 12.dp)
                        .weight(2F)
                )
                project?.title?.let { it1 ->
                    Text(
                        text = it1, Modifier
                            .weight(4F)
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
            Row(
                Modifier.padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ButtonUsers(
                    singleUser = false,
                    onClicked = { showTeamPicker() }
                )

                RowTeamMember(
                    list = listChosenUsers,
                    Modifier
                        .weight(4F)
                )
            }


        } else {
            Row(Modifier.padding(vertical = 12.dp)) {
                Text(
                    text = "",
                )
            }
        }



        if (listMilestones?.isNotEmpty() == true) {
            Row(Modifier.padding(vertical = 12.dp)) {
                Text(
                    text = "Milestone",
                    Modifier
                        .clickable { showMilestonePicker() }
                        .padding(end = 12.dp)
                        .weight(2F)
                )
                milestone?.title?.let { it1 ->
                    Text(
                        text = it1, Modifier
                            .weight(4F)
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
