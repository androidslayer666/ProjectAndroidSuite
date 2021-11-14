package com.example.domain.interactor.comment

import com.example.domain.model.Comment
import com.example.domain.utils.Result

interface PutCommentToTask {
    suspend operator fun invoke(taskId: Int, comment: Comment): Result<String, String>
}