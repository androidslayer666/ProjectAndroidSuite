package com.example.projectandroidsuite.logic

import com.example.database.entities.ProjectEntity
import com.example.database.entities.TaskEntity

enum class ProjectSorting {
    STAGE_ASC, STAGE_DESC, CREATED_ASC, CREATED_DESC
}

fun List<ProjectEntity>.sortProjects(sorting: ProjectSorting?): List<ProjectEntity> {
    return if (sorting != null)
        when (sorting) {
            ProjectSorting.STAGE_ASC -> this.sortedBy { it.status }
            ProjectSorting.STAGE_DESC -> this.sortedByDescending { it.status }
            ProjectSorting.CREATED_ASC -> this.sortedBy { it.created }
            ProjectSorting.CREATED_DESC -> this.sortedByDescending { it.status }
        }
    else
        this
}


enum class TaskSorting {
    STAGE_ASC, STAGE_DESC, DEADLINE_ASC, DEADLINE_DESC, IMPORTANT_ASC, IMPORTANT_DESC

}


fun List<TaskEntity>.sortTasks(sorting: TaskSorting?): List<TaskEntity> {
    return if (sorting != null)
        when (sorting) {
            TaskSorting.STAGE_ASC -> this.sortedBy { it.status }
            TaskSorting.STAGE_DESC -> this.sortedByDescending { it.status }
            TaskSorting.DEADLINE_ASC -> this.sortedBy { it.deadline }
            TaskSorting.DEADLINE_DESC -> this.sortedByDescending { it.deadline }
            TaskSorting.IMPORTANT_ASC -> this.sortedBy { it.priority }
            TaskSorting.IMPORTANT_DESC -> this.sortedByDescending { it.priority }
        }
    else
        this
}