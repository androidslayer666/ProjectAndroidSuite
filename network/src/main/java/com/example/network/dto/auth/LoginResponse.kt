package com.example.network.dto.auth

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("code")
    var code: Int,

    @SerializedName("response")
    var token: Token,

    )