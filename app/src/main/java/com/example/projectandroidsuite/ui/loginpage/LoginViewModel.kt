package com.example.projectandroidsuite.ui.loginpage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.SessionManager
import com.example.network.dto.auth.LoginRequest
import com.example.network.dto.auth.LoginResponse
import com.example.network.services.ApiClientAuth
import com.example.projectandroidsuite.ProjectApplication
import com.example.projectandroidsuite.logic.validateEmail
import com.example.projectandroidsuite.logic.validatePassword
import com.example.projectandroidsuite.logic.validatePortalNameInput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    private var _inputPortalValidated = MutableLiveData<Boolean>(null)
    val inputPortalValidated: LiveData<Boolean> = _inputPortalValidated

    private var _inputEmailValidated = MutableLiveData<Boolean>()
    val inputEmailValidated: LiveData<Boolean> = _inputEmailValidated

    private var _inputPasswordValidated = MutableLiveData<Boolean>()
    val inputPasswordValidated: LiveData<Boolean> = _inputPasswordValidated

    private var _canConnectToPortal = MutableLiveData<Boolean>(false)
    val canConnectToPortal: LiveData<Boolean> = _canConnectToPortal

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

    init {
        sessionManager.logOut()
    }

//    fun checkIfAuthenticated(): Boolean {
//        Log.d("checkIfAuthenticated",sessionManager.isAuthenticated().toString())
//        return sessionManager.isAuthenticated()
//    }

    fun onChangePortalAddress(sequence: CharSequence) {
        _portalAddress.value = sequence.toString()
        val isValidated = validatePortalNameInput(sequence.toString()) || portalIsInCloud.value == 1
        _inputPortalValidated.value = isValidated
        if (_canConnectToPortal.value == true) {
            _canConnectToPortal.value = false
        }
        if (isValidated) {
            tryIfPortalExists()
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

    fun tryIfPortalExists() {
        val apiClient = ApiClientAuth()
        Log.d("LoginViewModel", "try portal")
        portalAddress.value?.toString()?.let { Log.d("LoginViewModel", it) }
        apiClient.getApiService(portalAddress.value?.trim() ?: "")
            .tryLogin()
            .enqueue(
                (object : Callback<LoginResponse> {
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Log.d("LoginViewModel", t.toString())
                        if (inputPortalValidated.value == true)
                            _canConnectToPortal.value = false
                    }

                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        Log.d("LoginViewModel", response.toString())
                        if (response.code() == 401) {
                            _canConnectToPortal.value = true
                        }
                    }
                })
            )
    }

    fun authenticate(
        onCompleteLogin: () -> Unit
    ) {
        Log.d("LoginFragment",  "inputPortalValidated" + inputPortalValidated.value.toString())
        Log.d("LoginFragment",  "canConnectToPortal" + canConnectToPortal.value.toString())
        Log.d("LoginFragment",  "inputEmailValidated" + inputEmailValidated.value.toString())
        Log.d("LoginFragment",  "inputPasswordValidated" + inputPasswordValidated.value.toString())

//        inputPortalValidated.value == true &&
//                canConnectToPortal.value == true &&
//                inputEmailValidated.value == true &&
//                inputPasswordValidated.value == true

        if (inputPortalValidated.value == true &&
            canConnectToPortal.value == true &&
            inputEmailValidated.value == true &&
            inputPasswordValidated.value == true
        ) {
            viewModelScope.launch{
                Log.d("LoginFragment", "all is well")
                sessionManager.authenticate(
                    LoginRequest(
                        emailInput.value!!,
                        passwordInput.value!!
                    ), ProjectApplication.applicationContext(),
                    portalAddress.value!!
                )
                sessionManager.rememberPortalAddress(portalAddress.value!!)
                onCompleteLogin()
            }
        }
    }

}