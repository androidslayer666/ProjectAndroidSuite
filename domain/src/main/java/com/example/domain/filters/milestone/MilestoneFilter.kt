package com.example.domain.filters.milestone

import com.example.domain.model.Milestone


data class MilestoneFilter(
    var searchQuery: String?
)

fun List<Milestone>.filterMilestoneByFilter(filter: MilestoneFilter?) : List<Milestone>{
    val newList = this.toMutableList()
    this.forEach { milestone ->
        //Log.d("filterProjectsBySearch", filter.toString())
        filter?.searchQuery?.let {
            if (!milestone.title?.contains(filter.searchQuery!!.trim(), true)!!) {
                newList.remove(milestone)
            }
        }
    }
    return newList
}