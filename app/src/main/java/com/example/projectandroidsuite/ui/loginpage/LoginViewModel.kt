package com.example.projectandroidsuite.ui.loginpage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.AuthCredentialsProvider
import com.example.domain.repository.AuthRepository
import com.example.domain.Failure
import com.example.domain.Success
import com.example.projectandroidsuite.logic.validateCode
import com.example.projectandroidsuite.logic.validateEmail
import com.example.projectandroidsuite.logic.validatePassword
import com.example.projectandroidsuite.logic.validatePortalNameInput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private var _inputPortalValidated = MutableLiveData<Boolean>(false)
    val inputPortalValidated: LiveData<Boolean> = _inputPortalValidated

    private var _inputEmailValidated = MutableLiveData<Boolean>()
    val inputEmailValidated: LiveData<Boolean> = _inputEmailValidated

    private var _inputPasswordValidated = MutableLiveData<Boolean>()
    val inputPasswordValidated: LiveData<Boolean> = _inputPasswordValidated

    private var _canConnectToPortal = MutableLiveData<Boolean>(false)
    val canConnectToPortal: LiveData<Boolean> = _canConnectToPortal

    private var _inputCodeValidated = MutableLiveData<Boolean>(false)
    val inputCodeValidated: LiveData<Boolean> = _inputCodeValidated

    private var _portalAddress = MutableLiveData<String>()
    val portalAddress: LiveData<String> = _portalAddress

    private var _portalAddressForLocal = MutableLiveData<String>()
    val portalAddressForLocal: LiveData<String> = _portalAddressForLocal

    private var _emailInput = MutableLiveData<String>()
    val emailInput: LiveData<String> = _emailInput

    private var _passwordInput = MutableLiveData<String>()
    val passwordInput: LiveData<String> = _passwordInput

    private var _portalIsInCloud = MutableLiveData<Int>(0)
    val portalIsInCloud: LiveData<Int> = _portalIsInCloud

    private var _twoFactorAuth = MutableLiveData<Boolean>(false)
    val twoFactorAuth: LiveData<Boolean> = _twoFactorAuth

    private var _tfaGoogleInput = MutableLiveData<String?>(null)
    val tfaGoogleInput: LiveData<String?> = _tfaGoogleInput

    fun checkIfAuthenticated(): Boolean {
        Log.d("checkIfAuthenticated",authRepository.isAuthenticated().toString())
        return authRepository.isAuthenticated()
    }

    fun onChangePortalAddress(sequence: CharSequence) {
        _portalAddress.value = sequence.toString()
        val isValidated = validatePortalNameInput(sequence.toString()) || portalIsInCloud.value == 1
        _inputPortalValidated.value = isValidated
        if (_canConnectToPortal.value == true) {
            _canConnectToPortal.value = false
        }
        if (isValidated) {
            tryIfPortalExists(portalAddress.value!!)
        }
        //Log.d("LoginViewModel", "portal address is valid" + inputPortalValidated.value.toString())
    }


    fun setPortalIsInCloud(value : Int) {
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
        authRepository.rememberPortalAddress(portalAddress.value!!)
        viewModelScope.launch(IO) {
            when (authRepository.tryPortal(address)) {
                is Success -> withContext(Main) {_canConnectToPortal.value = true}
                is Failure -> {}
            }
        }
    }

    fun authenticate(
        onCompleteLogin: () -> Unit
    ) {
        Log.d("LoginFragment",  "inputPortalValidated" + inputPortalValidated.value.toString())
        Log.d("LoginFragment",  "canConnectToPortal" + canConnectToPortal.value.toString())
        Log.d("LoginFragment",  "inputEmailValidated" + inputEmailValidated.value.toString())
        Log.d("LoginFragment",  "inputPasswordValidated" + inputPasswordValidated.value.toString())
        Log.d("LoginFragment",  "inputPasswordValidated" + portalAddress.value.toString())
        if (inputPortalValidated.value == true &&
            canConnectToPortal.value == true &&
            inputEmailValidated.value == true &&
            inputPasswordValidated.value == true
        ) {
            viewModelScope.launch{
                if(tfaGoogleInput.value == null) {
                    authRepository.rememberPortalAddress(portalAddress.value!!)
                    Log.d("LoginViewModel", "all is well")
                    val response = authRepository.login(
                        portalAddress.value?:"",
                        emailInput.value!!,
                        passwordInput.value!!
                    )
                    Log.d("LoginViewModel", response.toString())
                    when (response) {
                        is Success -> {
                            if (response.value.token?.authToken != null) {
                                onCompleteLogin()
                            } else if (response.value.tfa == true) {
                                Log.d("LoginFragment", "tfa == true")
                                _twoFactorAuth.value = true
                            }
                        }
                        is Failure -> {
                        }
                    }
                }else {
                    authRepository.rememberPortalAddress(portalAddress.value!!)
                    Log.d("LoginFragment", "all is well")
                    val response = authRepository.login(
                        portalAddress.value?:"",
                        emailInput.value!!,
                        passwordInput.value!!,
                        tfaGoogleInput.value!!.toInt()
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

}