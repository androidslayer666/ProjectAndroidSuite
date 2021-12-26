package com.example.projectandroidsuite.ui.parts.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.domain.model.User
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.ui.parts.CardTeamMember
import com.example.projectandroidsuite.ui.parts.customitems.CustomDialog
import com.example.projectandroidsuite.ui.utils.ComposeTestDescriptions.TEAM_PICKER_DIALOG
import com.example.projectandroidsuite.ui.utils.PickerType

@Composable
fun TeamPickerDialog(
    list: List<User>?,
    //onSubmit: () -> Unit,
    onClick: (user: User) -> Unit,
    closeDialog: () -> Unit,
    pickerType: PickerType,
    searchString: String = "",
    onSearchChanged: (String) -> Unit
) {

    CustomDialog(
        show = true,
        hide = { closeDialog() },
        text = when (pickerType) {
            PickerType.SINGLE -> stringResource(R.string.choose_responsible)
            PickerType.MULTIPLE -> stringResource(R.string.choose_team)
        },
        onSubmit = {
            closeDialog()
        }) {
        TeamPickerBody(
            list = list?:listOf(), { user -> onClick(user) }, pickerType,
            searchString, onSearchChanged, closeDialog
        )
    }
}

@Composable
fun TeamPickerBody(
    list: List<User>,
    onClick: (name: User) -> Unit,
    pickerType: PickerType,
    searchString: String = "",
    onSearchChanged: (String) -> Unit,
    closeDialog: () -> Unit
) {

    Column(
        Modifier.semantics { contentDescription = TEAM_PICKER_DIALOG }
    ) {
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
                            Image(
                                painterResource(id = R.drawable.ic_user_chosen),
                                contentDescription = ""
                            )
                        }
                    }
                    CardTeamMember(user = user, true)
                }
            }
        }
    }
}