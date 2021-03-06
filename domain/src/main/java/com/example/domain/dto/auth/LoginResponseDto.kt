package com.example.domain.dto.auth

import com.google.gson.annotations.SerializedName

data class LoginResponseDto(
    @SerializedName("code")
    var code: Int,
    @SerializedName("response")
    var token: TokenDto?,
    )