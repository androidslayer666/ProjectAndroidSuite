package com.example.domain.repositoryimpl

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.domain.repository.AuthRepository
import com.example.network.Constants.AUTHENTICATED
import com.example.network.Constants.EXPIRATION_DATE
import com.example.network.Constants.PORTAL_ADDRESS
import com.example.network.Constants.USER_TOKEN
import com.example.network.dto.auth.LoginRequest
import com.example.network.dto.auth.Token
import com.example.network.endpoints.AuthEndPoint
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthRepositoryImpl @Inject constructor(
    context: Context,
    private val apiService: AuthEndPoint
) : AuthRepository {

    private var prefs: SharedPreferences =
        context.getSharedPreferences("AuthToken", Context.MODE_PRIVATE)
    private var _authenticated = MutableLiveData<Boolean>()

    init {
        _authenticated.value = isAuthenticated()
    }

    override suspend fun authenticate(email: String, password: String) {
        try {
            val loginResponse = apiService.login( LoginRequest(email = email, password = password))
            Log.d("authenticate", loginResponse.toString())
            if (loginResponse.token.authToken.isNotEmpty())
                saveAuthToken(loginResponse.token)
        } catch (e:Exception) {
            Log.d("SessionManager", e.toString())
        }
    }

    private fun saveAuthToken(token: Token) {
        Log.d("SessionManager", token.authToken)
        val editor = prefs.edit()
        _authenticated.value = true
        editor.putString(USER_TOKEN, token.authToken)
        editor.putBoolean(AUTHENTICATED, true)
        editor.putString(EXPIRATION_DATE, token.tokenExpirationDate)
        editor.apply()
    }

    override fun rememberPortalAddress(portalAddress: String) {
        val editor = prefs.edit()
        editor.putString(PORTAL_ADDRESS, "https://" + portalAddress + "/")
        editor.apply()
    }

    override fun logOut() {
        Log.d("SessionManager", "logging out")
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

}