package com.example.domain.dto.auth

data class CapabilitiesDto(
    val ldapEnabled: Boolean = false,
    val ssoUrl: String = "",
    val ssoLabel: String = "",
    val providers: List<String> = emptyList()
)