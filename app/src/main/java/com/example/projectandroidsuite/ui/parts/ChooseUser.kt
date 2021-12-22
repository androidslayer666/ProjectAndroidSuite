package com.example.projectandroidsuite.ui.parts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.model.User
import com.example.projectandroidsuite.ui.parts.customitems.ButtonUsers

@Composable
fun ChooseUser(
    onClick: () -> Unit,
    responsible: User?
) {

    Row(
        Modifier
            .padding(vertical = 12.dp)
            .clickable { onClick() }) {
        ButtonUsers(
            singleUser = true,
            onClicked = { onClick() }
        )
        responsible?.let { user ->
            Row(
                Modifier.weight(4F)
            ) {
                CardTeamMember(user = user)
            }
        }
    }
}


@Composable
fun ChooseTeam(
    onClick: () -> Unit,
    team: List<User>?
) {

    Row(
        Modifier
            .padding(vertical = 12.dp)
            .clickable { onClick() }) {
        ButtonUsers(
            singleUser = false,
            onClicked = { onClick() }
        )
        team?.let { team ->
            Row(
                Modifier.weight(4F)
            ) {
                RowTeamMember(list = team)
            }
        }
    }
}