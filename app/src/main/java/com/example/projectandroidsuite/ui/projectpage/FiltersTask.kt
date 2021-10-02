package com.example.projectandroidsuite.ui.projectpage


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.logic.PickerType
import com.example.projectandroidsuite.logic.ProjectSorting
import com.example.projectandroidsuite.logic.TaskSorting
import com.example.projectandroidsuite.logic.TaskStatus
import com.example.projectandroidsuite.ui.parts.TeamMemberCard
import com.example.projectandroidsuite.ui.parts.TeamPickerDialog

@Composable
fun FilterTasks(
    viewModel: ProjectViewModel,
) {
    var showUserPicker by remember { mutableStateOf(false) }

    val listUsersFlow by viewModel.userListFlow.observeAsState()
    val userSearch by viewModel.userSearchProject.observeAsState("")
    val stage by viewModel.stageForFilteringTask.observeAsState()
    val user by viewModel.userForFilteringTask.observeAsState()
    val sorting by viewModel.taskSorting.observeAsState()

    Surface(
        elevation = 10.dp,
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomEnd = 16.dp,
            bottomStart = 16.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.primary)
        ) {
            Row {
                Text(text = "stage")

                Column(Modifier.selectableGroup()) {
                    Row() {
                        RadioButton(
                            (stage == TaskStatus.ACTIVE),
                            { viewModel.setStageForFilteringTask(TaskStatus.ACTIVE) }
                        )
                        Text(text = "Active")
                    }
                    Row() {
                        RadioButton(
                            (stage == TaskStatus.COMPLETE),
                            { viewModel.setStageForFilteringTask(TaskStatus.COMPLETE) }
                        )
                        Text(text = "Complete")
                    }
                }
            }
            listUsersFlow?.let {
                Row() {
                    Text(text = "Choose team", Modifier.clickable { showUserPicker = true })
                    user?.let { it1 -> TeamMemberCard(user = it1) }
                }
                if (showUserPicker) {
                    TeamPickerDialog(
                        list = it,
                        onSubmitList = { },
                        onClick = { user -> viewModel.setUserForFilteringTask(user) },
                        closeDialog = { showUserPicker = false },
                        pickerType = PickerType.SINGLE,
                        userSearch,
                        { query -> viewModel.setUserSearch(query) }
                    )
                }
            }
            Text(
                text = "ClearFilters",
                modifier = Modifier.clickable { viewModel.clearFiltersTask() })


            Text(
                text = "Sorting",
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier.padding(vertical = 12.dp)
            )
            Row {

                Row(Modifier.weight(2F)) {
                    Text(text = "Deadline ", color = MaterialTheme.colors.onPrimary)

                }
                Row(Modifier.weight(4F)) {

                    RadioButton(
                        (sorting == TaskSorting.DEADLINE_ASC),
                        { viewModel.setTaskSorting(TaskSorting.DEADLINE_ASC) }
                    )
                    Image(
                        painterResource(R.drawable.sort_variant),
                        contentDescription = "Status"
                    )

                    Spacer(modifier = Modifier.width(6.dp))
                    RadioButton(
                        (sorting == TaskSorting.DEADLINE_DESC),
                        { viewModel.setTaskSorting(TaskSorting.DEADLINE_DESC) }
                    )
                    Image(
                        painterResource(R.drawable.sort_reverse_variant),
                        contentDescription = "Status"
                    )
                }
            }

            Row {

                Row(
                    Modifier
                        .padding(bottom = 20.dp)
                        .weight(2F)
                ) {
                    Text(text = "Stage ", color = MaterialTheme.colors.onPrimary)
                }

                Row(Modifier.weight(4F)) {
                    RadioButton(
                        (sorting == TaskSorting.STAGE_ASC),
                        { viewModel.setTaskSorting(TaskSorting.STAGE_ASC) }
                    )
                    Image(
                        painterResource(R.drawable.sort_variant),
                        contentDescription = "Status"
                    )

                    Spacer(modifier = Modifier.width(6.dp))
                    RadioButton(
                        (sorting == TaskSorting.STAGE_DESC),
                        { viewModel.setTaskSorting(TaskSorting.STAGE_DESC) }
                    )
                    Image(
                        painterResource(R.drawable.sort_reverse_variant),
                        contentDescription = "Status"
                    )
                }
            }


            Row {

                Row(
                    Modifier
                        .padding(bottom = 20.dp)
                        .weight(2F)
                ) {
                    Text(text = "Importance", color = MaterialTheme.colors.onPrimary)
                }

                Row(Modifier.weight(4F)) {
                    RadioButton(
                        (sorting == TaskSorting.IMPORTANT_ASC),
                        { viewModel.setTaskSorting(TaskSorting.IMPORTANT_ASC) }
                    )
                    Image(
                        painterResource(R.drawable.sort_variant),
                        contentDescription = "Status"
                    )

                    Spacer(modifier = Modifier.width(6.dp))
                    RadioButton(
                        (sorting == TaskSorting.IMPORTANT_DESC),
                        { viewModel.setTaskSorting(TaskSorting.IMPORTANT_DESC) }
                    )
                    Image(
                        painterResource(R.drawable.sort_reverse_variant),
                        contentDescription = "Status"
                    )
                }
            }
        }
    }
}