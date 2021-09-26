package com.example.projectandroidsuite.ui.search

import androidx.compose.foundation.background
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
import com.example.projectandroidsuite.ui.parts.customitems.CustomDivider

@Composable
fun SearchDialog(
    viewModel: SearchViewModel,
    closeDialog: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            closeDialog()
        },
        title = {
            Text(text = "Search")
        },
        text = {
            SearchBody(viewModel)
        },
        confirmButton = {},
        dismissButton = {}
    )
}

@Composable
fun SearchBody(
    viewModel: SearchViewModel
) {
    val searchString by viewModel.searchString.observeAsState("")
    val projects by viewModel.projects.observeAsState(listOf())
    val tasks by viewModel.tasks.observeAsState(listOf())

    Column(Modifier.defaultMinSize(minHeight = 400.dp)) {
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
                        .padding(12.dp))
                    CustomDivider()
                }
            }
        }
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
                        .padding(12.dp))
                    CustomDivider()
                }
            }
        }


    }
}