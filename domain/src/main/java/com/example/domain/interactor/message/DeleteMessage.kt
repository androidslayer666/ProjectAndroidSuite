package com.example.domain.interactor.message

import com.example.domain.utils.Result

interface DeleteMessage {
    suspend operator fun invoke(messageId: Int, projectId: Int?): Result<String, String>
}