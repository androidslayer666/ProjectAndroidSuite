package com.example.data.endpoints

import com.example.domain.dto.auth.LoginRequestDto
import com.example.domain.dto.auth.LoginResponseDto
import com.example.domain.dto.auth.ResponseCapabilities
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface AuthEndPoint {

        @POST
        suspend fun login(@Url url: String, @Body request: LoginRequestDto): LoginResponseDto

        @GET
        suspend fun checkPortalPossibilities(@Url url: String) : ResponseCapabilities

}