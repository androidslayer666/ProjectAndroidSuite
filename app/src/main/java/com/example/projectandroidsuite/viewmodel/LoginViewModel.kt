package com.example.projectandroidsuite.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.example.network.dto.auth.LoginRequest
import com.example.network.dto.auth.LoginResponse
import com.example.network.services.ApiClientAuth

import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private var _inputPortalValidated = MutableLiveData<Boolean>(null)
    val inputPortalValidated: LiveData<Boolean> = _inputPortalValidated

    private var _inputEmailValidated = MutableLiveData<Boolean>()
    val inputEmailValidated: LiveData<Boolean> = _inputEmailValidated

    private var _inputPasswordValidated = MutableLiveData<Boolean>()
    val inputPasswordValidated: LiveData<Boolean> = _inputPasswordValidated

    private var _canConnectToPortal = MutableLiveData<Boolean>(false)
    val canConnectToPortal: LiveData<Boolean> = _canConnectToPortal

    private var _loginPortalInput = MutableLiveData<String>()
    val loginPortalInput: LiveData<String> = _loginPortalInput

    private var _emailInput = MutableLiveData<String>()
    val emailInput: LiveData<String> = _emailInput

    private var _passwordInput = MutableLiveData<String>()
    val passwordInput: LiveData<String> = _passwordInput


    fun onChangePortalAddress(sequence: CharSequence, start: Int, before: Int, count: Int) {
        _loginPortalInput.value = sequence.toString()
        val isValidated = validatePortalNameInput(sequence.toString())
        _inputPortalValidated.value = isValidated
        if (_canConnectToPortal.value == true) {
            _canConnectToPortal.value = false
        }
        if(isValidated){
            tryIfPortalExists()
        }
        //Log.d("LoginViewModel", "portal address is valid" + inputPortalValidated.value.toString())
    }

    fun onChangeEmail(sequence: CharSequence, start: Int, before: Int, count: Int) {
        _inputEmailValidated.value = validateEmail(sequence.toString())
        _emailInput.value = sequence.toString()
        //Log.d("LoginViewModel", inputEmailValidated.value.toString())
    }

    fun onChangePassword(sequence: CharSequence, start: Int, before: Int, count: Int) {
        _inputPasswordValidated.value = validatePassword(sequence.toString())
        _passwordInput.value = sequence.toString()
        //Log.d("LoginViewModel", passwordInput.value.toString())
    }

    fun tryIfPortalExists() {
        val apiClient = ApiClientAuth()

        loginPortalInput.value?.toString()?.let { Log.d("LoginViewModel", it) }
        apiClient.getApiService(loginPortalInput.value?.trim() ?: "")
            .login(LoginRequest("", ""))
            .enqueue(
                (object : Callback<LoginResponse> {
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        if (inputPortalValidated.value == true)
                            _canConnectToPortal.value = false
                        //Log.d("LoginViewModel", t.toString())
                    }

                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if (response.code() == 500) {
                            //Log.d("LoginViewModel", response.toString())
                            _canConnectToPortal.value = true
                        }
                    }
                })
            )
    }
}