package com.example.domain.filters.file

import com.example.domain.model.File


data class FileFilter(
    var searchQuery: String?
)

fun List<File>.filterFileByFilter(filter: FileFilter?) : List<File>{
    val newList = this.toMutableList()
    this.forEach { file ->
        //Log.d("filterProjectsBySearch", filter.toString())
        if (filter?.searchQuery != null)
            if (!file.title?.contains(filter.searchQuery!!.trim(), true)!!) {
                newList.remove(file)
            }
    }
    return newList
}