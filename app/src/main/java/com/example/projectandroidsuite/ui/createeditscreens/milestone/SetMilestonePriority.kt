package com.example.projectandroidsuite.ui.createeditscreens.milestone

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun SetMilestonePriority(
    priority: Boolean?,
    onPriorityToggled: (Boolean) -> Unit
) {
    Row(
        Modifier
            .padding(vertical = 12.dp)
    ) {
        Text(text = "Key milestone", modifier = Modifier.weight(2F))
        Checkbox(
            modifier = Modifier.weight(4F),
            checked = priority ?: false,
            onCheckedChange = { checked -> onPriorityToggled(checked) }
        )
    }
}