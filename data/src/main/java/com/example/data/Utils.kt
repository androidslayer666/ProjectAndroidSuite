package com.example.data

import android.util.Log
import com.example.domain.arrangingComments
import com.example.domain.mappers.toEntity
import com.example.domain.mappers.toListEntities
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


fun authTokenInterceptor(token: String): OkHttpClient =
    OkHttpClient.Builder().addInterceptor { chain ->
        val newRequest: Request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        chain.proceed(newRequest)
    }.build()


fun <EndPoint> buildEndPoint(endPoint: Class<EndPoint>, token: String, baseUrl:String): EndPoint {
    return Retrofit.Builder()
        .client(authTokenInterceptor(token))
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(endPoint)
}




