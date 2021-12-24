package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.data.Constants.AUTHENTICATED
import com.example.data.Constants.EXPIRATION_DATE
import com.example.data.Constants.HTTPS
import com.example.data.Constants.USER_TOKEN
import com.example.data.constructAuthUrl
import com.example.data.constructCheckPossibilitiesUrl
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
        //Log.d("SessionManager", token.authToken)
        val editor = prefs.edit()
        _authenticated.value = true
        editor.putString(USER_TOKEN, token.authToken)
        editor.putBoolean(AUTHENTICATED, true)
        editor.putString(EXPIRATION_DATE, token.tokenExpirationDate)
        editor.apply()
    }

    override fun rememberPortalAddress(address: String) {
        val editor = prefs.edit()
        editor.putString(com.example.data.Constants.PORTAL_ADDRESS, "$HTTPS$address/")
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
        return try {
            val loginResponse =
                apiService.login(
                    constructAuthUrl(address, code),
                    LoginRequestDto(email = email, password = password)
                )
            //Log.d("authenticate", loginResponse.toString())
            if(loginResponse.token?.authToken != null){ saveAuthToken(loginResponse.token!!) }
            Success(loginResponse.fromLoginResponseDtoToLoginResponse())
        } catch (e: Exception) {
            //Log.d("SessionManager", e.toString())
            Failure(e.toString())
        }
    }

    override suspend fun checkPortalPossibilities(address: String): Result<String, String> {
        return try {
            //val response =
            apiService.checkPortalPossibilities(constructCheckPossibilitiesUrl(address))
            //Log.d("AuthRepositoryImpl", response.toString())
            Success("")
        } catch (e: Exception) {
            //Log.e("SessionManager", e.toString())
            Failure(e.toString())
        }
    }

}