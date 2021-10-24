package com.example.domain.repositoryimpl

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.domain.mappers.fromLoginResponseDtoToLoginResponse
import com.example.domain.model.LoginResponse
import com.example.domain.repository.AuthRepository
import com.example.domain.Failure
import com.example.domain.Result
import com.example.domain.Success
import com.example.network.Constants.AUTHENTICATED
import com.example.network.Constants.AUTH_TOKEN
import com.example.network.Constants.EXPIRATION_DATE
import com.example.network.Constants.PORTAL_ADDRESS
import com.example.network.Constants.USER_TOKEN
import com.example.network.dto.auth.LoginRequestDto
import com.example.network.dto.auth.TokenDto
import com.example.network.endpoints.AuthEndPoint
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthRepositoryImpl @Inject constructor(
    context: Context,
    private val apiService: AuthEndPoint
) : AuthRepository {

    private var prefs: SharedPreferences =
        context.getSharedPreferences(AUTH_TOKEN, Context.MODE_PRIVATE)
    private var _authenticated = MutableLiveData<Boolean>()

    init {
        _authenticated.value = isAuthenticated()
    }

    private fun saveAuthToken(token: TokenDto) {
        Log.d("SessionManager", token.authToken)
        val editor = prefs.edit()
        _authenticated.value = true
        editor.putString(USER_TOKEN, token.authToken)
        editor.putBoolean(AUTHENTICATED, true)
        editor.putString(EXPIRATION_DATE, token.tokenExpirationDate)
        editor.apply()
    }

    override fun rememberPortalAddress(address: String) {
        val editor = prefs.edit()
        editor.putString(PORTAL_ADDRESS, "https://" + address + "/")
        editor.apply()
    }

    override fun logOut() {
        val editor = prefs.edit()
        _authenticated.value = false
        editor.putString(USER_TOKEN, null)
        editor.putBoolean(AUTHENTICATED, false)
        editor.putString(EXPIRATION_DATE, null)
        editor.apply()
    }

    override fun isAuthenticated(): Boolean {
        return prefs.getBoolean(AUTHENTICATED, false)
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

    override suspend fun tryPortal(address: String): Result<String, String> {
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