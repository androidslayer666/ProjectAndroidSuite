package com.example.domain.interactor.comment

import com.example.domain.Result
import com.example.domain.repository.CommentRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeleteComment(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(commentId: String, taskId: Int? = null): Result<String, String> {
        return commentRepository.deleteComment(commentId, taskId)
    }
}