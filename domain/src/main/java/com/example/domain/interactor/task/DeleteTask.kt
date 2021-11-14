package com.example.domain.interactor.task

import com.example.domain.utils.Result

interface DeleteTask {
    suspend operator fun invoke(taskId: Int?): Result<String, String>
}