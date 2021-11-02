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
import com.example.projectandroidsuite.ui.utils.validation.validateCode
import com.example.projectandroidsuite.ui.utils.validation.validateEmail
import com.example.projectandroidsuite.ui.utils.validation.validatePassword
import com.example.projectandroidsuite.ui.utils.validation.validatePortalNameInput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val login: Login,
    private val checkPortalPossibilities: CheckPortalPossibilities,
    private val rememberPortalAddress: RememberPortalAddress,
    private val checkIfAuthenticated: CheckIfAuthenticated
) : ViewModel() {

    private var _inputPortalValidated = MutableStateFlow(false)
    val inputPortalValidated: StateFlow<Boolean> = _inputPortalValidated

    private var _inputEmailValidated = MutableStateFlow(false)
    val inputEmailValidated: StateFlow<Boolean> = _inputEmailValidated

    private var _inputPasswordValidated = MutableStateFlow(false)
    val inputPasswordValidated: StateFlow<Boolean> = _inputPasswordValidated

    private var _inputCodeValidated = MutableStateFlow(false)

    private var _canConnectToPortal = MutableStateFlow(false)
    val canConnectToPortal: StateFlow<Boolean> = _canConnectToPortal

    private var _portalAddress = MutableStateFlow("")
    val portalAddress: StateFlow<String> = _portalAddress

    private var _emailInput = MutableStateFlow("")
    val emailInput: StateFlow<String> = _emailInput

    private var _passwordInput = MutableStateFlow("")
    val passwordInput: StateFlow<String> = _passwordInput

    private var _portalIsInCloud = MutableStateFlow(0)
    val portalIsInCloud: StateFlow<Int> = _portalIsInCloud

    private var _twoFactorAuth = MutableStateFlow(false)
    val twoFactorAuth: StateFlow<Boolean> = _twoFactorAuth

    private var _tfaGoogleInput = MutableStateFlow<String?>(null)
    val tfaGoogleInput: StateFlow<String?> = _tfaGoogleInput

    fun checkAuthentication(): Boolean {
        return checkIfAuthenticated()
    }

    fun onChangePortalAddress(sequence: CharSequence) {
        _portalAddress.value = sequence.toString()
        val isValidated = validatePortalNameInput(sequence.toString()) || portalIsInCloud.value == 1
        _inputPortalValidated.value = isValidated
        if (_canConnectToPortal.value) {
            _canConnectToPortal.value = false
        }
        if (isValidated) {
            tryIfPortalExists(portalAddress.value)
        }
    }


    fun setPortalIsInCloud(value: Int) {
        _portalIsInCloud.value = value
    }

    fun onChangeEmail(sequence: CharSequence) {
        _inputEmailValidated.value = validateEmail(sequence.toString())
        _emailInput.value = sequence.toString()
        //Log.d("LoginViewModel", inputEmailValidated.value.toString())
    }

    fun onChangePassword(sequence: CharSequence) {
        _inputPasswordValidated.value = validatePassword(sequence.toString())
        _passwordInput.value = sequence.toString()
        //Log.d("LoginViewModel", passwordInput.value.toString())
    }

    fun setTfaGoogleInput(value: String) {
        _tfaGoogleInput.value = value
        _inputCodeValidated.value = validateCode(value)
    }

    fun tryIfPortalExists(address: String) {
        rememberPortalAddress(address)
        viewModelScope.launch(IO) {
            when (checkPortalPossibilities(address)) {
                is Success -> withContext(Main) { _canConnectToPortal.value = true }
                is Failure -> {
                }
            }
        }
    }

    fun authenticate(
        onCompleteLogin: () -> Unit
    ) {
        if (inputPortalValidated.value && canConnectToPortal.value && inputEmailValidated.value
            && inputPasswordValidated.value
        ) {
            viewModelScope.launch {
                rememberPortalAddress(portalAddress.value)
                Log.d("LoginFragment", "all is well")
                val response = login(
                    portalAddress.value,
                    emailInput.value,
                    passwordInput.value,
                    tfaGoogleInput.value?.toInt()
                )
                when (response) {
                    is Success -> {
                        if (response.value.token != null) {
                            onCompleteLogin()
                        }
                    }
                    is Failure -> {
                    }
                }
            }
        }
    }

}