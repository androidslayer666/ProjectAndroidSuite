package com.example.projectandroidsuite.ui.parts.inputs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.model.User
import com.example.projectandroidsuite.ui.ProjectTheme
import com.example.projectandroidsuite.ui.parts.CardTeamMember
import com.example.projectandroidsuite.ui.parts.RowTeamMember
import com.example.projectandroidsuite.ui.parts.customitems.ButtonUsers

@Composable
fun ChooseUser(
    onClick: () -> Unit,
    responsible: User?,
    userIsEmpty: Boolean? = null
) {

    Row(
        Modifier
            .padding(vertical = 12.dp)
            .clickable { onClick() }) {
        ButtonUsers(
            singleUser = true,
            onClicked = { onClick() }
        )
        if (userIsEmpty == true) {
            ErrorMessage(condition = userIsEmpty, text = "please choose user")
        }
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
    team: List<User>?,
    teamIsEmpty: Boolean? = null
) {

    Row(
        Modifier
            .padding(vertical = 12.dp)
            .clickable { onClick() }) {
        ButtonUsers(
            singleUser = false,
            onClicked = { onClick() }
        )
        if (teamIsEmpty == true) {
            ErrorMessage(condition = teamIsEmpty, text = "please choose team")
        }
        team?.let { team ->
            Row(
                Modifier.weight(4F)
            ) {
                RowTeamMember(list = team)
            }
        }
    }
}

@Preview
@Composable
fun PreviewChooseTeam(
) {
    ProjectTheme {
        ChooseTeam(
            onClick = {},
            team = listOf(),
            teamIsEmpty = true
        )
    }
}