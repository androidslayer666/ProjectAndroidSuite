package com.example.data.endpoints

import com.example.domain.dto.Team
import com.example.domain.dto.UserTransporter
import retrofit2.http.GET
import retrofit2.http.Path


interface TeamEndPoint {

    @GET("api/2.0/project/{projectId}/team")
    suspend fun getProjectTeam(@Path("projectId")projectId: Int): Team

    @GET("api/2.0/people")
    suspend fun getAllPortalUsers(): Team

    @GET("api/2.0/people/@self")
    suspend fun getSelfProfile(): UserTransporter

}
