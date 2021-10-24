package com.example.network.dto.auth

import com.google.gson.annotations.SerializedName


data class ResponseCapabilities(
    @SerializedName("folderId")
    val response: CapabilitiesDto
)