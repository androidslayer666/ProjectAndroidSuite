package com.example.domain.interactor.comment

import com.example.domain.model.Comment
import com.example.domain.repository.CommentRepository
import com.example.domain.utils.Result
import com.example.domain.utils.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRepository : CommentRepository {
    override suspend fun populateCommentsWithTaskId(taskId: Int): Result<String, String> {
        return Success("")
    }

    override fun getCommentByTaskId(taskId: Int?): Flow<List<Comment>?> {
        return flow{emit(listOf(Comment(id = "", text = "Fake comment")))}
    }

    override suspend fun putCommentToMessage(
        messageId: Int,
        comment: Comment
    ): Result<String, String> {
        return Success("")
    }

    override suspend fun putCommentToTask(taskId: Int, comment: Comment): Result<String, String> {
        return Success("")
    }

    override suspend fun deleteComment(commentId: String, taskId: Int?): Result<String, String> {
        return Success("")
    }
}