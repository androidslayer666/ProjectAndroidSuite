package com.example.domain.interactor.login

import android.util.Log
import com.example.domain.model.LoginResponse
import com.example.domain.repository.AuthRepository
import com.example.domain.utils.Result

class Login(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        address: String,
        email: String,
        password: String,
        code: Int? = null
    ): Result<LoginResponse, String> {

        return authRepository.login(
            address, email, password, code
        )
    }
}