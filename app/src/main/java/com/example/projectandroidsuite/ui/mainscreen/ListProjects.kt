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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.domain.filters.project.ProjectStatus
import com.example.domain.model.Project
import com.example.projectandroidsuite.R


@Composable
fun ProjectList(
    viewModel: ProjectsViewModel,
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()

    val navigation = MainScreenNavigation(navController)

    LazyColumn(Modifier.background(MaterialTheme.colors.background)) {
        items(uiState.projects) { project ->
            ProjectItem(project) { id -> navigation.navigateToProjectDetails(id) }
        }
    }
}

@Composable
fun ProjectItem(project: Project, onClick: (projectId: Int) -> Unit) {

    var responsibleString = project.responsible?.displayName
    val participantsNumber = project.participantCount
    if (participantsNumber != null) {
        if (participantsNumber > 1) responsibleString += "+ ${(participantsNumber - 1)}"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .background(MaterialTheme.colors.background),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Image(
            painterResource(
                when (project.status) {
                    ProjectStatus.ACTIVE -> R.drawable.ic_project_status_active
                    ProjectStatus.STOPPED -> R.drawable.ic_project_status_done
                    ProjectStatus.PAUSED -> R.drawable.ic_project_status_paused
                    else -> R.drawable.ic_project_status_active
                }
            ),
            contentDescription = "Status",
            modifier = Modifier
                .weight(0.3f)
                .padding(end = 12.dp)
        )

        Column(
            Modifier
                .weight(2f)
                .fillMaxSize()
                .clickable { onClick(project.id) }) {
            Spacer(Modifier.size(6.dp))
            Text(
                text = project.title,
                style = MaterialTheme.typography.body1,
                maxLines = 1,
                overflow = TextOverflow.Clip,
            )
            if (responsibleString != null) {
                Text(
                    text = responsibleString,
                    style = MaterialTheme.typography.overline
                )
            }
            Spacer(Modifier.size(6.dp))
        }
        Column(Modifier.weight(0.5f), horizontalAlignment = Alignment.CenterHorizontally) {
            project.taskCount?.let {
                Text(
                    text = "$it Tasks",
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
