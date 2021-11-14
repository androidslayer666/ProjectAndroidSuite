package com.example.domain.interactorimpl.comment


import com.example.domain.interactor.comment.DeleteComment
import com.example.domain.repository.CommentRepository
import com.example.domain.utils.Result


class DeleteCommentImpl (
    private val commentRepository: CommentRepository
) : DeleteComment {
    override suspend operator fun invoke(commentId: String, taskId: Int?): Result<String, String> {
        return commentRepository.deleteComment(commentId, taskId)
    }
}