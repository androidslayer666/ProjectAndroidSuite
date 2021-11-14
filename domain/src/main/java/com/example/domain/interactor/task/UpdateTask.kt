package com.example.domain.interactor.task

import com.example.domain.filters.task.TaskStatus
import com.example.domain.model.Task
import com.example.domain.utils.Result

interface UpdateTask {
    suspend operator fun invoke(taskId: Int?, task: Task, taskStatus: TaskStatus?): Result<String, String>
    }