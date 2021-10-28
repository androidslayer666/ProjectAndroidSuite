package com.example.domain.utils

import com.example.domain.model.User

fun List<User>.getListUserIdsFromList(): List<String> {
    val listIds = mutableListOf<String>()
    for (user in this) {
        listIds.add(user.id)
    }
    return listIds
}