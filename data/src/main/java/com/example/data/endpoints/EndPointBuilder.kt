package com.example.data.endpoints

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun <EndPoint> buildEndPoint(endPoint: Class<EndPoint>, token: String, baseUrl:String): EndPoint {
    return Retrofit.Builder()
        .client(authTokenInterceptor(token))
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(endPoint)
}