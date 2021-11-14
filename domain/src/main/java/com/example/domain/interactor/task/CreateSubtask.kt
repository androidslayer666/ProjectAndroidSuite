package com.example.domain.interactor.task

import com.example.domain.model.Subtask
import com.example.domain.utils.Result

interface CreateSubtask {
    suspend operator fun invoke(subtask: Subtask): Result<String, String>
    }