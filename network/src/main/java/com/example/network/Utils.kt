package com.example.network

import com.example.network.Constants.BASE_URL_PROJECT
import com.example.network.dto.ProjectDto
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


fun authTokenInterceptor(token: String) =
    OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
        val newRequest: Request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        chain.proceed(newRequest)
    }).build()

fun <EndPoint> buildEndPoint(endPoint: Class<EndPoint>, token: String): EndPoint {
    return Retrofit.Builder()
        .client(authTokenInterceptor(token))
        .baseUrl(BASE_URL_PROJECT)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(endPoint)
}
