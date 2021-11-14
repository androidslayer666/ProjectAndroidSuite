package com.example.domain.sorting

import com.example.domain.model.Task

enum class TaskSorting {
    STAGE_ASC, STAGE_DESC, DEADLINE_ASC, DEADLINE_DESC, IMPORTANT_ASC, IMPORTANT_DESC

}


fun List<Task>.sortTasks(sorting: TaskSorting?): List<Task> {
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