package com.example.domain

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthCredentialsProvider @Inject constructor(context: Context) {

    private var prefs: SharedPreferences =
        context.getSharedPreferences("AuthToken", Context.MODE_PRIVATE)

    fun fetchPortalAddress(): String? {
        return prefs.getString("portal_address", null)
    }

    fun fetchAuthToken(): String? {
        return prefs.getString("user_token", null)
    }

}