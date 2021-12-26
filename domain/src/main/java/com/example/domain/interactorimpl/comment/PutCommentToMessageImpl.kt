package com.example.domain.interactorimpl.comment

import com.example.domain.interactor.comment.PutCommentToMessage
import com.example.domain.model.Comment
import com.example.domain.repository.CommentRepository
import com.example.domain.repository.MessageRepository
import com.example.domain.utils.Failure
import com.example.domain.utils.Result
import com.example.domain.utils.Success
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PutCommentToMessageImpl(
    private val commentRepository: CommentRepository,
    private val messageRepository: MessageRepository
) : PutCommentToMessage {
    override suspend operator fun invoke(
        messageId: Int?,
        comment: Comment,
        projectId: Int?
    ): Result<String, String> {
        CoroutineScope(Dispatchers.IO).launch {
            messageId?.let { messageRepository.updateMessageComments(messageId, projectId?:0) }
        }

//        return messageId?.let { commentRepository.putCommentToMessage(it, comment) }
//            ?: Failure("can't figure what is the message for this comment")

        return if(messageId != null) {
            val response = commentRepository.putCommentToMessage(messageId, comment)
            when (response) {
                is Success -> Success("")
                is Failure -> Failure(response.reason.message ?:"")
            }
        } else {
            Failure("can't figure what is the message for this comment")
        }
    }
}