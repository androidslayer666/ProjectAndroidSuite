package com.example.projectandroidsuite.ui.parts

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.ui.parts.customitems.CustomTextField

@Composable
fun TitleInput(
    text : String,
    onInputChange : (String) -> Unit
) {
    CustomTextField(
        value = text,
        onValueChange = onInputChange,
        label = stringResource(R.string.title)
        )
}

@Composable
fun DescriptionInput(
    text : String,
    onInputChange : (String) -> Unit
) {
    CustomTextField(
        value = text,
        onValueChange = onInputChange,
        label = stringResource(R.string.description),
        numberOfLines = 3
    )

}
