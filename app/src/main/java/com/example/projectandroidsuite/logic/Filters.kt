package com.example.projectandroidsuite.logic

import com.example.database.entities.ProjectEntity
import com.example.database.entities.TaskEntity
import com.example.database.entities.UserEntity

data class ProjectFilter(
    var searchQuery: String? = null,
    var responsible: String? = null,
    var status: ProjectStatus? = null
)

enum class ProjectStatus(val index: Int) {
    ACTIVE(0), PAUSED(2), STOPPED(1)
}

fun List<ProjectEntity>.filterProjectsByFilter(filter: ProjectFilter): List<ProjectEntity> {
    val newList = this.toMutableList()
    //Log.d("filterProjectsBySearch", filter.toString())
    this.forEach { project ->

        if (filter.searchQuery != null)
            if (!project.title.contains(filter.searchQuery!!.trim(), true)) {
                newList.remove(project)
            }
        if (filter.responsible != null){
            if(filter.responsible != project.responsible?.id){
                newList.remove(project)
            }
        }
        if(filter.status != null){
            if(filter.status!!.index != project.status)
            {
                newList.remove(project)
            }
        }

    }
    return newList.toList()
}

data class UserFilter(
    var searchQuery: String?
)

fun List<UserEntity>.filterUsersByFilter(filter: UserFilter): MutableList<UserEntity> {
    val newList = mutableListOf<UserEntity>()
    this.forEach { user ->
        //Log.d("filterProjectsBySearch", filter.toString())
        if (filter.searchQuery != null)
            if (user.displayName.contains(filter.searchQuery!!.trim(), true)) {
                newList.add(user)
            }
    }
    return newList
}

data class TaskFilter(
    var searchQuery: String? = null,
    var responsible: String? = null,
    var status: TaskStatus? = null
)

enum class TaskStatus (val index : Int){
    ACTIVE(1), COMPLETE(0)
}

fun List<TaskEntity>.filterTaskByFilter(filter: TaskFilter): List<TaskEntity> {
    val newList = this.toMutableList()
    //Log.d("filterProjectsBySearch", filter.toString())
    this.forEach { task ->
        if (filter.searchQuery != null)
            if (!task.title.contains(filter.searchQuery!!.trim(), true)) {
                newList.remove(task)
            }
        if (filter.responsible != null){
            if(!task.responsibles.getListIds().contains(filter.responsible)){
                newList.remove(task)
            }
        }
        if(filter.status != null){
            if(filter.status!!.index != task.status) {
                newList.remove(task)
            }
        }
    }
    return newList.toList()
}

