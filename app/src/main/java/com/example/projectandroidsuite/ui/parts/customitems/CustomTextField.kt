package com.example.projectandroidsuite.ui.parts.customitems

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomTextField(
    modifier: Modifier? = null,
    value: String? = "",
    onValueChange: (text: String) -> Unit
    ) {
    BasicTextField(
        modifier = modifier?: Modifier,
        value = value ?: "",
        onValueChange = { text -> onValueChange(text) },
        decorationBox = { innerTextField ->
            Row(
                Modifier
                    .background(Color.Gray)
                    .height(30.dp)
                    .padding(4.dp)
            ) {
                innerTextField()  //<-- Add this
            }
        },
    )
}