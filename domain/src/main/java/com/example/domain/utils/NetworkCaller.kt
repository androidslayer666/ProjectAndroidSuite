package com.example.domain.utils

suspend fun <T> networkCaller(
    call: suspend () -> T,
    onSuccess: suspend (T) -> Unit,
    onSuccessString: String = "",
    onFailureString: String = ""
): Result<String, String> {
    return try {
        val result = call()
        if (result != null) {
            onSuccess(result)
            Success(onSuccessString)
        } else {
            Failure(onFailureString)
        }
    } catch (e: Exception) {
        Failure(onFailureString)
    }
}