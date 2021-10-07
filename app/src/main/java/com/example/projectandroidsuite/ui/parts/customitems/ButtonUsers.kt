package com.example.projectandroidsuite.ui.parts.customitems

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.projectandroidsuite.R

@Composable
fun ButtonUsers(
    singleUser: Boolean,
    onClicked: () -> Unit
) {
    Surface(
        shape= RoundedCornerShape(10.dp),
        color= MaterialTheme.colors.primaryVariant,
        modifier = Modifier
            .clickable { onClicked() }
            .size(80.dp,30.dp)
    ) {
        Image(
            painterResource(id = if (singleUser) R.drawable.account else R.drawable.account_group),
            contentDescription = ""
        )
    }

}