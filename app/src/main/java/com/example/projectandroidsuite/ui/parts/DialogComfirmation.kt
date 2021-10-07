package com.example.projectandroidsuite.ui.parts

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.runtime.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.database.entities.UserEntity
import com.example.projectandroidsuite.logic.PickerType
import com.example.projectandroidsuite.ui.parts.customitems.CustomDialog
import com.example.projectandroidsuite.ui.parts.customitems.DialogButtonRow
import com.example.projectandroidsuite.ui.parts.customitems.CustomDialogButton as CustomDialogButton

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