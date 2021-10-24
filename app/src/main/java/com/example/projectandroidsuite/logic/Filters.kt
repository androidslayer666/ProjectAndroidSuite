package com.example.projectandroidsuite.logic

import com.example.database.entities.*
import com.example.domain.model.*
import java.util.*

data class ProjectFilter(
    var searchQuery: String? = null,
    var responsible: String? = null,
    var status: ProjectStatus? = null
)

enum class ProjectStatus(val index: Int) {
    ACTIVE(0), PAUSED(2), STOPPED(1)
}

fun List<Project>.filterProjectsByFilter(filter: ProjectFilter): List<Project> {
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

fun List<User>.filterUsersByFilter(filter: UserFilter): MutableList<User> {
    val newList = mutableListOf<User>()
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
    var status: TaskStatus? = null,
    var interval: Pair<Date?, Date?>? = null
)

enum class TaskStatus (val index : Int){
    ACTIVE(1), COMPLETE(2)
}

fun List<Task>.filterTaskByFilter(filter: TaskFilter): List<Task> {
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
        filter.interval?.let {
            filter.interval!!.first?.let{
            }
            filter.interval!!.second?.let{
            }
        }
    }
    return newList.toList()
}

data class MilestoneFilter(
    var searchQuery: String?
)

fun List<Milestone>.filterMilestoneByFilter(filter: MilestoneFilter) : List<Milestone>{
    val newList = mutableListOf<Milestone>()
    this.forEach { milestone ->
        //Log.d("filterProjectsBySearch", filter.toString())
        if (filter.searchQuery != null)
            if (milestone.title?.contains(filter.searchQuery!!.trim(), true) == true) {
                newList.add(milestone)
            }
    }
    return newList
}

data class FileFilter(
    var searchQuery: String?
)

fun List<File>.filterFileByFilter(filter: FileFilter) : List<File>{
    val newList = mutableListOf<File>()
    this.forEach { file ->
        //Log.d("filterProjectsBySearch", filter.toString())
        if (filter.searchQuery != null)
            if (file.title?.contains(filter.searchQuery!!.trim(), true) == true) {
                newList.add(file)
            }
    }
    return newList
}