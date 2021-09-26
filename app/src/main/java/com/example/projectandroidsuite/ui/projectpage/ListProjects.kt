package com.example.projectandroidsuite.ui.projectpage

import android.util.Log
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
import com.example.database.entities.ProjectEntity
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.logic.swipeToRefresh
import com.example.projectandroidsuite.ui.parts.customitems.CustomDivider


@Composable
fun ProjectList(
    viewModel: ProjectViewModel,
    navController: NavHostController
) {
    val list by viewModel.projects.observeAsState(listOf())
    val problemWithFetchingProjects by viewModel.problemWithFetchingProjects.observeAsState()

    if(problemWithFetchingProjects != null){
        Text(text = problemWithFetchingProjects!!)
    }
    
    LazyColumn(Modifier.background(MaterialTheme.colors.background)){
        list?.let {
            items(list!!) { project ->
                ProjectItem(project) { id -> navController.navigate("projects/$id") }
                CustomDivider()
            }
        }
    }
}

@Composable
fun ProjectItem(project: ProjectEntity, onClick: (projectId: Int) -> Unit) {

    var responsibleString = project.responsible?.displayName
    val participantsNumber = project.participantCount
    if (participantsNumber != null) {
        if (participantsNumber > 1) responsibleString += "+ ${(participantsNumber - 1)}"
    }

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)
        .background(MaterialTheme.colors.background))
    {
        Image(
            painterResource(R.drawable.ic_project_status_active),
            contentDescription = "Status",
            modifier = Modifier.weight(0.3f)
        )

        Column(Modifier.weight(3f).clickable { onClick(project.id) }) {
            project.title.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.body1, maxLines = 1, overflow = TextOverflow.Clip,
                )
            }
            if (responsibleString != null) {
                Text(text = responsibleString,
                    style = MaterialTheme.typography.overline)
            }
        }
        Column(Modifier.weight(0.5f)) {
            project.taskCount?.let {
                Text(
                    text = "$it Tasks",
                    style = MaterialTheme.typography.overline
                )
            }
        }
    }
}
