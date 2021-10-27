package com.example.domain.interactor.comment

import com.example.domain.Result
import com.example.domain.model.Comment
import com.example.domain.repository.CommentRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PutCommentToTask(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(taskId: Int, comment: Comment): Result<String, String> {
        CoroutineScope(Dispatchers.IO).launch {
            commentRepository.populateCommentsWithTaskId(taskId)
        }
        return commentRepository.putCommentToTask(taskId, comment)
    }
}