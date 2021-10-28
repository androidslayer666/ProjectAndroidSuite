package com.example.projectandroidsuite.ui.utils

import com.example.domain.model.User

fun List<User>.getListIds(): List<String> {
    val listIds = mutableListOf<String>()
    for (user in this) {
        listIds.add(user.id)
    }
    return listIds
}
