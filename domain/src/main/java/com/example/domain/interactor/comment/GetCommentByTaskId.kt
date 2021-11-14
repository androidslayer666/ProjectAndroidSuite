package com.example.domain.interactor.comment

import com.example.domain.model.Comment
import kotlinx.coroutines.flow.Flow

interface GetCommentByTaskId {
    suspend operator fun invoke(taskId: Int?): Flow<List<Comment>?>
}