package com.example.projectandroidsuite.ui.loginpage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectandroidsuite.R


@Composable
fun LoginTextGreeting() {
    val uriHandler = LocalUriHandler.current

    val annotatedLinkString: AnnotatedString = buildAnnotatedString {

        val str = stringResource(R.string.if_you_dont_have_one)
        val startIndex = str.indexOf("If")
        val endIndex = str.length
        append(str)
        addStyle(
            style = SpanStyle(
                color = Color(0xff64B5F6),
                fontSize = 14.sp,
                textDecoration = TextDecoration.Underline,
                fontStyle = MaterialTheme.typography.body1.fontStyle
            ), start = startIndex, end = endIndex
        )

        addStringAnnotation(
            tag = "URL",
            annotation = stringResource(R.string.onlyoffice_address_for_creating_portal),
            start = startIndex,
            end = endIndex
        )
    }


    Column {
        Text(
            text = stringResource(R.string.welcome),
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.onPrimary,
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.CenterHorizontally)
        )

        Text(
            text = stringResource(R.string.this_app_works_only),
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.CenterHorizontally)
        )
        ClickableText(
            text = annotatedLinkString,
            style = MaterialTheme.typography.caption,
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.CenterHorizontally),
            onClick = {
                annotatedLinkString
                    .getStringAnnotations("URL", it, it)
                    .firstOrNull()?.let { stringAnnotation ->
                        uriHandler.openUri(stringAnnotation.item)
                    }
            }
        )
    }
}

