package com.example.projectandroidsuite.logic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.TaskFilter
import com.example.domain.TaskStatus
import com.example.domain.filterTaskByFilter
import com.example.domain.model.Milestone
import com.example.domain.model.Task
import com.example.domain.model.User


fun validatePortalNameInput(input: String): Boolean {
    val regex = Regex(
        pattern = "([a-zA-Z]+(\\.onlyoffice+)+(\\.com|.eu|.sg)+)\$",
        options = setOf(RegexOption.IGNORE_CASE)
    )
    return regex.matches(input)
}

fun validateEmail(input: String): Boolean {
    val regex = Regex(
        pattern = "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+))",
        options = setOf(RegexOption.IGNORE_CASE)
    )
    return regex.matches(input)
}

fun validatePassword(input: String): Boolean {
    val regex = Regex(
        pattern = "(?=^.{8,}\$).*\$",
        options = setOf(RegexOption.IGNORE_CASE)
    )
    return regex.matches(input)
}

fun validateCode(input: String): Boolean {
    val regex = Regex(
        pattern = "^[0-9]*$"
    )
    return regex.matches(input)
}




fun List<User>.getListIds(): List<String> {
    val listIds = mutableListOf<String>()
    for (user in this) {
        listIds.add(user.id)
    }
    return listIds
}

fun List<User>.getUserById(id: String): User? {
    for (user in this) {
        if (user.id == id) return user
    }
    return null
}


fun <T> MutableLiveData<T>.forceRefresh() {
    if (this.value != null)
        this.value = this.value
}


fun <T, K, R> LiveData<T>.combineWith(
    liveData: LiveData<K>,
    block: (T?, K?) -> R
): LiveData<R> {
    val result = MediatorLiveData<R>()
    result.addSource(this) {
        result.value = block(this.value, liveData.value)
    }
    result.addSource(liveData) {
        result.value = block(this.value, liveData.value)
    }
    return result
}


fun arrangeMilestonesAndTasks(
    listMilestones: List<Milestone>,
    listTasks: List<Task>
): Map<Milestone?, List<Task>> {
    val map = mutableMapOf<Milestone?, List<Task>>()
    val listTasksWithoutMilestone = mutableListOf<Task>()
    for (milestone in listMilestones) {
        val listTasksWithMilestone = mutableListOf<Task>()
        for (task in listTasks) {
            if (task.milestoneId == milestone.id) {
                listTasksWithMilestone.add(task)
            }
            if(task.milestoneId == null){
                if(!listTasksWithoutMilestone.map{it.id}.contains(task.id))
                listTasksWithoutMilestone.add(task)
            }
        }
        map[milestone] = listTasksWithMilestone.filterTaskByFilter(TaskFilter(status = TaskStatus.ACTIVE))
    }
    map[null] = listTasksWithoutMilestone.filterTaskByFilter(TaskFilter(status = TaskStatus.ACTIVE))
    return map
}

