package com.example.domain.mappers

import java.text.SimpleDateFormat
import java.util.*

fun String.stringToDate() : Date? {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    //Log.d("stringToDate", format.parse(this).toString())
    return format.parse(this)
}