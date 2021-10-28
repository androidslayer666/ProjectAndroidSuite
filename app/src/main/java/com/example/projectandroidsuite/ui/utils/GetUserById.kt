package com.example.projectandroidsuite.ui.utils

import com.example.domain.model.User

fun List<User>.getUserById(id: String): User? {
    for (user in this) {
        if (user.id == id) return user
    }
    return null
}