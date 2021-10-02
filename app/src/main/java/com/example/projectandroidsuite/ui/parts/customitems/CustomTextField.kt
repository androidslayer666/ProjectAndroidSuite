package com.example.projectandroidsuite.ui.parts.customitems

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomTextField(
    height:Int = 50,
    numberOfLines: Int = 1,
    label : String = "",
    value: String,
    onValueChange: ((text: String) -> Unit)
    ) {
    TextField(
        value = value,
        onValueChange = { input -> onValueChange(input) },
        label = {Text(label)},
        singleLine = numberOfLines == 1,
        maxLines = numberOfLines,
        modifier = Modifier.height(height = height.dp),
        colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.background)
    )
}