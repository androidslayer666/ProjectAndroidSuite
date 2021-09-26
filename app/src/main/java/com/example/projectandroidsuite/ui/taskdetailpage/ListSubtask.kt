package com.example.projectandroidsuite.ui.taskdetailpage

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.database.entities.SubtaskEntity

@Composable
fun ListSubtask(listSubtask: List<SubtaskEntity>) {
    LazyColumn{
        items(listSubtask){ subtask ->
            subtask.title?.let { Text(text = it) }
            Divider(color = MaterialTheme.colors.primary, thickness = 2.dp, startIndent = 40.dp, modifier = Modifier.width(200.dp))
        }
    }
}