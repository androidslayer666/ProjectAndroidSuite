package com.example.domain.model

import com.example.network.dto.auth.TokenDto

data class LoginResponse(
    var code: Int,
    var token: TokenDto?,
    var tfa: Boolean? ,
)