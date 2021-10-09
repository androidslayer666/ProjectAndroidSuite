package com.example.projectandroidsuite.ui.parts

import androidx.compose.runtime.Composable
import com.example.projectandroidsuite.ui.parts.customitems.CustomDialog

@Composable
fun ConfirmationDialog(
    text: String,
    onSubmit: () -> Unit,
    closeDialog: () -> Unit
) {
    CustomDialog(
        show = true,
        hide = { closeDialog() },
        text = text,
        onSubmit = { onSubmit() }) {
    }
}