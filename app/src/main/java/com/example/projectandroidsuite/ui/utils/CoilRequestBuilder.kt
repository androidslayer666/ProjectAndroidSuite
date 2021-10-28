package com.example.projectandroidsuite.ui.utils

import android.content.Context
import coil.request.ImageRequest


fun coilRequestBuilder(uri: String, context: Context, portalAddress: String, token: String): ImageRequest {
    return ImageRequest.Builder(context)
        .data("$portalAddress$uri")
        .addHeader(
            "Authorization",
            "Bearer $token"
        )
        .build()
}