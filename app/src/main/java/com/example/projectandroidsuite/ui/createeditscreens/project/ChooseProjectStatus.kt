package com.example.projectandroidsuite.ui.createeditscreens.project

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.filters.project.ProjectStatus
import com.example.projectandroidsuite.ui.parts.customitems.CustomButton

@Composable
fun ChooseProjectStatus(
    status: ProjectStatus?,
    setStatus: (ProjectStatus) -> Unit

) {
    Row(Modifier.padding(vertical = 12.dp)) {
//        Row(
//            Modifier
//                .weight(4F)
//        )
//        {
        CustomButton(
            text = "Active",
            clicked = (status == ProjectStatus.ACTIVE),
            onClick = { setStatus(ProjectStatus.ACTIVE) })
        Spacer(Modifier.size(12.dp))
        CustomButton(
            text = "Paused",
            clicked = (status == ProjectStatus.PAUSED),
            onClick = { setStatus(ProjectStatus.PAUSED) })
        Spacer(Modifier.size(12.dp))
        CustomButton(
            text = "Stopped",
            clicked = (status == ProjectStatus.STOPPED),
            onClick = { setStatus(ProjectStatus.STOPPED) }
        )
    }

}