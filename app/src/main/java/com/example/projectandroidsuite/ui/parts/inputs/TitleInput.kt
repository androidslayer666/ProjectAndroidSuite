package com.example.projectandroidsuite.ui.parts.inputs

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.ui.parts.customitems.CustomTextField

@Composable
fun TitleInput(
    text: String,
    onInputChange: (String) -> Unit,
    textIsEmptyWhenShouldnt: Boolean? = null
) {

    Box {
        CustomTextField(
            value = text,
            onValueChange = onInputChange,
            label = stringResource(R.string.title)
        )

        ErrorMessage(condition = textIsEmptyWhenShouldnt, text = "please enter title")
    }
}

@Composable
fun DescriptionInput(
    text: String,
    onInputChange: (String) -> Unit,
    textIsEmptyWhileShouldnt: Boolean? = null
) {
    CustomTextField(
        value = text,
        onValueChange = onInputChange,
        label = stringResource(R.string.description),
        numberOfLines = 3
    )

    ErrorMessage(condition = textIsEmptyWhileShouldnt, text = "please enter description")
}


@Preview
@Composable
fun showDescriptionInput() {
    DescriptionInput(
        "text",
        {},
        true
    )
}