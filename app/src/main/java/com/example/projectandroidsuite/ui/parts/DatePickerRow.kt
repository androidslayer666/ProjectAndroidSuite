package com.example.projectandroidsuite.ui.parts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.projectandroidsuite.R
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DatePickerRow(
    toggleDatePicker : () -> Unit,
    endDate : Date
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.clickable { toggleDatePicker() }
    ){
        Text(text = stringResource(R.string.end_date))
        Text(text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(endDate))
    }
}