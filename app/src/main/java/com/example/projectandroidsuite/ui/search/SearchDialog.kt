package com.example.projectandroidsuite.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.projectandroidsuite.ui.parts.customitems.CustomDivider

@Composable
fun SearchDialog(
    viewModel: SearchViewModel,
    navController: NavHostController,
    closeDialog: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            closeDialog()
            viewModel.clearSearchString()
        },
        title = {
            Text(text = "Search")
        },
        text = {
            SearchBody(viewModel, navController)
        },
        confirmButton = {},
        dismissButton = {}
    )
}

@Composable
fun SearchBody(
    viewModel: SearchViewModel,
    navController: NavHostController
) {
    val searchString by viewModel.searchString.observeAsState("")
    val projects by viewModel.projects.observeAsState(listOf())
    val tasks by viewModel.tasks.observeAsState(listOf())
    val milestones by viewModel.milestones.observeAsState(listOf())
    val files by viewModel.files.observeAsState(listOf())


    //todo animation and scroll the entire screen

    Column(Modifier.defaultMinSize(minHeight = 600.dp)) {
        TextField(
            value = searchString,
            onValueChange = { string -> viewModel.setSearchString(string) })
        if (!projects.isNullOrEmpty() && searchString.isNotEmpty()) {
            Text(
                text = "Projects",
                color = MaterialTheme.colors.background,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.primary)
            )
            LazyColumn (
                Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp)){
                items(projects!!) {
                    Text(text = it.title , Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("project/${it.id}") }
                        .padding(12.dp))
                    CustomDivider()
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        if (!tasks.isNullOrEmpty() && searchString.isNotEmpty()) {
            Text(
                text = "Tasks",
                color = MaterialTheme.colors.background,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.primary)
            )
            LazyColumn(
                Modifier
                    .fillMaxWidth()
                    ) {
                items(tasks!!) {
                    Text(text = it.title, Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("tasks/${it.id}") }
                        .padding(12.dp))
                    CustomDivider()
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        if (!milestones.isNullOrEmpty() && searchString.isNotEmpty()) {
            Text(
                text = "Milestones",
                color = MaterialTheme.colors.background,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.primary)
            )
            LazyColumn (
                Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp)){
                items(milestones!!) {
                    Text(text = it.title ?:"" , Modifier
                        .fillMaxWidth()
                        .padding(12.dp))
                    CustomDivider()
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        if (!files.isNullOrEmpty() && searchString.isNotEmpty()) {
            Text(
                text = "Files",
                color = MaterialTheme.colors.background,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.primary)
            )
            LazyColumn (
                Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp)){
                items(files!!) {
                    Text(text = it.title ?:"" , Modifier
                        .fillMaxWidth()
                        .padding(12.dp))
                    CustomDivider()
                }
            }
        }

    }
}