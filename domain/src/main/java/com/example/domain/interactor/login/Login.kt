package com.example.domain.interactor.login

import com.example.domain.model.LoginResponse
import com.example.domain.utils.Result

interface Login {
    suspend operator fun invoke(
        address: String,
        email: String,
        password: String,
        code: Int? = null
    ): Result<LoginResponse, String>
}