package com.example.projectandroidsuite.ui.mainscreen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.filters.task.TaskStatus
import com.example.domain.model.User
import com.example.domain.sorting.TaskSorting
import com.example.projectandroidsuite.ui.parts.CardTeamMember
import com.example.projectandroidsuite.ui.parts.dialogs.TeamPickerDialog
import com.example.projectandroidsuite.ui.parts.customitems.ButtonUsers
import com.example.projectandroidsuite.ui.parts.customitems.CustomButton
import com.example.projectandroidsuite.ui.parts.customitems.CustomSortButton
import com.example.projectandroidsuite.ui.utils.PickerType

@Composable
fun FilterTasks(
    viewModel: TasksViewModel,
) {
    var showUserPicker by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    Box {
        Row {

            Row(Modifier.weight(3F)) {}

            Row(Modifier.weight(2F)) {
                Surface(
                    elevation = 10.dp,
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colors.primary)
                            .padding(12.dp)
                    ) {

                        SetTaskStatusForFiltering(
                            taskStatus = uiState.stageForFilteringTask,
                            setTaskStatus = { status -> viewModel.setStatusForFilteringTask(status) })

                        SetTaskUserForFiltering(
                            user = uiState.userForFilteringTask,
                            onClick = { showUserPicker = true }
                        )



                        Spacer(Modifier.size(12.dp))

                        ClearFilters {
                            viewModel.clearFiltersTask()
                        }

                        Spacer(Modifier.size(24.dp))

                        SetTaskSorting(
                            taskSorting = uiState.taskSorting,
                            setTaskSorting = { sorting -> viewModel.setTaskSorting(sorting)}
                        )
                    }
                }
            }

        }
        if (showUserPicker) {
            TeamPickerDialog(
                list = uiState.users,
                //onSubmit = { },
                onClick = { user -> viewModel.setUserForFilteringTask(user) },
                closeDialog = { showUserPicker = false },
                pickerType = PickerType.SINGLE,
                searchString = uiState.userSearchQuery,
                onSearchChanged = { query -> viewModel.setUserSearch(query) }
            )
        }
    }
}


@Composable
fun SetTaskStatusForFiltering(
    taskStatus: TaskStatus?,
    setTaskStatus: (TaskStatus) -> Unit
) {
    CustomButton(
        text = "Active",
        clicked = (taskStatus == TaskStatus.ACTIVE),
        onClick = { setTaskStatus(TaskStatus.ACTIVE) })
    Spacer(Modifier.size(12.dp))
    CustomButton(
        text = "Complete",
        clicked = (taskStatus == TaskStatus.COMPLETE),
        onClick = { setTaskStatus(TaskStatus.COMPLETE) })
    Spacer(Modifier.size(24.dp))


}


@Composable
fun SetTaskUserForFiltering(
    user: User?,
    onClick: () -> Unit

) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        ButtonUsers(
            singleUser = true,
            onClicked = { onClick() }
        )
        Spacer(Modifier.size(12.dp))
        user?.let { it1 -> CardTeamMember(user = it1) }
    }
}

@Composable
fun SetTaskSorting(
    taskSorting: TaskSorting,
    setTaskSorting: (TaskSorting) -> Unit
){
    Text(
        text = "Sorting",
        color = MaterialTheme.colors.onPrimary,
        modifier = Modifier.padding(vertical = 12.dp)
    )
    Column() {
        Row() {
            Text(text = "Deadline ", color = MaterialTheme.colors.onPrimary)
        }
        Row() {
            CustomSortButton(
                ascending = true,
                clicked = (taskSorting == TaskSorting.DEADLINE_ASC)
            ) { setTaskSorting(TaskSorting.DEADLINE_ASC) }
            Spacer(modifier = Modifier.width(12.dp))
            CustomSortButton(
                ascending = false,
                clicked = (taskSorting == TaskSorting.DEADLINE_DESC)
            ) { setTaskSorting(TaskSorting.DEADLINE_DESC) }
        }
    }

    Spacer(Modifier.size(24.dp))

    Column() {
        Row {
            Text(text = "Stage ", color = MaterialTheme.colors.onPrimary)
        }
        Row() {
            CustomSortButton(
                ascending = true,
                clicked = (taskSorting == TaskSorting.STAGE_ASC)
            ) { setTaskSorting(TaskSorting.STAGE_ASC) }
            Spacer(modifier = Modifier.width(12.dp))
            CustomSortButton(
                ascending = false,
                clicked = (taskSorting == TaskSorting.STAGE_DESC)
            ) { setTaskSorting(TaskSorting.STAGE_DESC) }
        }
    }

    Spacer(Modifier.size(24.dp))

    Column() {
        Row {
            Text(text = "Importance", color = MaterialTheme.colors.onPrimary)
        }
        Row() {
            CustomSortButton(
                ascending = true,
                clicked = (taskSorting == TaskSorting.IMPORTANT_ASC)
            ) { setTaskSorting(TaskSorting.IMPORTANT_ASC) }
            Spacer(modifier = Modifier.width(12.dp))
            CustomSortButton(
                ascending = false,
                clicked = (taskSorting == TaskSorting.IMPORTANT_DESC)
            ) { setTaskSorting(TaskSorting.IMPORTANT_DESC) }
        }
    }
}