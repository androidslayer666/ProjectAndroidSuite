package com.example.projectandroidsuite.ui.loginpage

import android.view.KeyEvent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.projectandroidsuite.ui.ProjectsScreens


@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun LoginPage(
    viewModel: LoginViewModel,
    navController: NavHostController
) {

    val portalAddress by viewModel.portalAddress.observeAsState("")
    val emailAddress by viewModel.emailInput.observeAsState("")
    val password by viewModel.passwordInput.observeAsState("")
    val addressIsValid by viewModel.canConnectToPortal.observeAsState(false)
    val emailIsValid by viewModel.inputEmailValidated.observeAsState(true)
    val passwordIsValid by viewModel.inputPasswordValidated.observeAsState(true)
    val twoFactorAuth by viewModel.twoFactorAuth.observeAsState(false)
    val tfaGoogleInput by viewModel.tfaGoogleInput.observeAsState("")


    val portalIsInCloud by viewModel.portalIsInCloud.observeAsState()

    val (focusRequesterMail, focusRequesterPassword) = FocusRequester.createRefs()
    val titles = listOf("Cloud portal", "Local portal")

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
    ) {
        AnimatedVisibility(
            !addressIsValid,
            enter = slideInVertically(

                initialOffsetY = { fullHeight -> -fullHeight },
                animationSpec = tween(durationMillis = 400)
            ),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> -fullHeight },
                animationSpec = tween(durationMillis = 400)
            )
        ) {
            LoginTextGreeting()
        }

        Spacer(modifier = Modifier.size(12.dp))
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
        Spacer(modifier = Modifier.size(12.dp))

        TextField(
            value = portalAddress,
            onValueChange = { input ->
                viewModel.onChangePortalAddress(input)
            },
            singleLine = true,
            modifier = Modifier
                .defaultMinSize(minWidth = 300.dp)
                .padding(bottom = 20.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.background,
                textColor = MaterialTheme.colors.onBackground
            ),
            isError = !addressIsValid && portalAddress.isNotEmpty()
        )
        if (!addressIsValid) {
            if (portalIsInCloud == 0) {
                Text(
                    text = "Input your portal address in a format : xxxxxx.onlyoffice.com/sg/eu",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier
                        .padding(start = 50.dp, end = 50.dp)
                        .align(Alignment.CenterHorizontally)
                )
            } else {
                Button(onClick = { viewModel.tryIfPortalExists(portalAddress) }) {
                    Text("Connect")
                }
            }
        }

        AnimatedVisibility(
            addressIsValid,
            enter = slideInHorizontally(

                initialOffsetX = { fullHeight -> -fullHeight },
                animationSpec = tween(durationMillis = 400)
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { fullHeight -> -fullHeight },
                animationSpec = tween(durationMillis = 400)
            )
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                TextField(
                    value = emailAddress,
                    onValueChange = { input ->
                        viewModel.onChangeEmail(input)
                    },
                    label = { Text("Email") },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.background,
                        textColor = MaterialTheme.colors.onBackground
                    ),
                    isError = !emailIsValid,
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
                            }
                            false
                        })
                TextField(
                    value = password,
                    onValueChange = { input ->
                        viewModel.onChangePassword(input)
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    label = { Text("Password") },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusRequesterPassword.requestFocus() }
                    ),
                    modifier = Modifier
                        .defaultMinSize(minWidth = 300.dp)
                        .padding(20.dp)
                        .focusRequester(focusRequesterPassword)
                        .onKeyEvent {
                            if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                                viewModel.authenticate(onCompleteLogin = {
                                    navController.navigate(
                                        ProjectsScreens.Projects.name
                                    )
                                })
                                true
                            } else false
                        },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.background,
                        textColor = MaterialTheme.colors.onBackground
                    ),
                    isError = !passwordIsValid
                )


                if(twoFactorAuth) {
                    Text("please input code from Google Authenticator for your Onlyoffice login")
                    TextField(
                        value = tfaGoogleInput ?:"",
                        onValueChange = viewModel::setTfaGoogleInput
                    )
                }

                Button(onClick = {
                    viewModel.authenticate(onCompleteLogin = {
                        navController.navigate(
                            ProjectsScreens.Projects.name
                        )
                    })
                    //Log.d("authenticated", viewModel.authenticated.value.toString())
                }) {
                    Text(text = "Log In")
                }
            }
        }
    }
}


