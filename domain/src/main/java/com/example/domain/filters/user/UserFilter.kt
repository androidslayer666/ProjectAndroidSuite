package com.example.domain.filters.user

import com.example.domain.model.User

data class UserFilter(
    var searchQuery: String?
)

fun List<User>.filterUsersByFilter(filter: UserFilter?): MutableList<User> {
    val newList = this.toMutableList()
    this.forEach { user ->
        //Log.d("filterProjectsBySearch", filter.toString())
        if (filter?.searchQuery != null)
            if (!user.displayName.contains(filter.searchQuery!!.trim(), true)) {
                newList.remove(user)
            }
    }
    return newList
}