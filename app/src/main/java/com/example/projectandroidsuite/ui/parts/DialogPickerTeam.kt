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
import com.example.database.entities.UserEntity
import com.example.projectandroidsuite.logic.PickerType

@Composable
fun TeamPickerDialog(
    list: List<UserEntity>,
    onSubmitList: () -> Unit,
    onClick: (user: UserEntity) -> Unit,
    closeDialog: () -> Unit,
    pickerType: PickerType,
    searchString: String,
    onSearchChanged: (String) -> Unit
) {

    AlertDialog(
        onDismissRequest = {
            closeDialog()
        },
        title = {
            when (pickerType) {
                PickerType.SINGLE -> Text(text = "Choose responsible")
                PickerType.MULTIPLE -> Text(text = "Choose team")
            }

        },
        text = {
            TeamPickerBody(
                list = list, { user -> onClick(user) }, pickerType,
                searchString, onSearchChanged, closeDialog
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    onSubmitList()
                    closeDialog()
                }, modifier = Modifier.width(100.dp)
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    closeDialog()
                }, modifier = Modifier.width(100.dp)
            ) {
                Text("Dismiss")
            }
        }
    )
}

@Composable
fun TeamPickerBody(
    list: List<UserEntity>,
    onClick: (name: UserEntity) -> Unit,
    pickerType: PickerType,
    searchString: String,
    onSearchChanged: (String) -> Unit,
    closeDialog: () -> Unit
) {

    Column {
        TextField(value = searchString, onValueChange = onSearchChanged)
        LazyColumn {
            items(list) { user ->
                var chosen by remember { mutableStateOf(user.chosen == true) }
                Row(Modifier.padding(12.dp)) {
                    when (pickerType) {
                        PickerType.SINGLE -> {
                        }
                        PickerType.MULTIPLE -> if (chosen) {
                            Image(imageVector = Icons.Default.Star, contentDescription = "star")
                        }
                    }
                    Text(text = user.displayName, Modifier.clickable {
                        when (pickerType) {
                            PickerType.SINGLE -> {
                                onClick(user)
                                closeDialog()
                            }
                            PickerType.MULTIPLE -> {
                                onClick(user)
                                chosen = !chosen
                            }
                        }
                    })
                }
            }
        }
    }
}