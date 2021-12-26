package com.example.domain.interactorimpl.comment

import com.example.domain.interactor.comment.GetCommentByTaskId
import com.example.domain.mappers.fromListCommentEntitiesToListComments
import com.example.domain.model.Comment
import com.example.domain.repository.CommentRepository
import com.example.domain.utils.arrangingComments
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch

class GetCommentByTaskIdImpl(
    private val commentRepository: CommentRepository
): GetCommentByTaskId {
    override suspend operator fun invoke(taskId: Int?): Flow<List<Comment>?> {
        CoroutineScope(Dispatchers.IO).launch {
            taskId?.let{ commentRepository.populateCommentsWithTaskId(it) }
        }
        return if(taskId != null) {
            commentRepository.getCommentByTaskId(taskId).transform {
                if(it != null)
                emit(arrangingComments(it))
            }
        } else flow{}
    }
}