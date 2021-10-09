package com.example.projectandroidsuite.ui.parts

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.database.entities.ProjectEntity
import com.example.projectandroidsuite.logic.PickerType
import com.example.projectandroidsuite.ui.parts.customitems.CustomDialog

@Composable
fun ProjectPickerDialog(
    list: List<ProjectEntity>,
    onSubmit: () -> Unit,
    onClick: (user: ProjectEntity) -> Unit,
    closeDialog: () -> Unit,
    ifChooseResponsibleOrTeam: PickerType,
    searchString: String,
    onSearchChanged: (String) -> Unit,
    problemWithFetchingProjects: String? = null
) {

    CustomDialog(
        show = true,
        hide = { closeDialog() },
        text = "Project",
        onSubmit = { onSubmit() },
        showButtons = false,
    ) {
        ProjectPickerBody(
            list = list, { project -> onClick(project)
                closeDialog()
                         }, ifChooseResponsibleOrTeam,
            searchString, onSearchChanged, problemWithFetchingProjects
        )
    }
}

@Composable
fun ProjectPickerBody(
    list: List<ProjectEntity>,
    onClick: (project: ProjectEntity) -> Unit,
    ifChooseResponsibleOrTeam: PickerType,
    searchString: String,
    onSearchChanged: (String) -> Unit,
    problemWithFetchingProjects: String? = null
) {
    Column {
        if (problemWithFetchingProjects != null) {
            Text(text = problemWithFetchingProjects!!)
        }
        TextField(value = searchString, onValueChange = onSearchChanged)
        LazyColumn {
            items(list) { name ->
                Row(Modifier.padding(12.dp)) {
                    var chosen by remember { mutableStateOf(false) }

                    when (ifChooseResponsibleOrTeam) {
                        PickerType.SINGLE -> {
                        }
                        PickerType.MULTIPLE -> if (chosen) {
                            Image(imageVector = Icons.Default.Star, contentDescription = "star")
                        }
                    }

                    Text(text = name.title, Modifier.clickable {
                        onClick(name)
                        chosen = !chosen
                    })
                }
            }
        }
    }
}