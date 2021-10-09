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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun LoginTextGreeting() {
    val uriHandler = LocalUriHandler.current

    val annotatedLinkString: AnnotatedString = buildAnnotatedString {

        val str = "If you don't have one - get one!"
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
            annotation = "https://www.onlyoffice.com/cloud-office.aspx",
            start = startIndex,
            end = endIndex
        )
    }


    Column {


        Text(
            text = "Welcome!",
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.onPrimary,
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.CenterHorizontally)
        )

        Text(
            text = "This app works only with ONLYOFFICE Groups portal.",
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

