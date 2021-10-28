package com.example.projectandroidsuite.ui.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.constrainWidth


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