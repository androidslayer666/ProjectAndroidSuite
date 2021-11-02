package com.example.projectandroidsuite.ui.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.domain.utils.Failure
import com.example.domain.utils.Result
import com.example.domain.utils.Success


fun makeToast(text: String, context: Context) {
    Toast.makeText(
        context, text,
        Toast.LENGTH_SHORT
    ).show()
}

fun showResultToast(
    result: Result<String, String>?,
    onSuccess: () -> Unit,
    context: Context
) {
    when (result) {
        is Success -> {
            Log.d("deleteProject", result.toString())
            makeToast((result).value, context)
            onSuccess()
        }
        is Failure -> {
            Log.d("deleteProject", result.toString())
            makeToast((result).reason, context)
        }
    }
}
