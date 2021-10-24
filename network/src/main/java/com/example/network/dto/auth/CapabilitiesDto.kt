package com.example.network.dto.auth

data class CapabilitiesDto(
    val ldapEnabled: Boolean = false,
    val ssoUrl: String = "",
    val ssoLabel: String = "",
    val providers: List<String> = emptyList()
)