package com.example.projectandroidsuite.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.ui.parts.customitems.CustomDivider

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    navController: NavHostController,
    navigateBack: () -> Unit
) {
    val searchString by viewModel.searchString.collectAsState()
    val projects by viewModel.projects.collectAsState()
    val tasks by viewModel.tasks.collectAsState()
    val milestones by viewModel.milestones.collectAsState()
    val files by viewModel.files.collectAsState()

    val navigation = SearchNavigation(navController)


    Column(Modifier.defaultMinSize(minHeight = 600.dp)) {
        TextField(
            value = searchString,
            onValueChange = { string -> viewModel.setSearchString(string) },
            trailingIcon = {
                Image(painterResource(R.drawable.ic_cancel),
                    contentDescription = "clear text",
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .clickable {
                            viewModel.setSearchString("")
                        }
                )
            }
        )
        if (!projects.isNullOrEmpty() && searchString.isNotEmpty()) {
            SearchList(
                text = "Projects",
                list = projects.map { project -> Pair(project.title, project.id) },
                onClick = { id -> navigation.navigateToProjectDetails(id) }
            )
        }

        if (!tasks.isNullOrEmpty() && searchString.isNotEmpty()) {
            SearchList(
                text = "Tasks",
                list = tasks.map { task -> Pair(task.title, task.id) },
                onClick = { id -> navigation.navigateToTaskDetails(id) }
            )
        }

        if (!milestones.isNullOrEmpty() && searchString.isNotEmpty()) {
            SearchList(
                text = "Milestones",
                list = milestones.map { milestone -> Pair(milestone.title?:"", milestone.id) }
            )
        }

        if (!files.isNullOrEmpty() && searchString.isNotEmpty()) {
            SearchList(
                text = "Files",
                list = files.map { file -> Pair(file.title?:"", file.id) }
            )
        }
    }
}

@Composable
fun SearchList(
    text: String,
    list: List<Pair<String,Int>>,
    onClick: ((Int) -> Unit)? = null
) {
    Text(
        text = text,
        color = MaterialTheme.colors.background,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
    )
    LazyColumn(
        Modifier
            .fillMaxWidth()
            .padding(start = 12.dp)
    ) {
        items(list) {
            Text(text = it.first, Modifier
                .fillMaxWidth()
                .clickable { if(onClick != null) onClick(it.second)  }
                .padding(12.dp))
            CustomDivider()
        }
    }
}