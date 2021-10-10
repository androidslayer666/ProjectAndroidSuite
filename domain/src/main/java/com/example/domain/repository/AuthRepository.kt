package com.example.domain.repository

import android.util.Log
import com.example.network.Constants
import com.example.network.dto.auth.LoginRequest
import com.example.network.dto.auth.Token

interface AuthRepository {

    suspend fun authenticate(email: String, password: String)

    fun rememberPortalAddress(portalAddress: String)

    fun logOut()

    fun isAuthenticated(): Boolean

}