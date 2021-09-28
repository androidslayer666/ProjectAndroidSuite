package com.example.projectandroidsuite.ui.parts

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

fun getYearList() = (Calendar.getInstance().get(Calendar.YEAR)..2023).map { it.toString() }
fun getMonthList() = (1..12).map { it.toString() }
fun getDayList() = (1..31).map { it.toString() }

@Composable
fun DatePickerDialog(
    date : Date = Date(),
    showDialog: Boolean,
    setDialogVisible: (Boolean) -> Unit,
    onConfirm: (date: Date) -> Unit
) {
    //Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    Log.d("setProject", date.toString())

    var day by remember { mutableStateOf(SimpleDateFormat("dd").format(date))}
    var month by remember { mutableStateOf(SimpleDateFormat("MM").format(date))}
    var year by remember { mutableStateOf(SimpleDateFormat("yyyy").format(date))}

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                setDialogVisible(false)
            },
            title = {
                Text(text = "Select a date")
            },
            text = {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DatePickerItemDropdownMenu(
                        initialText = Calendar.getInstance().get(Calendar.YEAR).toString(),
                        itemList = getYearList(),
                        onItemSelected = {
                            year = it
                        }
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    DatePickerItemDropdownMenu(
                        initialText = (Calendar.getInstance().get(Calendar.MONTH) + 1).toString(),
                        itemList = getMonthList(),
                        onItemSelected = {
                            month = it
                        }
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    DatePickerItemDropdownMenu(
                        initialText = Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString(),
                        itemList = getDayList(),
                        onItemSelected = {
                            day = it
                        }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    Log.d("setProject", date.year.toString())
                    Log.d("DatePickerDialog", "$year/$month/$day")
                    onConfirm( SimpleDateFormat("yyyy/MM/dd").parse("$year/$month/$day"))
                    setDialogVisible(false)
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(onClick = {
                    setDialogVisible(false)
                }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun DatePickerItemDropdownMenu(
    initialText: String,
    itemList: List<String>,
    onItemSelected: (String) -> Unit,
) {
    val (dropdownText, setDropdownText) = remember { mutableStateOf(initialText) }
    val (isMenuExpanded, setMenuExpanded) = remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable(onClick = {
            setMenuExpanded(true)
        })
    ) {
        Text(
            text = dropdownText,
            style = MaterialTheme.typography.body1
        )
        Spacer(Modifier.width(4.dp))
    }
    DropdownMenu(
        expanded = isMenuExpanded,
        onDismissRequest = {
            setMenuExpanded(false)
        }
    ) {
        itemList.forEach {
            DropdownMenuItem(onClick = {
                setDropdownText(it)
                setMenuExpanded(false)
                onItemSelected(it)
            }) {
                Text(
                    text = it,
                    style = MaterialTheme.typography.body1,
                )
            }
        }
    }
}