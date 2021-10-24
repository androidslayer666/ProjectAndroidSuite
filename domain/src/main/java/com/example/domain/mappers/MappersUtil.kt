package com.example.domain.mappers

import com.example.domain.Constants.FORMAT_API_DATE
import java.text.SimpleDateFormat
import java.util.*

fun String.stringToDate() : Date? {
    val format = SimpleDateFormat(FORMAT_API_DATE, Locale.getDefault())
    return format.parse(this)
}