package com.example.domain.interactorimpl.comment

import com.example.domain.interactor.comment.PutCommentToTask
import com.example.domain.model.Comment
import com.example.domain.repository.CommentRepository
import com.example.domain.utils.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PutCommentToTaskImpl(
    private val commentRepository: CommentRepository
) : PutCommentToTask {
    override suspend operator fun invoke(taskId: Int, comment: Comment): Result<String, String> {
        CoroutineScope(Dispatchers.IO).launch {
            commentRepository.populateCommentsWithTaskId(taskId)
        }
        return commentRepository.putCommentToTask(taskId, comment)
    }
}