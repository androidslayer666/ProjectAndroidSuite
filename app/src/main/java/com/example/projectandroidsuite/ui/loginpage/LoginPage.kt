package com.example.projectandroidsuite.ui.loginpage

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.ui.ProjectsScreens
import com.example.projectandroidsuite.ui.utils.makeToast


@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun LoginPage(
    viewModel: LoginViewModel,
    navController: NavHostController
) {

    val portalAddress by viewModel.portalAddress.collectAsState()
    val emailAddress by viewModel.emailInput.collectAsState()
    val password by viewModel.passwordInput.collectAsState()
    val tfaGoogleInput by viewModel.tfaGoogleInput.collectAsState()
    val portalIsInCloud by viewModel.portalIsInCloud.collectAsState()
    val loginInputState by viewModel.loginInputState.collectAsState()

    val (focusRequesterMail, focusRequesterPassword) = FocusRequester.createRefs()
    val titles = listOf(stringResource(R.string.cloud_portal), stringResource(R.string.local_portal))

    loginInputState.serverResponseError?.let {
        makeToast(it, LocalContext.current)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
    ) {
        AnimatedVisibility(
            loginInputState.isPortalValidated != true,
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
                selectedTabIndex = portalIsInCloud,
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
            isError = loginInputState.isPortalValidated != true && portalAddress.isNotEmpty()
        )
        if (loginInputState.isPortalValidated != true) {
            if (portalIsInCloud == 0) {
                Text(
                    text = stringResource(R.string.input_your_portal_address_in_a_format),
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier
                        .padding(start = 50.dp, end = 50.dp)
                        .align(Alignment.CenterHorizontally)
                )
            } else {
                Button(onClick = { viewModel.tryIfPortalExists(portalAddress) }) {
                    Text(stringResource(R.string.connect))
                }
            }
        }

        AnimatedVisibility(
            loginInputState.isPortalValidated == true,
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
                LoginEmailTextField(
                    emailAddressInput = emailAddress,
                    onChange = { input -> viewModel.onChangeEmail(input) },
                    isError = loginInputState.isEmailValidated  != true,
                    focusRequester = focusRequesterPassword,
                    passFocusTo = focusRequesterMail
                )
                LoginPasswordTextField(
                    password = password,
                    onChange = { input ->
                        viewModel.onChangePassword(input)
                    },
                    passFocusTo = focusRequesterPassword,
                    isError = loginInputState.isPasswordValidated != true
                )

                if (loginInputState.twoFactorAuth == true) {
                    Text(stringResource(R.string.please_input_code_from_google))
                    TextField(
                        value = tfaGoogleInput,
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
                    Text(text = stringResource(R.string.log_in))
                }
            }
        }
    }
}


