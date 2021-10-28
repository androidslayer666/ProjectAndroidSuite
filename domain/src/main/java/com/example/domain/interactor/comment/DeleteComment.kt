package com.example.domain.interactor.comment

import com.example.domain.utils.Failure
import com.example.domain.utils.Result
import com.example.domain.repository.CommentRepository

class DeleteComment(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(commentId: String, taskId: Int? = null): Result<String, String> {
        return commentRepository.deleteComment(commentId, taskId)
    }
}