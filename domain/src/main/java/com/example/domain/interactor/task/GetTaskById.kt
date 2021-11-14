package com.example.domain.interactor.task

import com.example.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface GetTaskById {
    operator fun invoke(taskId: Int): Flow<Task?>
    }