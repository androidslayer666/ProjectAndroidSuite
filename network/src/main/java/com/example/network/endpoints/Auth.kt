package com.example.network.endpoints

import com.example.network.Constants.LOGIN_URL
import com.example.network.Constants.TRY_PORTAL_URL
import com.example.network.dto.auth.LoginRequest
import com.example.network.dto.auth.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface Auth {

        //todo make one request

        @GET(TRY_PORTAL_URL)
        fun tryLogin(): Call<LoginResponse>

        @POST(LOGIN_URL)
        suspend fun login(@Body request: LoginRequest): LoginResponse

}