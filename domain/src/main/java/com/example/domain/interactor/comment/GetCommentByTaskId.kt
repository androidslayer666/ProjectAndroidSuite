package com.example.domain.interactor.comment

import com.example.domain.model.Comment
import com.example.domain.repository.CommentRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class GetCommentByTaskId(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(taskId: Int?): Flow<List<Comment>?> {
        CoroutineScope(Dispatchers.IO).launch {
            taskId?.let{ commentRepository.populateCommentsWithTaskId(it) }
        }
        return taskId?.let {
            commentRepository.populateCommentsWithTaskId(taskId)
            commentRepository.getCommentByTaskId(taskId)
        } ?: flow{}
    }
}