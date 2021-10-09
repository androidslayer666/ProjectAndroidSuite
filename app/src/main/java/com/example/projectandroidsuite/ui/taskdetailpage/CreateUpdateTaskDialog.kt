package com.example.projectandroidsuite.ui.taskdetailpage

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.database.entities.TaskEntity
import com.example.domain.repository.Success
import com.example.projectandroidsuite.logic.PickerType
import com.example.projectandroidsuite.logic.TaskStatus
import com.example.projectandroidsuite.logic.makeToast
import com.example.projectandroidsuite.ui.parts.*
import com.example.projectandroidsuite.ui.parts.customitems.*
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

    var showTeamPicker by remember { mutableStateOf(false) }
    var showProjectPicker by remember { mutableStateOf(false) }
    var showMilestonePicker by remember { mutableStateOf(false) }
    val listMilestones by viewModel.milestonesList.observeAsState()
    val listProjects by viewModel.projectList.observeAsState()
    val listUsersFlow by viewModel.userList.observeAsState()
    val project by viewModel.project.observeAsState()

    val projectSearch by viewModel.projectSearchQuery.observeAsState("")
    val userSearch by viewModel.userSearchQuery.observeAsState("")

    Log.d("CreateTaskDialog ", "Task creating " + taskCreationStatus.toString())

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
        hide = { closeDialog() },
        text = if (task == null) "Create task" else "Update task",
        onSubmit = {
            if (task == null) {
                viewModel.createTask()
            } else {
                viewModel.updateTask()
            }
        },
        onDeleteClick = onDeleteClick
    ) {
        CreateTaskDialogInput(
            viewModel,
            { showProjectPicker = true },
            { showTeamPicker = true },
            { showMilestonePicker = true },
            task == null
        )
    }

    if (showProjectPicker) {
        ProjectPickerDialog(
            list = listProjects!!,
            onSubmit = { showProjectPicker = false },
            onClick = { project ->
                run {
                    viewModel.setProject(project = project)
                }
            },
            closeDialog = { showProjectPicker = false },
            ifChooseResponsibleOrTeam = PickerType.SINGLE,
            searchString = projectSearch,
            onSearchChanged = { query -> viewModel.setProjectSearch(query) }
        )
    }

    if (showTeamPicker) {
        TeamPickerDialog(
            list = listUsersFlow!!,
            onSubmit = { showTeamPicker = false},
            onClick = { user ->
                run {
                    viewModel.addOrRemoveUser(user)
                }
            },
            closeDialog = { showTeamPicker = false },
            pickerType = PickerType.MULTIPLE,
            searchString = userSearch,
            onSearchChanged = { query -> viewModel.setUserSearch(query)}
        )
    }

    if (showMilestonePicker && project != null){
        makeToast("the project doesn't have milestones", LocalContext.current)
        showMilestonePicker = false
    }

    listMilestones?.let {
        if (showMilestonePicker && listMilestones?.isNotEmpty() == true) {
            DialogPickerMilestone(
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




}

@Composable
fun CreateTaskDialogInput(
    viewModel: TaskCreateEditViewModel,
    showProjectPicker: () -> Unit,
    showTeamPicker: () -> Unit,
    showMilestonePicker: () -> Unit,
    modeCreate: Boolean
) {

    var showDatePicker by remember { mutableStateOf(false) }

    val title by viewModel.title.observeAsState("")
    val description by viewModel.description.observeAsState("")
    val listProjects by viewModel.projectList.observeAsState()
    val project by viewModel.project.observeAsState()
    val listUsersFlow by viewModel.userList.observeAsState()
    val listChosenUsers by viewModel.chosenUserList.observeAsState(mutableListOf())
    val endDate by viewModel.endDate.observeAsState(Date())

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



        if (listMilestones != null) {
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
