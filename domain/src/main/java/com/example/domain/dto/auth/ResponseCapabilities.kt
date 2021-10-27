package com.example.domain.dto.auth

import com.google.gson.annotations.SerializedName


data class ResponseCapabilities(
    @SerializedName("folderId")
    val response: CapabilitiesDto
)