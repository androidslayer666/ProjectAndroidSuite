package com.example.projectandroidsuite.ui.projectpage


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.logic.*
import com.example.projectandroidsuite.ui.parts.TeamMemberCard
import com.example.projectandroidsuite.ui.parts.TeamPickerDialog
import com.example.projectandroidsuite.ui.parts.customitems.CustomButton
import com.example.projectandroidsuite.ui.parts.customitems.CustomSortButton

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
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .background(MaterialTheme.colors.primary)
                .padding(12.dp)
        ) {
            CustomButton(
                text = "Active",
                clicked = (stage == TaskStatus.ACTIVE),
                onClick = { viewModel.setStageForFilteringTask(TaskStatus.ACTIVE) })
            Spacer(Modifier.size(12.dp))
            CustomButton(
                text = "Complete",
                clicked = (stage == TaskStatus.COMPLETE),
                onClick = { viewModel.setStageForFilteringTask(TaskStatus.COMPLETE) })

            listUsersFlow?.let {
                Column (modifier = Modifier.padding(vertical = 12.dp)) {
                    Text(text = "Responsible", Modifier.clickable { showUserPicker = true })
                    Spacer(Modifier.size(12.dp))
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

            Surface(
                elevation = 10.dp,
                color = MaterialTheme.colors.primaryVariant
            ){
                Spacer(Modifier.size(12.dp))
                Text(
                    text = "ClearFilters",
                    modifier = Modifier.clickable { viewModel.clearFiltersTask()})
                Spacer(Modifier.size(12.dp))
            }

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
                    CustomSortButton(ascending = true, clicked = (sorting == TaskSorting.DEADLINE_ASC)
                    ) { viewModel.setTaskSorting(TaskSorting.DEADLINE_ASC) }
                    Spacer(modifier = Modifier.width(12.dp))
                    CustomSortButton(ascending = false, clicked = (sorting == TaskSorting.DEADLINE_DESC)
                    ) { viewModel.setTaskSorting(TaskSorting.DEADLINE_DESC) }
                }
            }

            Column() {
                Row(
                    Modifier
                        .padding(bottom = 20.dp)
                ) {
                    Text(text = "Stage ", color = MaterialTheme.colors.onPrimary)
                }

                Row() {
                    CustomSortButton(ascending = true, clicked = (sorting == TaskSorting.STAGE_ASC)
                    ) { viewModel.setTaskSorting(TaskSorting.STAGE_ASC) }
                    Spacer(modifier = Modifier.width(12.dp))
                    CustomSortButton(ascending = false, clicked = (sorting == TaskSorting.STAGE_DESC)
                    ) { viewModel.setTaskSorting(TaskSorting.STAGE_DESC) }
                }
            }


            Column() {
                Row(
                    Modifier
                        .padding(bottom = 20.dp)
                ) {
                    Text(text = "Importance", color = MaterialTheme.colors.onPrimary)
                }
                Row() {
                    CustomSortButton(ascending = true, clicked = (sorting == TaskSorting.IMPORTANT_ASC)
                    ) { viewModel.setTaskSorting(TaskSorting.IMPORTANT_ASC) }
                    Spacer(modifier = Modifier.width(12.dp))
                    CustomSortButton(ascending = false, clicked = (sorting == TaskSorting.IMPORTANT_DESC)
                    ) { viewModel.setTaskSorting(TaskSorting.IMPORTANT_DESC) }
                }
            }
        }
    }
}