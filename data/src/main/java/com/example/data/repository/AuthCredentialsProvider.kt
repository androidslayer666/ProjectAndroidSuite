package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences


class AuthCredentialsProvider (context: Context) {

    private var prefs: SharedPreferences =
        context.getSharedPreferences("AuthToken", Context.MODE_PRIVATE)

    fun fetchPortalAddress(): String? {
        return prefs.getString("portal_address", null)
    }

    fun fetchAuthToken(): String? {
        return prefs.getString("user_token", null)
    }

}