package com.example.projectandroidsuite.ui.createeditscreens.task

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.filters.task.TaskStatus
import com.example.projectandroidsuite.ui.parts.customitems.CustomButton

@Composable
fun ChooseTaskStatus(
    taskStatus: TaskStatus?,
    setStatus: (TaskStatus) -> Unit
) {
    Row(Modifier.padding(vertical = 12.dp)) {
        CustomButton(
            text = "Active",
            clicked = (taskStatus == TaskStatus.ACTIVE),
            onClick = { setStatus(TaskStatus.ACTIVE) })
        Spacer(Modifier.size(12.dp))
        CustomButton(
            text = "Complete",
            clicked = (taskStatus == TaskStatus.COMPLETE),
            onClick = { setStatus(TaskStatus.COMPLETE) })

    }
}