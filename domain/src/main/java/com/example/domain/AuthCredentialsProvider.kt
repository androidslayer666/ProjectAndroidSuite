package com.example.domain

import android.content.Context
import android.content.SharedPreferences
import com.example.network.Constants.AUTH_TOKEN
import com.example.network.Constants.PORTAL_ADDRESS
import com.example.network.Constants.USER_TOKEN
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