package com.example.projectandroidsuite.ui.parts

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.database.entities.UserEntity
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.ui.parts.customitems.TitleOverflowedText
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DetailHeaderWrapper(
    title: String?,
    description: String?,
    responsible: UserEntity? = null,
    team: List<UserEntity>?,
    endDate: Date? = null,
    taskStatus: Int? = null,
    projectStatus: Int? = null,
    project: String? = null,
    milestone: String? = null,
    priority: Int? = null,
    onEditClick: (()-> Unit)? = null,
    onStatusClicked: (()-> Unit)? = null
) {

    var showTitleOverflow by remember { mutableStateOf(false) }

    Column(modifier = Modifier.defaultMinSize(minHeight = 100.dp)) {
        Row() {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 6.dp)
                    .weight(5F)
            ) {
                if (projectStatus != null) {
                    Image(
                        painterResource(
                            when (projectStatus) {
                                0 -> R.drawable.ic_project_status_active
                                1 -> R.drawable.ic_project_status_done
                                2 -> R.drawable.ic_project_status_paused
                                else -> R.drawable.ic_project_status_active
                            }
                        ),
                        contentDescription = "",
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                    )
                }
                if (taskStatus != null) {
                    Image(
                        painterResource(
                            when (taskStatus) {
                                1 -> R.drawable.ic_project_status_active
                                2 -> R.drawable.ic_project_status_done
                                else -> R.drawable.ic_project_status_active
                            }
                        ),
                        contentDescription = "",
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                            .clickable {
                                if (onStatusClicked != null) onStatusClicked() else {
                                }
                            }
                    )
                }

                Row(Modifier.weight(5F), verticalAlignment = Alignment.CenterVertically) {
                    if (priority != null && priority == 1) {
                        Image(
                            painterResource(
                                R.drawable.ic_baseline_flag_24
                            ),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(horizontal = 2.dp)
                        )
                    }
                    TitleOverflowedText(title ?: "")
                }
            }
            Row(Modifier.weight(1F), verticalAlignment = Alignment.CenterVertically) {
                if(onEditClick != null)
                Image(
                    painterResource(
                        R.drawable.square_edit_outline_active
                    ),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .clickable { onEditClick() }
                )
                else
                    Image(
                        painterResource(
                            R.drawable.square_edit_outline_passive
                        ),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(top = 12.dp)
                    )
            }
        }
        Spacer(modifier = Modifier.size(6.dp))
        if (description != null && description.isNotEmpty()) {
            Row(modifier = Modifier.padding(top = 12.dp, start = 6.dp)) {
                TitleOverflowedText(
                    text = description,
                    style = MaterialTheme.typography.body1,
                )
            }
        }
        Spacer(modifier = Modifier.size(6.dp))
        if (responsible != null)
            Row(Modifier.padding(top = 12.dp)) {
                Text(text = "Responsible", Modifier.padding(end = 12.dp, start = 6.dp))
                CardTeamMember(responsible)
            }
        Spacer(modifier = Modifier.size(6.dp))
        Row {
            Text(text = "Team", Modifier.padding(end = 12.dp, top = 12.dp, start = 6.dp))
            team?.let { RowTeamMember(it, Modifier.padding(bottom = 12.dp, top = 12.dp)) }
        }
        Spacer(modifier = Modifier.size(6.dp))

        if (endDate != null)
            Row(Modifier.padding(vertical = 6.dp)) {
                Text(
                    text = "End date",
                    Modifier.padding(end = 12.dp, start = 6.dp)
                )
                Text(
                    text = SimpleDateFormat("dd/MM/yyyy").format(endDate),
                    Modifier
                )
            }
        Spacer(modifier = Modifier.size(6.dp))
        if (project != null) {
            Row(Modifier.padding(vertical = 6.dp)) {
                Text(text = "Project", Modifier.padding(end = 12.dp, start = 6.dp))
                Text(project)
            }
        }
        Spacer(modifier = Modifier.size(6.dp))
        if (milestone != null) {
            Row(Modifier.padding(vertical = 6.dp)) {
                Text(text = "Milestone", Modifier.padding(end = 12.dp, start = 6.dp))
                Text(milestone)
            }
        }

    }
}