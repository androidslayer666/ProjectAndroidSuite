package com.example.projectandroidsuite.ui.parts

import android.view.ContextThemeWrapper
import android.widget.CalendarView
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.AlertDialog
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.logic.Constants.FORMAT_SHOW_DATE
import com.example.projectandroidsuite.ui.parts.customitems.CustomDialogButton
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
                    SimpleDateFormat(FORMAT_SHOW_DATE, Locale.getDefault()).parse("$day/${month + 1}/$year")?: Date()
                )
            }
        }
    )
}