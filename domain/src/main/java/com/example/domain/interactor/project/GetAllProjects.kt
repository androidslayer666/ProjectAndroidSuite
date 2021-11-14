package com.example.domain.interactor.project

import com.example.domain.filters.project.ProjectStatus
import com.example.domain.model.Project
import com.example.domain.model.User
import com.example.domain.sorting.ProjectSorting
import kotlinx.coroutines.flow.Flow

interface GetAllProjects {
    suspend operator fun invoke(): Flow<List<Project>>
    fun setFilter(searchQuery: String? = null, status: ProjectStatus?= null , user: User? = null)
    fun setProjectSorting(sorting: ProjectSorting)
    }