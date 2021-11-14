package com.example.domain.filters.project

import com.example.domain.model.Project
import com.example.domain.model.User

data class ProjectFilter(
    var searchQuery: String? = null,
    var responsible: User? = null,
    var status: ProjectStatus? = null
)


fun List<Project>.filterProjectsByFilter(filter: ProjectFilter?): List<Project> {
    val newList = this.toMutableList()
    //Log.d("filterProjectsBySearch", filter.toString())
    this.forEach { project ->
        filter?.searchQuery?.let {
            if (!project.title.contains(it.trim(), true)) {
                newList.remove(project)
            }
        }
        filter?.responsible?.let{
            if(it.id != project.responsible?.id){
                newList.remove(project)
            }
        }
        filter?.status?.let{
            if(it != project.status)
            {
                newList.remove(project)
            }
        }
    }
    return newList.toList()
}