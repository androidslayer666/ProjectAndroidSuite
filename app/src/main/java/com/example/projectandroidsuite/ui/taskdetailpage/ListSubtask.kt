package com.example.projectandroidsuite.ui.taskdetailpage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.database.entities.SubtaskEntity
import com.example.domain.model.Subtask
import com.example.projectandroidsuite.R

@Composable
fun ListSubtask(listSubtask: List<Subtask>) {
    LazyColumn {
        items(listSubtask) { subtask ->
            Row(
                Modifier.defaultMinSize(minHeight = 30.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painterResource(if (subtask.status == 1) R.drawable.ic_project_status_active else R.drawable.ic_project_status_done),
                    contentDescription = "Status",
                    modifier = Modifier.weight(1F)
                )
                Column(modifier = Modifier.weight(5F)) {
                    subtask.title?.let { Text(text = it) }
                }
            }

            Divider(
                color = MaterialTheme.colors.primary,
                thickness = 2.dp,
                startIndent = 40.dp,
                modifier = Modifier.width(200.dp)
            )
        }
    }
}