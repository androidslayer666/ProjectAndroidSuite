package com.example.domain.fakes

import java.text.SimpleDateFormat
import java.util.*

object DummyDateList {
    val listDates = listOf(
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse("01/11/2021"),
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse("02/11/2021"),
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse("03/11/2021"),
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse("04/11/2021")
    )
}