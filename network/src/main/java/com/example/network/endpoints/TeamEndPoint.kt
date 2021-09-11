package com.example.network.endpoints

import com.example.network.dto.Team
import com.example.network.dto.UserDto
import retrofit2.http.GET
import retrofit2.http.Path

interface TeamEndPoint {

    @GET("api/2.0/project/{projectId}/teamExcluded")
    suspend fun getProjectTeam(@Path("projectId")projectId: Int): Team

    @GET("api/2.0/people")
    suspend fun getAllPortalUsers():Team


}
