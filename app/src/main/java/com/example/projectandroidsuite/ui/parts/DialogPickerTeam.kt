package com.example.projectandroidsuite.ui.parts

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.domain.model.User
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.logic.PickerType
import com.example.projectandroidsuite.ui.parts.customitems.CustomDialog

@Composable
fun TeamPickerDialog(
    list: List<User>,
    onSubmit: () -> Unit,
    onClick: (user: User) -> Unit,
    closeDialog: () -> Unit,
    pickerType: PickerType,
    searchString: String,
    onSearchChanged: (String) -> Unit
) {

    CustomDialog(
        show = true,
        hide = { closeDialog() },
        text = when (pickerType) {
            PickerType.SINGLE -> "Choose responsible"
            PickerType.MULTIPLE -> "Choose team"
        },
        onSubmit = { onSubmit() }) {
        TeamPickerBody(
            list = list, { user -> onClick(user) }, pickerType,
            searchString, onSearchChanged, closeDialog
        )
    }
}

@Composable
fun TeamPickerBody(
    list: List<User>,
    onClick: (name: User) -> Unit,
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
                Row(
                    Modifier
                        .padding(12.dp)
                        .clickable {
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
                        },
                verticalAlignment = Alignment.CenterVertically
                    ) {

                    when (pickerType) {
                        PickerType.SINGLE -> {
                        }
                        PickerType.MULTIPLE -> if (chosen) {
                            Image(painterResource(id = R.drawable.ic_user_chosen), contentDescription = "star")
                        }
                    }
                    CardTeamMember(user = user, true)
                }
            }
        }
    }
}