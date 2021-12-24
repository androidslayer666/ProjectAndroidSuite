package com.example.data

import com.example.data.Constants.API_AUTHENTICATION
import com.example.data.Constants.AUTHORIZATION
import com.example.data.Constants.BEARER
import com.example.data.Constants.CAPABILITIES
import com.example.data.Constants.HTTPS

import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun constructAuthUrl(address: String, code: Int? ): String {
    return if (code != null) "$HTTPS$address$API_AUTHENTICATION/$code"
    else "$HTTPS$address$API_AUTHENTICATION"
}

fun constructCheckPossibilitiesUrl(address: String): String {
    return "$HTTPS$address$CAPABILITIES"
}




