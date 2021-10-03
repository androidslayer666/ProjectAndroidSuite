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

@Composable
fun ConfirmationDialog(
    text: String,
    onSubmit: () -> Unit,
    closeDialog: () -> Unit
) {

    AlertDialog(
        onDismissRequest = {
            closeDialog()
        },
        title = {
                Text(text = text)
        },
        text = {
        },
        confirmButton = {
            Row {
                Surface(
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .weight(1F)
                        .clickable { closeDialog() },
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.defaultMinSize(minHeight = 30.dp)
                    ) {
                        Spacer(Modifier.size(12.dp))
                        Text("Dismiss", style = MaterialTheme.typography.caption)
                        Spacer(Modifier.size(12.dp))
                        Image(
                            painterResource(com.example.projectandroidsuite.R.drawable.window_close),
                            ""
                        )
                    }
                }
                Spacer(Modifier.size(12.dp))

                Surface(
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .weight(1F)
                        .clickable {
                            onSubmit()
                            closeDialog()
                        },
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.defaultMinSize(minHeight = 30.dp)
                    ) {
                        Spacer(Modifier.size(12.dp))
                        Text("Confirm", style = MaterialTheme.typography.caption)
                        Spacer(Modifier.size(12.dp))
                        Image(
                            painterResource(com.example.projectandroidsuite.R.drawable.ic_project_status_done),
                            ""
                        )
                    }
                }
            }
        },
        dismissButton = { }
    )
}