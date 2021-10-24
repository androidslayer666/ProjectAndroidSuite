package com.example.projectandroidsuite.logic

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.constrainWidth
import coil.request.ImageRequest
import com.example.domain.Failure
import com.example.domain.Result
import com.example.domain.Success


enum class PickerType {
    MULTIPLE, SINGLE
}

fun coilRequestBuilder(uri: String, context: Context, portalAddress: String, token: String): ImageRequest {
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

fun showResultToast(
    result: Result<String, String>?,
    onSuccess: () -> Unit,
    context: Context
) {
        when (result) {
            is Success -> {
                Log.d("deleteProject", result.toString())
                makeToast((result).value, context)
                onSuccess()
            }
            is Failure -> {
                Log.d("deleteProject", result.toString())
                makeToast((result).reason, context)
            }
        }
}


@Composable
fun BackHandler(enabled: Boolean = true, onBack: () -> Unit) {
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

@SuppressWarnings( "deprecation" )
fun fromHtml(html: String?): Spanned {
    return when {
        html == null -> {
            // return an empty spannable if the html is null
            SpannableString("")
        }
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
            // FROM_HTML_MODE_LEGACY is the behaviour that was used for versions below android N
            // we are using this flag to give a consistent behaviour
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        }
        else -> {
            Html.fromHtml(html)
        }
    }
}