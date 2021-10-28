package com.example.projectandroidsuite.ui.parts.customitems

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.projectandroidsuite.ui.utils.BackPressedHandler


@Composable
fun CustomDialog(
    show: Boolean,
    hide: () -> Unit,
    text: String,
    onSubmit: () -> Unit,
    onDeleteClick: (() -> Unit)? = null,
    showButtons: Boolean = true,
    content: @Composable () -> Unit
) {

    BackPressedHandler(true) {hide()}
    if (show) {
        Surface(
            color = Color.Black.copy(alpha = 0.8F),
            modifier = Modifier
                .fillMaxSize()
                .clickable { hide() }
        ) {}
        Surface(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.padding(horizontal = 30.dp, vertical = 30.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.primary)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .background(MaterialTheme.colors.primary)
                    ) {
                        Text(text,style = MaterialTheme.typography.h6)
                    }
                }
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colors.primary)
                        .padding(20.dp)
                ) {
                    content()
                    if(showButtons)
                    DialogButtonRow(
                        onSubmit = onSubmit,
                        closeDialog = hide,
                        onDeleteClick = onDeleteClick
                    )
                }
            }
        }
    }
}
