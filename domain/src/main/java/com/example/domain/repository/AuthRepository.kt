package com.example.domain.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.network.dto.auth.LoginRequest
import com.example.network.dto.auth.Token
import com.example.network.endpoints.AuthEndPoint
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthRepository @Inject constructor(context: Context) {

    //todo remove lateinit
    private lateinit var apiService: AuthEndPoint

    private var prefs: SharedPreferences =
        context.getSharedPreferences("AuthToken", Context.MODE_PRIVATE)
    private var _authenticated = MutableLiveData<Boolean>()


    companion object {
        const val USER_TOKEN = "user_token"
        const val AUTHENTICATED = "authenticated"
        const val EXPIRATION_DATE = "expirationDate"
        const val PORTAL_ADDRESS = "portal_address"
    }

    init {
        _authenticated.value = isAuthenticated()
    }

    suspend fun authenticate(loginRequest: LoginRequest, context: Context, url: String) {
        val loginResponse = getAuthService(url).login(loginRequest)
        Log.d("authenticate", loginResponse.toString())
        if(loginResponse.token.authToken.isNotEmpty())
            saveAuthToken(loginResponse.token)
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

    fun rememberPortalAddress(portalAddress: String) {
        val editor = prefs.edit()
        editor.putString(PORTAL_ADDRESS, "https://" + portalAddress + "/")
        editor.apply()
    }

    fun fetchPortalAddress(): String? {
        return prefs.getString(PORTAL_ADDRESS, null)
    }


    fun fetchAuthToken(): String? {
        //Log.d("SessionManager", token?: "no token")
        return prefs.getString(USER_TOKEN, null)
    }

    fun logOut() {
        Log.d("SessionManager", "logging out")
        val editor = prefs.edit()
        _authenticated.value = false
        editor.putString(USER_TOKEN, null)
        editor.putBoolean(AUTHENTICATED, false)
        editor.putString(EXPIRATION_DATE, null)
        editor.apply()
    }

    fun isAuthenticated(): Boolean {
        return prefs.getBoolean(AUTHENTICATED, false)
    }

    private fun getAuthService(ulr: String): AuthEndPoint {
        if (!::apiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://$ulr/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            apiService = retrofit.create(AuthEndPoint::class.java)
        }
        return apiService
    }

}