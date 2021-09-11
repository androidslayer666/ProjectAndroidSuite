package com.example.network.dto

import com.google.gson.annotations.SerializedName

data class Team(
    @SerializedName("response")
    val ids: List<UserDto>? = null

)
