package com.example.domain.interactorimpl.comment


import com.example.domain.interactor.comment.DeleteComment
import com.example.domain.repository.CommentRepository
import com.example.domain.utils.Failure
import com.example.domain.utils.Result
import com.example.domain.utils.Success
import com.example.domain.utils.toStringString


class DeleteCommentImpl(
    private val commentRepository: CommentRepository
) : DeleteComment {
    override suspend operator fun invoke(commentId: String, taskId: Int?): Result<String, String> {

        return if (taskId != null)
            commentRepository.deleteComment(commentId, taskId).toStringString()
        else
            Failure("can't figure out task Id")
    }
}