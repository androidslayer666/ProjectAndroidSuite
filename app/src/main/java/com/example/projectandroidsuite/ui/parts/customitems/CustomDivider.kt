package com.example.projectandroidsuite.ui.parts.customitems

import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomDivider(start: Int = 55) {
    Divider(
        color = MaterialTheme.colors.primary,
        thickness = 2.dp,
        startIndent = start.dp,
        modifier = Modifier.width(200.dp)
    )
}