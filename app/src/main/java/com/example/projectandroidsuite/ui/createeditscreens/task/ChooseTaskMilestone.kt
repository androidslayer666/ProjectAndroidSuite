package com.example.projectandroidsuite.ui.createeditscreens.task

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.model.Milestone

@Composable
fun ChooseTaskMilestone(
    onClick: ()-> Unit,
    milestone: Milestone?
) {
    Row(Modifier.padding(vertical = 12.dp)) {
        Text(
            text = "Milestone",
            Modifier
                .clickable { onClick() }
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
}