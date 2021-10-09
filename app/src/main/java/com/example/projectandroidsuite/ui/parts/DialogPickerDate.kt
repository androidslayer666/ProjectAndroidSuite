package com.example.projectandroidsuite.ui.parts

import android.view.ContextThemeWrapper
import android.widget.CalendarView
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.ui.parts.customitems.CustomDialogButton
import com.example.projectandroidsuite.ui.parts.customitems.DialogButtonRow
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun DatePicker(onDateSelected: (Date) -> Unit, onDismissRequest: () -> Unit) {
    var selDate by remember { mutableStateOf(Date()) }


    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        text =
        {
            CustomCalendarView(onDateSelected = {
                selDate = it
            })
        },
        buttons = {
            Row {
                Row(Modifier.weight(1F)) {}
                Row(Modifier.weight(3F)) {
                    CustomDialogButton(
                        onClick = {
                            onDateSelected(selDate)
                            onDismissRequest()
                        },
                        text = "Confirm",
                        typeConfirm = true
                    )
                }
                Row(Modifier.weight(1F)) {}

            }


        })

}


@Composable
fun CustomCalendarView(onDateSelected: (Date) -> Unit) {
    // Adds view to Compose
    AndroidView(
        modifier = Modifier.wrapContentSize(),
        factory = { context ->
            CalendarView(ContextThemeWrapper(context, R.style.CalenderViewCustom))
        },
        update = { view ->
            view.setOnDateChangeListener { _, year, month, day ->
                onDateSelected(
                    SimpleDateFormat("yyyy/MM/dd").parse("$year/${month + 1}/$day")
                )
            }
        }
    )
}