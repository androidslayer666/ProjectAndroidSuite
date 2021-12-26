package com.example.domain.utils

import android.util.Log
import retrofit2.HttpException

suspend fun <T> networkCaller(
    call: suspend () -> T,
    onSuccess: suspend (T) -> Unit,
): Result<String, Throwable> {
    return try {
        val result = call()
        if (result != null) {
            onSuccess(result)
            Success("")
        } else {
            Log.d("networkCaller", "failure" + result.toString())
            Failure(HttpException(result))
        }
    } catch (e: Exception) {
        Failure(e)
    }
}