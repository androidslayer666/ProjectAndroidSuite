package com.example.network.dto.auth

import com.google.gson.annotations.SerializedName

data class LoginRequestDto(
    @SerializedName("userName")
    var email: String,

    @SerializedName("password")
    var password: String
)
