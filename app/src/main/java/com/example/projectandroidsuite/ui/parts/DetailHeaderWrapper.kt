package com.example.projectandroidsuite.ui.parts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.database.entities.UserEntity

@Composable
fun DetailHeaderWrapper(
    title: String?,
    description: String?,
    responsible: UserEntity? = null,
    team: List<UserEntity>?
) {
    Column(modifier = Modifier.defaultMinSize(minHeight = 100.dp)) {
        Text(
            text = title ?: "",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(top = 12.dp, start = 6.dp)
        )
        if( description != null && description.isNotEmpty())
           {
            Text(
                text = description,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(top = 12.dp, start = 6.dp)
            )
        }
        if (responsible != null)
            Row(Modifier.padding(top = 12.dp)) {
                Text(text = "Responsible", Modifier.padding(end = 12.dp, start = 6.dp))
                TeamMemberCard(responsible)
            }
        Row {
            Text(text = "Team", Modifier.padding(end = 12.dp, top = 12.dp, start = 6.dp))
            team?.let { TeamMemberRow(it, Modifier.padding(bottom = 12.dp, top = 12.dp))}
        }
    }
}