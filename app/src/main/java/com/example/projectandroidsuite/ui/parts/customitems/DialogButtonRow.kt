package com.example.projectandroidsuite.ui.parts.customitems

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.projectandroidsuite.R

@Composable
fun DialogButtonRow(
    onSubmit: () -> Unit,
    closeDialog: () -> Unit,
    onDeleteClick: (() -> Unit)? = null
){
    Row {
        if (onDeleteClick != null) {
            Image(
                painterResource(
                    R.drawable.ic_baseline_delete_36_red
                ),
                contentDescription = "",
                modifier = Modifier
                    .weight(0.7F)
                    .clickable { onDeleteClick() }
            )
        }
        Spacer(Modifier.size(12.dp))
        Row(modifier = Modifier.weight(1F)) {
            CustomDialogButton(
                onClick = { closeDialog() },
                text = "Dismiss",
                typeConfirm = false
            )
        }
        Spacer(Modifier.size(12.dp))
        Row(modifier = Modifier.weight(1F)) {
            CustomDialogButton(
                onClick = {
                    onSubmit()
                },
                text = "Confirm",
                typeConfirm = true
            )
        }
    }
}