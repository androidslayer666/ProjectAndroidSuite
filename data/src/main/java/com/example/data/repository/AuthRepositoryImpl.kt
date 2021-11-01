package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.domain.utils.Failure
import com.example.domain.utils.Result
import com.example.domain.utils.Success
import com.example.domain.mappers.fromLoginResponseDtoToLoginResponse
import com.example.domain.model.LoginResponse
import com.example.domain.repository.AuthRepository
import com.example.domain.dto.auth.LoginRequestDto
import com.example.domain.dto.auth.TokenDto
import com.example.data.endpoints.AuthEndPoint
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    context: Context,
    private val apiService: AuthEndPoint
) : AuthRepository {

    private var prefs: SharedPreferences =
        context.getSharedPreferences(com.example.data.Constants.AUTH_TOKEN, Context.MODE_PRIVATE)
    private var _authenticated = MutableLiveData<Boolean>()

    init {
        _authenticated.value = isAuthenticated()
    }

    private fun saveAuthToken(token: TokenDto) {
        Log.d("SessionManager", token.authToken)
        val editor = prefs.edit()
        _authenticated.value = true
        editor.putString(com.example.data.Constants.USER_TOKEN, token.authToken)
        editor.putBoolean(com.example.data.Constants.AUTHENTICATED, true)
        editor.putString(com.example.data.Constants.EXPIRATION_DATE, token.tokenExpirationDate)
        editor.apply()
    }

    override fun rememberPortalAddress(address: String) {
        val editor = prefs.edit()
        editor.putString(com.example.data.Constants.PORTAL_ADDRESS, "https://" + address + "/")
        editor.apply()
    }

    override fun logOut() {
        val editor = prefs.edit()
        _authenticated.value = false
        editor.putString(com.example.data.Constants.USER_TOKEN, null)
        editor.putBoolean(com.example.data.Constants.AUTHENTICATED, false)
        editor.putString(com.example.data.Constants.EXPIRATION_DATE, null)
        editor.apply()
    }

    override fun isAuthenticated(): Boolean {
        return prefs.getBoolean(com.example.data.Constants.AUTHENTICATED, false)
    }

    override suspend fun login(
        address: String,
        email: String,
        password: String,
        code: Int?
    ): Result<LoginResponse, String> {
        try {
            val portalAddress =
                if (code != null) "https://$address/api/2.0/authentication/$code"
                else "https://$address/api/2.0/authentication"
            Log.d("login", portalAddress)
            val loginResponse =
                apiService.login(portalAddress, LoginRequestDto(email = email, password = password))
            Log.d("authenticate", loginResponse.toString())
            loginResponse.token?.authToken?.let { saveAuthToken(loginResponse.token!!) }
            return Success(loginResponse.fromLoginResponseDtoToLoginResponse())
        } catch (e: Exception) {
            Log.d("SessionManager", e.toString())
            return Failure(e.toString())
        }
    }

    override suspend fun checkPortalPossibilities(address: String): Result<String, String> {
        try {
            val response =
                apiService.checkPortalPossibilities("https://$address/api/2.0/capabilities")
            Log.d("AuthRepositoryImpl", response.toString())
            return Success("portal exists")
        } catch (e: Exception) {
            Log.d("SessionManager", e.toString())
            return Failure(e.toString())
        }
    }

}