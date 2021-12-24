package com.example.projectandroidsuite.ui.loginpage

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.ui.utils.makeToast
import com.example.projectandroidsuite.ui.utils.validation.LoginInputState


@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun LoginPage(
    viewModel: LoginViewModel,
    navController: NavHostController
) {


    val uiState by viewModel.uiState.collectAsState()

    val navigation = LoginScreenNavigation(navController)

    //Log.d("LoginPage", uiState.toString())

    var currentTab by remember { mutableStateOf(LoginPages.PORTAL) }

    val (focusRequesterMail, focusRequesterPassword) = FocusRequester.createRefs()

    uiState.loginInputState.serverResponseError?.let {
        makeToast(it, LocalContext.current)
    }
    LaunchedEffect(key1 = uiState.loginInputState) {
        Log.d("LoginPage", uiState.loginInputState.toString())
        if (uiState.loginInputState.canConnectToPortal == true)
            currentTab = LoginPages.CREDENTIALS
        if (uiState.loginInputState.twoFactorAuth == true)
            currentTab = LoginPages.CODE
        if (uiState.loginInputState.authenticationIsComplete == true)
            navigation.navigateToMainScreen()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
    ) {

        when (currentTab) {
            LoginPages.PORTAL -> {
                LoginTextGreeting()

                PortalAddressInput(
                    uiState = uiState,
                    onChangeInput = { input ->
                        viewModel.onChangePortalAddress(input)
                    },
                    changeTab = { boolean -> viewModel.setPortalIsInCloud(boolean) },
                    onClickConnect = { viewModel.tryIfPortalExists() }
                )
            }
            LoginPages.CREDENTIALS -> {
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

                Button(onClick = { viewModel.authenticate() }) {
                    Text(text = stringResource(R.string.log_in))
                }

            }

            LoginPages.CODE -> {
                Text(stringResource(R.string.please_input_code_from_google))
                TextField(
                    value = uiState.tfaGoogleInput.toString(),
                    onValueChange = viewModel::setTfaGoogleInput
                )

                Button(onClick = { viewModel.authenticate() }) {
                    Text(text = stringResource(R.string.log_in))
                }
            }
        }

        LoginButtonNavigationRow(
            selectedTab = currentTab,
            setPage = { page ->
                changePageResolver(
                    loginInputState = uiState.loginInputState,
                    newPage = page,
                    setPage = { newPage -> currentTab = newPage })

            })
    }
}

fun changePageResolver(
    loginInputState: LoginInputState,
    newPage: LoginPages,
    setPage: (LoginPages) -> Unit
) {
    when (newPage) {
        LoginPages.PORTAL -> setPage(newPage)
        LoginPages.CREDENTIALS -> if (loginInputState.canConnectToPortal == true) setPage(newPage)
        LoginPages.CODE -> if (loginInputState.twoFactorAuth == true) setPage(newPage)
    }
}
