package com.example.domain.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.network.Constants.AUTHENTICATED
import com.example.network.Constants.EXPIRATION_DATE
import com.example.network.Constants.PORTAL_ADDRESS
import com.example.network.Constants.USER_TOKEN
import com.example.network.dto.auth.LoginRequest
import com.example.network.dto.auth.Token
import com.example.network.endpoints.AuthEndPoint
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

    override suspend fun authenticate(loginRequest: LoginRequest) {
        try {
            val loginResponse = apiService.login(loginRequest)
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