package com.example.domain.utils

fun Result<String, Throwable>.toStringString(): Result<String, String> {
    return when (this) {
        is Success -> Success("")
        is Failure -> Failure(this.reason.message ?:"")
    }
}