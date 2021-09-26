package com.example.network.services

import com.example.network.endpoints.Auth
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClientAuth {
    private lateinit var apiService: Auth

    fun getApiService(ulr: String): Auth {
        // Initialize ApiService if not initialized yet
        if (!::apiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://$ulr/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            apiService = retrofit.create(Auth::class.java)
        }
        return apiService
    }
}


