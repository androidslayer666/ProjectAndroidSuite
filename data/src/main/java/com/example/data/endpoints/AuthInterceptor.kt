package com.example.data.endpoints

import com.example.data.Constants
import okhttp3.OkHttpClient
import okhttp3.Request

fun authTokenInterceptor(token: String): OkHttpClient =
    OkHttpClient.Builder().addInterceptor { chain ->
        val newRequest: Request = chain.request().newBuilder()
            .addHeader(Constants.AUTHORIZATION, "${Constants.BEARER} $token")
            .build()
        chain.proceed(newRequest)
    }.build()