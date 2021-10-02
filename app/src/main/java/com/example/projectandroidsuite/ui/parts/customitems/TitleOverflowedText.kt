package com.example.projectandroidsuite.ui.parts.customitems

import androidx.compose.foundation.clickable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow


@Composable
fun TitleOverflowedText(
    text: String,
    style: TextStyle = MaterialTheme.typography.h6
) {
    var showTitleOverflow by remember { mutableStateOf(true) }

    if (showTitleOverflow)
        Text(
            text = text,
            style = style,
            modifier = Modifier.clickable { showTitleOverflow = !showTitleOverflow },
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    else
        Text(
            text = text,
            style = style,
            modifier = Modifier.clickable { showTitleOverflow = !showTitleOverflow },
        )


}