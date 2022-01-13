package com.example.domain.interactor.task

import com.example.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface GetOverdueTasks {
    suspend operator fun invoke() : Flow<List<Task>>
}