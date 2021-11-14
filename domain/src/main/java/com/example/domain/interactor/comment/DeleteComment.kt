package com.example.domain.interactor.comment

import com.example.domain.utils.Result

interface DeleteComment {
    suspend operator fun invoke(commentId: String, taskId: Int? = null): Result<String, String>
}