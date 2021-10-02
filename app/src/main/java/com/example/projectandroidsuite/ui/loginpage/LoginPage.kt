package com.example.projectandroidsuite.ui.loginpage

import android.util.Log
import android.view.KeyEvent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.projectandroidsuite.ui.ProjectsScreens

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginPage(
    viewModel: LoginViewModel,
    navController: NavHostController
) {

    val portalAddress by viewModel.portalAddress.observeAsState("")
    val portalAddressForLocal by viewModel.portalAddressForLocal.observeAsState("")
    val emailAddress by viewModel.emailInput.observeAsState("")
    val password by viewModel.passwordInput.observeAsState("")
    val addressIsValid by viewModel.canConnectToPortal.observeAsState(false)

    val portalIsInCloud by viewModel.portalIsInCloud.observeAsState()

    val (focusRequesterMail, focusRequesterPassword) = FocusRequester.createRefs()

    val uriHandler = LocalUriHandler.current

    val titles = listOf("Cloud portal", "Local portal")
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

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        if (!addressIsValid) {

            Text(
                text = "Welcome!",
                style = MaterialTheme.typography.h5,
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Text(
                text = "This app works only with ONLYOFFICE Groups portal.",
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


        Row {
            TabRow(
                selectedTabIndex = portalIsInCloud ?: 0,
                modifier = Modifier.size(width = 300.dp, height = 50.dp)
            ) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = portalIsInCloud == index,
                        onClick = { viewModel.setPortalIsInCloud(index) }
                    )
                }
            }
        }

        TextField(
            value = portalAddress,
            onValueChange = { input ->
                viewModel.onChangePortalAddress(input)
            },
            singleLine = true,
            modifier = Modifier
                .defaultMinSize(minWidth = 300.dp)
                .padding(bottom = 20.dp),
            colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.background)
        )
        if (!addressIsValid) {
            if (portalIsInCloud == 0) {
                Text(
                    text = "Input your portal address in a format : xxxxxx.onlyoffice.com/sg/eu",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier
                        .padding(start = 50.dp, end = 50.dp)
                        .align(Alignment.CenterHorizontally)
                )
            } else {
                Button(onClick = { viewModel.tryIfPortalExists() }) {
                    Text("Connect")
                }
            }
        }

        if (addressIsValid) {
            TextField(
                value = emailAddress,
                onValueChange = { input ->
                    viewModel.onChangeEmail(input)
                },
                label = { Text("Email") },
                colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.background),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { focusRequesterPassword.requestFocus() }
                ),
                modifier = Modifier
                    .defaultMinSize(minWidth = 300.dp)
                    .padding(20.dp)
                    .focusRequester(focusRequesterMail)
                    .onKeyEvent {
                        if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                            focusRequesterPassword.requestFocus()
                            true
                        }
                        false
                    })
            TextField(
                value = password,
                onValueChange = { input ->
                    viewModel.onChangePassword(input)
                },
                singleLine = true,
                label = { Text("Password") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { focusRequesterPassword.requestFocus() }
                ),
                modifier = Modifier
                    .defaultMinSize(minWidth = 300.dp)
                    .padding(20.dp)
                    .focusRequester(focusRequesterPassword)
                    .onKeyEvent {
                        if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                            viewModel.authenticate { navController.navigate(ProjectsScreens.Projects.name) }
                            true
                        } else false
                    },
                colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.background)
            )

            Button(onClick = {
                viewModel.authenticate { navController.navigate(ProjectsScreens.Projects.name) }
                Log.d("authenticated", viewModel.authenticated.value.toString())
            }) {
                Text(text = "Log In")
            }
        }

    }
}