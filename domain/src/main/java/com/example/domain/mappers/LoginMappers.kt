package com.example.domain.mappers

import com.example.domain.model.LoginResponse
import com.example.network.dto.auth.LoginResponseDto

fun LoginResponseDto.fromLoginResponseDtoToLoginResponse() : LoginResponse{
    return LoginResponse(
        code = this.code,
        token = this.token,
        tfa = this.token?.tfa
    )
}