package com.example.domain.dto.auth

import com.google.gson.annotations.SerializedName

data class TokenDto(
    @SerializedName("expires")
    var tokenExpirationDate: String,
    @SerializedName("token")
    var authToken: String,
    @SerializedName("tfa")
    var tfa: Boolean,
)