package com.example.domain.interactor.task

import com.example.domain.filters.task.TaskStatus
import com.example.domain.utils.Result

interface UpdateTaskStatus {
    suspend operator fun invoke(taskId: Int?, status: TaskStatus?): Result<String, String>
    }