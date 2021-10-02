package com.example.projectandroidsuite.ui.parts.customitems

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.unit.dp

@Composable
fun DrawSideLine(
    enable: Boolean,
    color :Color,
    width: Float,
    content: @Composable () -> Unit
) {
    if(enable) {
        Column(modifier = Modifier
            .padding(6.dp)
            .drawWithContent {
                drawContent()
                clipRect {
                    drawLine(
                        brush = SolidColor(color),
                        strokeWidth = width,
                        cap = StrokeCap.Square,
                        start = Offset.Zero.copy(y = 0F),
                        end = Offset(x = 0F, y = size.height)
                    )
                }
            }
        ) {
            Column(Modifier.padding(6.dp)) {
                content()
            }
        }
    } else {
        content()
    }
}