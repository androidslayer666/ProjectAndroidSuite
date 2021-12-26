package com.example.projectandroidsuite.ui.parts.inputs

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun ErrorMessage(
    condition: Boolean?,
    text: String
) {
    if (condition == true) {
        Surface(color = MaterialTheme.colors.background) {
            Text(
                text = text,
                color = MaterialTheme.colors.error,
                fontSize = 15.sp
            )
        }
    }
}