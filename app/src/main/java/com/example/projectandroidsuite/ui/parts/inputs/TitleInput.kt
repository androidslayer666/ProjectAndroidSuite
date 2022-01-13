package com.example.projectandroidsuite.ui.parts.inputs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.ui.parts.customitems.CustomTextField

@Composable
fun TitleInput(
    text: String,
    onInputChange: (String) -> Unit,
    textIsEmpty: Boolean? = null
) {

    Box(
        modifier = Modifier.padding(dimensionResource(R.dimen.mediumPadding))
    ) {
        CustomTextField(
            value = text,
            onValueChange = onInputChange,
            label = stringResource(R.string.title)
        )

        ErrorMessage(condition = textIsEmpty, text = "please enter title")
    }
}

@Composable
fun DescriptionInput(
    text: String,
    onInputChange: (String) -> Unit,
    textIsEmpty: Boolean? = null
) {
    Box(
        modifier = Modifier.padding(dimensionResource(R.dimen.mediumPadding))
    ) {
        CustomTextField(
            value = text,
            onValueChange = onInputChange,
            label = stringResource(R.string.description),
            numberOfLines = 3
        )

        ErrorMessage(condition = textIsEmpty, text = "please enter description")
    }
}


//@Preview
@Composable
fun PreviewDescriptionInput() {
    DescriptionInput(
        "text",
        {},
        true
    )
}