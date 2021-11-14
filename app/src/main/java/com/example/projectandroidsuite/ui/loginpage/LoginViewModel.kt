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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val login: Login,
    private val checkPortalPossibilities: CheckPortalPossibilities,
    private val rememberPortalAddress: RememberPortalAddress,
    private val checkIfAuthenticated: CheckIfAuthenticated
) : ViewModel() {

    private var _portalAddress = MutableStateFlow("")
    val portalAddress: StateFlow<String> = _portalAddress

    private var _emailInput = MutableStateFlow("")
    val emailInput: StateFlow<String> = _emailInput

    private var _passwordInput = MutableStateFlow("")
    val passwordInput: StateFlow<String> = _passwordInput

    private var _portalIsInCloud = MutableStateFlow(0)
    val portalIsInCloud: StateFlow<Int> = _portalIsInCloud

    private var _tfaGoogleInput = MutableStateFlow("")
    val tfaGoogleInput: StateFlow<String> = _tfaGoogleInput

    private var _loginInputState = MutableStateFlow(LoginInputState())
    val loginInputState: StateFlow<LoginInputState> = _loginInputState

    fun checkAuthentication(): Boolean {
        return checkIfAuthenticated()
    }

    fun onChangePortalAddress(sequence: CharSequence) {
        _portalAddress.value = sequence.toString()
        val isValidated = validatePortalNameInput(sequence.toString()) || portalIsInCloud.value == 1
        _loginInputState.value = _loginInputState.value.copy(isPortalValidated = isValidated)
        if (_loginInputState.value.canConnectToPortal == true) {
            _loginInputState.value = _loginInputState.value.copy(canConnectToPortal = false)
        }
        if (isValidated) {
            tryIfPortalExists(portalAddress.value)
        }
    }

    fun setPortalIsInCloud(value: Int) {
        _portalIsInCloud.value = value
    }

    fun onChangeEmail(sequence: CharSequence) {
        _loginInputState.value =
            _loginInputState.value.copy(isEmailValidated = validateEmail(sequence.toString()))
        _emailInput.value = sequence.toString()
    }

    fun onChangePassword(sequence: CharSequence) {
        _loginInputState.value =
            _loginInputState.value.copy(isPasswordValidated = validatePassword(sequence.toString()))
        _passwordInput.value = sequence.toString()
    }

    fun setTfaGoogleInput(value: String) {
        _tfaGoogleInput.value = value
        _loginInputState.value = _loginInputState.value.copy(isCodeValidated = validateCode(value))
    }

    fun tryIfPortalExists(address: String) {
        rememberPortalAddress(address)
        viewModelScope.launch(IO) {
            Log.d("authRepository", address)
            val response = checkPortalPossibilities(address)
            when (response) {
                is Success -> {
                    Log.d("authRepository", "success")
                    _loginInputState.value =
                        _loginInputState.value.copy(canConnectToPortal = true)
                }
                is Failure -> {
                    Log.d("authRepository", "failure")
                    _loginInputState.value =
                        _loginInputState.value.copy(serverResponseError = response.reason)
                }
            }
        }
    }

    fun authenticate(
        onCompleteLogin: () -> Unit
    ) {
        if (loginInputState.value.readyToTryAuth()) {
            viewModelScope.launch {
                rememberPortalAddress(portalAddress.value)
                val response = login(
                    portalAddress.value,
                    emailInput.value,
                    passwordInput.value,
                    tfaGoogleInput.value.toInt()
                )
                when (response) {
                    is Success -> {
                        if (response.value.token != null) {
                            onCompleteLogin()
                        }
                    }
                    is Failure -> {
                        _loginInputState.value =
                            _loginInputState.value.copy(serverResponseError = response.reason)
                    }
                }
            }
        }
    }

}