package com.example.domain.utils

import android.util.Log

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
            Log.d("networkCaller", "failure" + result.toString())
            Failure(onFailureString)
        }
    } catch (e: Exception) {
        Failure(onFailureString)
    }
}