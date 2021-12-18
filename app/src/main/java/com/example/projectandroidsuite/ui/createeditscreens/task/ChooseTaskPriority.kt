package com.example.projectandroidsuite.ui.createeditscreens.task

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.model.TaskPriority

@Composable
fun ChooseTaskPriority(
    priority: TaskPriority,
    setPriority: (TaskPriority) -> Unit
) {
    Row(Modifier.padding(vertical = 12.dp)) {
        Text(text = "Priority", modifier = Modifier.weight(2F))
        Checkbox(
            modifier = Modifier.weight(4F),
            checked = priority == TaskPriority.HIGH,
            onCheckedChange = { setPriority(if (it) TaskPriority.HIGH else TaskPriority.NORMAL) }
        )
    }
}