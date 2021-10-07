package com.example.projectandroidsuite.logic

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.constrainWidth
import coil.request.ImageRequest
import com.example.domain.SessionManager
import com.example.domain.repository.Failure
import com.example.domain.repository.Result
import com.example.domain.repository.Success


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


fun showResultToast(
    result: Result<String,String>?,
    onSuccess: () -> Unit,
    context: Context
) {
        when (result) {
            is Success -> {
                Log.d("deleteProject", result.toString())
                makeToast((result as Success<String>).value, context)
                onSuccess()
            }
            is Failure -> {
                Log.d("deleteProject", result.toString())
                makeToast((result as Failure<String>).reason, context)
            }
        }

}


@Composable
public fun BackHandler(enabled: Boolean = true, onBack: () -> Unit) {
    // Safely update the current `onBack` lambda when a new one is provided
    val currentOnBack by rememberUpdatedState(onBack)
    // Remember in Composition a back callback that calls the `onBack` lambda
    val backCallback = remember {
        object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                currentOnBack()
            }
        }
    }
    // On every successful composition, update the callback with the `enabled` value
    SideEffect {
        backCallback.isEnabled = enabled
    }
    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current) {
        "No OnBackPressedDispatcherOwner was provided via LocalOnBackPressedDispatcherOwner"
    }.onBackPressedDispatcher
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, backDispatcher) {
        // Add callback to the backDispatcher
        backDispatcher.addCallback(lifecycleOwner, backCallback)
        // When the effect leaves the Composition, remove the callback
        onDispose {
            backCallback.remove()
        }
    }
}

fun Modifier.expandScrollingViewportWidthBy(extraWidth: Dp) =
    clipToBounds().layout { measurable, constraints ->
        require(constraints.hasBoundedWidth)
        val placeable = measurable.measure(
            constraints.copy(
                maxWidth = constraints.maxWidth + extraWidth.roundToPx()
            )
        )
        layout(
            constraints.constrainWidth(placeable.width),
            placeable.height
        ) {
            placeable.placeRelative(0, 0)
        }
    }