package com.example.network.services

import com.example.network.endpoints.AuthEndPoint
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClientAuth {
    private lateinit var apiService: AuthEndPoint

    fun getApiService(ulr: String): AuthEndPoint {
        if (!::apiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://$ulr/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            apiService = retrofit.create(AuthEndPoint::class.java)
        }
        return apiService
    }
}


