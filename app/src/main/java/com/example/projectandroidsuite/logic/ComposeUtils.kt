package com.example.projectandroidsuite.logic

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import coil.request.ImageRequest
import com.example.domain.SessionManager


enum class PickerType {
    MULTIPLE, SINGLE
}

fun coilRequestBuilder(uri: String, context: Context): ImageRequest {

    val prefs: SharedPreferences =
        context.getSharedPreferences("AuthToken", Context.MODE_PRIVATE)
    val portalAddress = prefs.getString(SessionManager.PORTAL_ADDRESS, null)?.dropLast(1)
    val token = prefs.getString(SessionManager.USER_TOKEN, null)

    return ImageRequest.Builder(context)
        .data("$portalAddress$uri")
        .addHeader(
            "Authorization",
            "Bearer $token"
        )
        .build()
}


fun makeToast(text: String, context: Context) {
    Toast.makeText(
        context, text,
        Toast.LENGTH_LONG
    ).show()
}
