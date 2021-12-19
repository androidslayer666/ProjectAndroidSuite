package com.example.projectandroidsuite.ui.loginpage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.interactor.login.CheckIfAuthenticated
import com.example.domain.interactor.login.CheckPortalPossibilities
import com.example.domain.interactor.login.Login
import com.example.domain.interactor.login.RememberPortalAddress
import com.example.domain.utils.Failure
import com.example.domain.utils.Success
import com.example.projectandroidsuite.ui.utils.validation.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginScreenState(
    val portalAddress: String = "",
    val emailInput: String = "",
    val passwordInput: String = "",
    val portalIsInCloud: Boolean = true,
    val tfaGoogleInput: Int? = null,
    val loginInputState: LoginInputState = LoginInputState()
)


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val login: Login,
    private val checkPortalPossibilities: CheckPortalPossibilities,
    private val rememberPortalAddress: RememberPortalAddress,
    private val checkIfAuthenticated: CheckIfAuthenticated
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginScreenState())
    val uiState: StateFlow<LoginScreenState> = _uiState.asStateFlow()

    fun checkAuthentication(): Boolean {
        return checkIfAuthenticated()
    }

    fun onChangePortalAddress(sequence: CharSequence) {
        _uiState.update { it.copy(portalAddress = sequence.toString()) }
        val isValidated =
            validatePortalNameInput(sequence.toString()) || !uiState.value.portalIsInCloud

        _uiState.update { it.copy(loginInputState = LoginInputState(isPortalValidated = isValidated)) }
        if (uiState.value.loginInputState.canConnectToPortal == true) {
            _uiState.update { it.copy(loginInputState = LoginInputState(canConnectToPortal = false)) }
        }
        if (isValidated) {
            tryIfPortalExists()
        }
    }

    fun setPortalIsInCloud(value: Boolean) {
        _uiState.update { it.copy(portalIsInCloud = value) }
    }

    fun onChangeEmail(sequence: CharSequence) {
        updateInputState(LoginInputState(isEmailValidated = validateEmail(sequence.toString())))
        _uiState.update { it.copy(emailInput = sequence.toString()) }
    }

    fun onChangePassword(sequence: CharSequence) {
        updateInputState(LoginInputState(isPasswordValidated = validatePassword(sequence.toString())))
        _uiState.update { it.copy(passwordInput = sequence.toString()) }

    }

    fun setTfaGoogleInput(value: String) {
        updateInputState(LoginInputState(isCodeValidated = validateCode(value)))
        _uiState.update { it.copy(tfaGoogleInput = value.toIntOrNull()) }
    }

    fun tryIfPortalExists() {
        rememberPortalAddress(uiState.value.portalAddress)
        viewModelScope.launch(IO) {
            //Log.d("authRepository", address)
            val response = checkPortalPossibilities(uiState.value.portalAddress)
            when (response) {
                is Success -> {
                    Log.d("tryIfPortalExists", "success")
                    updateInputState(LoginInputState(canConnectToPortal = true))
                }
                is Failure -> {
                    Log.d("tryIfPortalExists", "failure")
                    updateInputState(LoginInputState(serverResponseError = response.reason))
                }
            }
        }
    }

    fun authenticate() {
        if (uiState.value.loginInputState.readyToTryAuth()) {
            viewModelScope.launch {
                rememberPortalAddress(uiState.value.portalAddress)
                val response = login(
                    uiState.value.portalAddress,
                    uiState.value.emailInput,
                    uiState.value.passwordInput,
                    uiState.value.tfaGoogleInput
                )
                when (response) {
                    is Success -> {
                        if (response.value.token != null && response.value.tfa != true ) {
                            updateInputState(LoginInputState(authenticationIsComplete = true))
                        } else if (response.value.tfa == true ){
                            updateInputState(LoginInputState(twoFactorAuth = true))
                        }
                    }
                    is Failure -> {
                        updateInputState(LoginInputState(serverResponseError = response.reason))
                    }
                }
            }
        }
    }

    private fun updateInputState(inputState: LoginInputState) {
        if (inputState.authenticationIsComplete != null) {
            _uiState.update { screenState ->
                screenState.copy(
                    loginInputState = screenState.loginInputState.copy(
                        authenticationIsComplete = inputState.authenticationIsComplete
                    )
                )
            }
        }
        if (inputState.twoFactorAuth != null) {
            _uiState.update { screenState ->
                screenState.copy(
                    loginInputState = screenState.loginInputState.copy(
                        twoFactorAuth = inputState.twoFactorAuth
                    )
                )
            }
        }
        if (inputState.canConnectToPortal != null){
            _uiState.update { screenState ->
                screenState.copy(
                    loginInputState = screenState.loginInputState.copy(
                        canConnectToPortal = inputState.canConnectToPortal
                    )
                )
            }
        }

        if (inputState.isCodeValidated != null){
            _uiState.update { screenState ->
                screenState.copy(
                    loginInputState = screenState.loginInputState.copy(
                        isCodeValidated = inputState.isCodeValidated
                    )
                )
            }
        }
        if (inputState.isEmailValidated != null) {
            _uiState.update { screenState ->
                screenState.copy(
                    loginInputState = screenState.loginInputState.copy(
                        isEmailValidated = inputState.isEmailValidated
                    )
                )
            }
        }
        if (inputState.isPortalValidated != null) {
            _uiState.update { screenState ->
                screenState.copy(
                    loginInputState = screenState.loginInputState.copy(
                        isPortalValidated = inputState.isPortalValidated
                    )
                )
            }
        }

        if (inputState.isPasswordValidated != null) {
            _uiState.update { screenState ->
                screenState.copy(
                    loginInputState = screenState.loginInputState.copy(
                        isPasswordValidated = inputState.isPasswordValidated
                    )
                )
            }
        }

        if (inputState.serverResponseError != null) {
            _uiState.update { screenState ->
                screenState.copy(
                    loginInputState = screenState.loginInputState.copy(
                        serverResponseError = inputState.serverResponseError
                    )
                )
            }
        }
    }
}
