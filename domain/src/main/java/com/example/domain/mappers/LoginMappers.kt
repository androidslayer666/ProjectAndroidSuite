package com.example.domain.mappers

import com.example.domain.dto.auth.LoginResponseDto
import com.example.domain.model.LoginResponse

fun LoginResponseDto.fromLoginResponseDtoToLoginResponse() : LoginResponse{
    return LoginResponse(
        code = this.code,
        token = this.token,
        tfa = this.token?.tfa
    )
}