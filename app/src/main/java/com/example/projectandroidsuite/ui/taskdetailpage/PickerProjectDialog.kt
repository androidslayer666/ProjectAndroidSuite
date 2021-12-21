package com.example.projectandroidsuite.ui.taskdetailpage

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.model.Project
import com.example.projectandroidsuite.ui.utils.PickerType
import com.example.projectandroidsuite.ui.parts.customitems.CustomDialog

@Composable
fun ProjectPickerDialog(
    list: List<Project>?,
    onSubmit: () -> Unit,
    onClick: (user: Project) -> Unit,
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
            list = list?:listOf(), { project -> onClick(project)
                closeDialog()
                         }, ifChooseResponsibleOrTeam,
            searchString, onSearchChanged, problemWithFetchingProjects
        )
    }
}

@Composable
fun ProjectPickerBody(
    list: List<Project>,
    onClick: (project: Project) -> Unit,
    ifChooseResponsibleOrTeam: PickerType,
    searchString: String,
    onSearchChanged: (String) -> Unit,
    problemWithFetchingProjects: String? = null
) {
    Column {
        problemWithFetchingProjects?.let {
            Text(text = it)
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