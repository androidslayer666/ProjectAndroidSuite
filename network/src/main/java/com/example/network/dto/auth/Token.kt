package com.example.network.dto.auth

import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName("expires")
    var tokenExpirationDate: String,
    @SerializedName("token")
    var authToken: String,
)