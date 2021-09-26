package com.example.network.endpoints

import com.example.network.Constants.LOGIN_URL
import com.example.network.dto.auth.LoginRequest
import com.example.network.dto.auth.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface Auth {

        //todo make one request

        @POST(LOGIN_URL)
        @Headers("Accept: application/json")
        fun tryLogin(@Body request: LoginRequest): Call<LoginResponse>

        @POST(LOGIN_URL)
        @Headers("Accept: application/json")
        suspend fun login(@Body request: LoginRequest): LoginResponse

}