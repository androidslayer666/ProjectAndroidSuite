package com.example.domain.utils

import android.util.Log

fun log(what: Any? = "empty", addition: String? = null, severity:Int =3){

    val stacktrace = Thread.currentThread().stackTrace
    val filename = stacktrace[4].fileName + "   "
    val add = addition ?: stacktrace[4].methodName

    when(severity) {
        3 -> Log.d(  filename + add, what.toString())
    }
}