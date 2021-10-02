package com.example.projectandroidsuite.logic

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
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

fun Modifier.firstBaselineToTop(
    firstBaselineToTop: Dp
) = layout { measurable, constraints ->
    // Measure the composable
    val placeable = measurable.measure(constraints)

    // Check the composable has a first baseline
    check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
    val firstBaseline = placeable[FirstBaseline]

    // Height of the composable with padding - first baseline
    val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
    val height = placeable.height + placeableY
    layout(placeable.width, height) {
        // Where the composable gets placed
        placeable.placeRelative(0, placeableY)
    }
}
