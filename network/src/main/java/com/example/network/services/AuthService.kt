package com.example.network.services

import android.content.Context
import android.util.Log
import com.example.network.Constants.BASE_URL_AUTH

import com.example.network.dto.auth.LoginRequest
import com.example.network.dto.auth.LoginResponse
import com.example.network.endpoints.Auth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClientAuth {
    private lateinit var apiService: Auth

    fun getApiService(ulr: String = BASE_URL_AUTH): Auth {
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


