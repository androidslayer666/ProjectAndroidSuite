package com.example.domain

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.network.Constants.AUTH_TOKEN
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
class AuthCredentialsProvider @Inject constructor(context: Context) {

    private var prefs: SharedPreferences =
        context.getSharedPreferences(AUTH_TOKEN, Context.MODE_PRIVATE)

    fun fetchPortalAddress(): String? {
        return prefs.getString(PORTAL_ADDRESS, null)
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

}