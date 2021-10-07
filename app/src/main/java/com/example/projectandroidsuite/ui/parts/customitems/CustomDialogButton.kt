package com.example.projectandroidsuite.ui.parts.customitems

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.projectandroidsuite.R

@Composable
fun CustomDialogButton(
    onClick: () -> Unit,
    text: String,
    typeConfirm : Boolean
) {
    Surface(
        color = MaterialTheme.colors.primary,
        modifier = Modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.defaultMinSize(minHeight = 30.dp)
        ) {
            Spacer(Modifier.size(12.dp))
            Text(text, style = MaterialTheme.typography.caption, modifier = Modifier.weight(3F))
            Spacer(Modifier.size(12.dp))
            Image(
                painterResource(if(typeConfirm) R.drawable.ic_project_status_done else R.drawable.window_close),
                "",
                modifier = Modifier.weight(1F)
            )
        }
    }
}