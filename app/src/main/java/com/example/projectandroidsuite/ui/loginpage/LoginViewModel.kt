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
            tryIfPortalExists(sequence.toString())
        }
    }

    fun setPortalIsInCloud(value: Boolean) {
        _uiState.update { it.copy(portalIsInCloud = value) }
    }

    fun onChangeEmail(sequence: CharSequence) {
        _uiState.update {
            it.copy(
                loginInputState = it.loginInputState.copy(
                    isEmailValidated = validateEmail(
                        sequence.toString()
                    )
                )
            )
        }
        _uiState.update { it.copy(emailInput = sequence.toString()) }
    }

    fun onChangePassword(sequence: CharSequence) {
        _uiState.update {
            it.copy(
                loginInputState = it.loginInputState.copy(
                    isPasswordValidated = validatePassword(
                        sequence.toString()
                    )
                )
            )
        }
        _uiState.update { it.copy(passwordInput = sequence.toString()) }

    }

    fun setTfaGoogleInput(value: String) {
        _uiState.update {
            it.copy(
                loginInputState = it.loginInputState.copy(
                    isCodeValidated = validateCode(
                        value
                    )
                )
            )
        }
        _uiState.update { it.copy(tfaGoogleInput = value.toIntOrNull()) }
    }

    fun tryIfPortalExists(address: String) {
        rememberPortalAddress(address)
        viewModelScope.launch(IO) {
            //Log.d("authRepository", address)
            val response = checkPortalPossibilities(address)
            when (response) {
                is Success -> {
                    Log.d("tryIfPortalExists", "success")
                    _uiState.update {
                        it.copy(
                            loginInputState = it.loginInputState.copy(
                                canConnectToPortal = true
                            )
                        )
                    }
                }
                is Failure -> {
                    Log.d("tryIfPortalExists", "failure")
                    _uiState.update {
                        it.copy(
                            loginInputState = it.loginInputState.copy(
                                serverResponseError = response.reason
                            )
                        )
                    }
                }
            }
        }
    }

    fun authenticate(
        onCompleteLogin: () -> Unit
    ) {
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
                        if (response.value.token != null) {
                            onCompleteLogin()
                        }
                    }
                    is Failure -> {
                        _uiState.update {
                            it.copy(
                                loginInputState = it.loginInputState.copy(
                                    serverResponseError = response.reason
                                )
                            )
                        }
                    }
                }
            }
        }
    }

}