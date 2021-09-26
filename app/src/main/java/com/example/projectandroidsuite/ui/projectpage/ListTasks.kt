package com.example.projectandroidsuite.ui.projectpage

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.database.entities.TaskEntity
import com.example.projectandroidsuite.R


@Composable
fun TaskList(
    viewModel: ProjectViewModel,
    navController: NavHostController
) {

    val list by viewModel.tasks.observeAsState()
    LazyColumn(Modifier.background(MaterialTheme.colors.background)){
        list?.let {
            items(list!!) { task ->
                TaskItem(task) { id -> navController.navigate("tasks/$id") }
                Divider(color =MaterialTheme.colors.primary, thickness = 2.dp, startIndent = 40.dp, modifier = Modifier.width(200.dp))
            }
        }
    }
}

@Composable
fun TaskItem(task: TaskEntity, onClick: (taskId: Int) -> Unit) {
    Row(modifier = Modifier

        .fillMaxWidth()
        .padding(12.dp)) {
        Image(
            painterResource(R.drawable.ic_project_status_active),
            contentDescription = "Status",
            modifier = Modifier.weight(0.3f)
        )
        Column(modifier = Modifier.weight(2f).clickable { onClick(task.id) }) {
            Text(text = task.title,
                style = MaterialTheme.typography.body1,
                maxLines = 1,
                overflow = TextOverflow.Clip,
                modifier = Modifier.clickable {
                    onClick(task.id)
                })
            if (task.responsibles.isNotEmpty()) {
                Text(
                    text = task.responsibles[0].displayName + " + " + (task.responsibles.size - 1),
                    style = MaterialTheme.typography.overline
                )
            }
        }
        Column(Modifier.weight(0.5f)) {
            task.subtasks?.let {
                Text(
                    text = "${it.size} Subtasks",
                    style = MaterialTheme.typography.overline
                )
            }
        }
    }
}
