package com.example.projectandroidsuite.ui.parts.customitems

import android.graphics.PorterDuff
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import com.example.projectandroidsuite.R

@Composable
fun CustomSortButton(
    ascending: Boolean,
    clicked: Boolean,
    onClick: () -> Unit
) {
    Box(Modifier.clickable { onClick() }) {
        if (ascending) {
            if (clicked) {
                Image(painterResource(id = R.drawable.sort_variant_hinted), contentDescription = "")
            } else {
                Image(painterResource(id = R.drawable.sort_variant), contentDescription = "")
            }

        } else {
            if (clicked) {
                Image(
                    painterResource(id = R.drawable.sort_reverse_variant_hinted),
                    contentDescription = ""
                )
            } else {
                Image(
                    painterResource(id = R.drawable.sort_reverse_variant),
                    contentDescription = ""
                )
            }
        }
    }
}