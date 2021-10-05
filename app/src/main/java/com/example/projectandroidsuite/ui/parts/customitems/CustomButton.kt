package com.example.projectandroidsuite.ui.parts.customitems

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun CustomButton(
    text: String,
    clicked: Boolean,
    onClick: () -> Unit
) {
    Surface(
        color = if (clicked) MaterialTheme.colors.secondary else MaterialTheme.colors.primary,
        elevation = if (clicked) 0.dp else 10.dp,
        modifier = Modifier
            .clickable { onClick() }
            .size(width = 90.dp, height = 30.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text)

        }
    }
}

