package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.Constants.AUTH_TOKEN
import com.example.data.Constants.PORTAL_ADDRESS
import com.example.data.Constants.USER_TOKEN


class AuthCredentialsProvider (context: Context) {

    private var prefs: SharedPreferences =
        context.getSharedPreferences(AUTH_TOKEN, Context.MODE_PRIVATE)

    fun fetchPortalAddress(): String? {
        return prefs.getString(PORTAL_ADDRESS, null)
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

}