package com.example.domain.utils

import com.example.domain.filters.task.TaskFilter
import com.example.domain.filters.task.TaskStatus
import com.example.domain.filters.task.filterTaskByFilter
import com.example.domain.model.Milestone
import com.example.domain.model.Task

fun arrangeMilestonesAndTasks(
    listMilestones: List<Milestone>,
    listTasks: List<Task>
): Map<Milestone?, List<Task>> {
    val map = mutableMapOf<Milestone?, List<Task>>()
    val listTasksWithoutMilestone = mutableListOf<Task>()
    for (milestone in listMilestones) {

        val listTasksWithMilestone = mutableListOf<Task>()
        for (task in listTasks) {
            if (task.milestoneId == milestone.id) {
                listTasksWithMilestone.add(task)
            }
            if(task.milestoneId == null){
                if(!listTasksWithoutMilestone.map{it.id}.contains(task.id))
                    listTasksWithoutMilestone.add(task)
            }
        }
        map[milestone] = listTasksWithMilestone.filterTaskByFilter(TaskFilter(status = TaskStatus.ACTIVE))
    }
    map[null] = listTasksWithoutMilestone.filterTaskByFilter(TaskFilter(status = TaskStatus.ACTIVE))
    return map
}
