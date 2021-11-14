package com.example.domain.sorting

import com.example.domain.model.Project

enum class ProjectSorting {
    STAGE_ASC, STAGE_DESC, CREATED_ASC, CREATED_DESC
}


fun List<Project>.sortProjects(sorting: ProjectSorting?): List<Project> {
    return when (sorting) {
            ProjectSorting.STAGE_ASC -> this.sortedBy { it.status }
            ProjectSorting.STAGE_DESC -> this.sortedByDescending { it.status }
            ProjectSorting.CREATED_ASC -> this.sortedBy { it.created }
            ProjectSorting.CREATED_DESC -> this.sortedByDescending { it.created }
            else -> this
        }
}