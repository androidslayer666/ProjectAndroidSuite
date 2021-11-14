package com.example.domain.interactor.comment

import com.example.domain.model.Comment
import com.example.domain.utils.Result

interface PutCommentToMessage {
    suspend operator fun invoke(
        messageId: Int?,
        comment: Comment,
        projectId: Int?
    ): Result<String, String>
}