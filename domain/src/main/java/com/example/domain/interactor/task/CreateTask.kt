package com.example.domain.interactor.task

import com.example.domain.model.Task
import com.example.domain.utils.Result

interface CreateTask {
    suspend operator fun invoke(milestoneId: Int?, task: Task): Result<String, String>
    }