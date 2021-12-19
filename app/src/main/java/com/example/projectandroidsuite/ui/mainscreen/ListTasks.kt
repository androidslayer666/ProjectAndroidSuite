package com.example.projectandroidsuite.ui.mainscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.domain.filters.task.TaskStatus
import com.example.domain.model.Task
import com.example.domain.model.TaskPriority
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.ui.utils.Constants.FORMAT_SHOW_DATE
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun TaskList(
    viewModel: TasksViewModel,
    navController: NavHostController
) {

    val uiState by viewModel.uiState.collectAsState()

    val navigation = MainScreenNavigation(navController)

    LazyColumn(Modifier.background(MaterialTheme.colors.background)) {
        items(uiState.tasks) { task ->
            TaskItem(task) { id -> navigation.navigateToTaskDetails(id) }
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    showStage: Boolean? = true,
    onClick: (taskId: Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 12.dp)
            .background(MaterialTheme.colors.background),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showStage == true)
            Image(
                painterResource(
                    when (task.status) {
                        TaskStatus.ACTIVE -> R.drawable.ic_project_status_active
                        TaskStatus.COMPLETE -> R.drawable.ic_project_status_done
                        else -> R.drawable.ic_project_status_active
                    }
                ),
                contentDescription = "Status",
                modifier = Modifier
                    .weight(0.3f)
                    .padding(end = 12.dp)
            )
        Column(modifier = Modifier
            .weight(2f)
            .clickable { onClick(task.id) }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(Modifier.size(6.dp))
                if (task.priority != null && task.priority == TaskPriority.HIGH) {
                    Image(
                        painterResource(
                            R.drawable.ic_baseline_flag_24
                        ),
                        contentDescription = "Status",
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                    )
                }
                Text(text = task.title,
                    style = MaterialTheme.typography.body1,
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    modifier = Modifier.clickable {
                        onClick(task.id)
                    })
            }
            Row {
                if (task.responsibles.isNotEmpty()) {
                    Text(
                        text = task.responsibles[0].displayName,
                        style = MaterialTheme.typography.overline,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }
                Text(
                    text = SimpleDateFormat(
                        FORMAT_SHOW_DATE,
                        Locale.getDefault()
                    ).format(task.deadline),
                    style = MaterialTheme.typography.overline
                )
            }
            Spacer(Modifier.size(6.dp))
        }
        Column(Modifier.weight(0.5f), horizontalAlignment = Alignment.CenterHorizontally) {
            task.subtasks?.let {
                Text(
                    textAlign = TextAlign.Center,
                    text = "${it.size} Subtasks",
                    style = MaterialTheme.typography.overline
                )
            }
        }
    }
    Row(Modifier.padding(horizontal = 12.dp)) {
        Column(Modifier.weight(0.3F)) {}
        Column(Modifier.weight(2F)) {
            Divider()
        }
        Column(Modifier.weight(0.5F)) {}
    }
}
