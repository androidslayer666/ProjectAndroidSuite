package com.example.domain.repository


import com.example.domain.utils.Result
import com.example.domain.model.LoginResponse

interface AuthRepository {

    suspend fun login(address: String,
                      email: String,
                      password: String,
                      code: Int? = null) : Result<LoginResponse, String>

    fun rememberPortalAddress(address: String)

    fun logOut()

    fun isAuthenticated(): Boolean

    suspend fun checkPortalPossibilities(address: String) : Result<String, String>

}