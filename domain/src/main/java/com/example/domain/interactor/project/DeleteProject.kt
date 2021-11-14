package com.example.domain.interactor.project

import com.example.domain.utils.Result

interface DeleteProject {
    suspend operator fun invoke(projectId: Int): Result<String, String>
}