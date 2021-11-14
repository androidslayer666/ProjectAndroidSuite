package com.example.domain.filters.task

import com.example.domain.model.Task
import com.example.domain.model.User
import com.example.domain.utils.getListUserIdsFromList
import java.util.*

data class TaskFilter(
    var searchQuery: String? = null,
    var responsible: User? = null,
    var status: TaskStatus? = null,
    var interval: Pair<Date?, Date?>? = null
)


fun List<Task>.filterTaskByFilter(filter: TaskFilter?): List<Task> {
    val newList = this.toMutableList()
    this.forEach { task ->
        filter?.searchQuery?.let {
            if (!task.title.contains(it.trim(), true)) {
                newList.remove(task)
            }
        }
        filter?.responsible?.let {
            if(!task.responsibles.getListUserIdsFromList().contains(it.id)){
                newList.remove(task)
            }
        }
        filter?.status?.let{
            if(it != task.status) {
                newList.remove(task)
            }
        }
        filter?.interval?.let { pairInterval ->
            pairInterval.first?.let{ start ->
                if(task.deadline < start){
                    newList.remove(task)
                }
            }
            pairInterval.second?.let{ end ->
                if(task.deadline > end){
                    newList.remove(task)
                }
            }
        }
    }
    return newList.toList()
}