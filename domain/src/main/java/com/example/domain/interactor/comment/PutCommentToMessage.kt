package com.example.domain.interactor.comment

import com.example.domain.utils.Failure
import com.example.domain.utils.Result
import com.example.domain.model.Comment
import com.example.domain.repository.CommentRepository
import com.example.domain.repository.MessageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PutCommentToMessage(
    private val commentRepository: CommentRepository,
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(
        messageId: Int?,
        comment: Comment,
        projectId: Int?
    ): Result<String, String> {
        CoroutineScope(Dispatchers.IO).launch {
            messageId?.let { messageRepository.updateMessageComments(messageId, projectId?:0) }
        }

        return messageId?.let { commentRepository.putCommentToMessage(it, comment) }
            ?: Failure("can't figure what is the message for this comment")
    }
}