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
    val uiState by viewModel.uiState.collectAsState()

    //Log.d("LoginPage", uiState.toString())

    val (focusRequesterMail, focusRequesterPassword) = FocusRequester.createRefs()
    val titles =
        listOf(stringResource(R.string.cloud_portal), stringResource(R.string.local_portal))

    uiState.loginInputState.serverResponseError?.let {
        makeToast(it, LocalContext.current)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
    ) {
        AnimatedVisibility(
            uiState.loginInputState.isPortalValidated != true,
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
                selectedTabIndex = if (uiState.portalIsInCloud) 0 else 1,
                modifier = Modifier.size(width = 300.dp, height = 50.dp)
            ) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = if (index == 0) uiState.portalIsInCloud else !uiState.portalIsInCloud,
                        onClick = { viewModel.setPortalIsInCloud(if (index == 0) uiState.portalIsInCloud else !uiState.portalIsInCloud) }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.size(12.dp))

        TextField(
            value = uiState.portalAddress,
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
            isError = uiState.loginInputState.isPortalValidated != true && uiState.portalAddress.isNotEmpty()
        )
        if (uiState.loginInputState.isPortalValidated != true) {
            if (uiState.portalIsInCloud) {
                Text(
                    text = stringResource(R.string.input_your_portal_address_in_a_format),
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier
                        .padding(start = 50.dp, end = 50.dp)
                        .align(Alignment.CenterHorizontally)
                )
            } else {
                Button(onClick = { viewModel.tryIfPortalExists(uiState.portalAddress) }) {
                    Text(stringResource(R.string.connect))
                }
            }
        }

        AnimatedVisibility(
            uiState.loginInputState.canConnectToPortal == true,
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
                    emailAddressInput = uiState.emailInput,
                    onChange = { input -> viewModel.onChangeEmail(input) },
                    isError = uiState.loginInputState.isEmailValidated != true,
                    focusRequester = focusRequesterPassword,
                    passFocusTo = focusRequesterMail
                )
                LoginPasswordTextField(
                    password = uiState.passwordInput,
                    onChange = { input ->
                        viewModel.onChangePassword(input)
                    },
                    passFocusTo = focusRequesterPassword,
                    isError = uiState.loginInputState.isPasswordValidated != true
                )

                if (uiState.loginInputState.twoFactorAuth == true) {
                    Text(stringResource(R.string.please_input_code_from_google))
                    TextField(
                        value = uiState.tfaGoogleInput.toString(),
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


