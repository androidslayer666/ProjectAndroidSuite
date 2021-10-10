package com.example.domain.repository

import android.util.Log
import com.example.database.entities.CommentEntity
import com.example.database.entities.ProjectEntity
import com.example.database.entities.TaskEntity
import com.example.database.entities.UserEntity
import com.example.domain.model.User
import com.example.network.dto.CommentDto
import com.example.network.dto.MessageDto
import com.example.network.dto.MilestoneDto
import com.example.network.dto.TaskDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

const val FORMAT_API_DATE = "yyyy-MM-dd'T'HH:mm:ss"

fun arrangingComments(list: List<CommentEntity>): List<CommentEntity> {
    val childList = mutableListOf<CommentEntity>()
    val parentList = mutableListOf<CommentEntity>()
    for (comment in list) {
        if (comment.parentId != "00000000-0000-0000-0000-000000000000") {
            //Log.d("arrangingComments", "Parent ID  !=   " + comment.parentId.toString())
            childList.add(comment)
        } else {
            //Log.d("arrangingComments", "Parent ID  ==   " + comment.parentId.toString())
            parentList.add(comment)
        }
    }
    return insertingCommentToParent(parentList, childList)
}

fun insertingCommentToParent(
    parentList: MutableList<CommentEntity>,
    childList: MutableList<CommentEntity>,
    commentLevel: Int = 1
): List<CommentEntity> {

//        val newChildList = childList.toMutableList()
//        val newParentList = parentList.toMutableList()

    //Log.d("insertingCommentToParen", "Child list" + childList.map{it.parentId}.toString())
    //Log.d("insertingCommentToParen", "Parent list" + parentList.map{it.parentId}.toString())
    var counter = childList.size

    for (i in 0 until parentList.size) {
        for (y in 0 until counter) {
//                Log.d("insertingCommentToParen", "i  "+ i.toString())
//                Log.d("insertingCommentToParen", "y  "+y.toString())
//                Log.d("insertingCommentToParen", "counter  "+counter.toString())
            if (counter == (y)) {
//                    Log.d("insertingCommentToParen", " LOop is breaked"+counter.toString())
                break
            }
//                Log.d("insertingCommentToParen", "i  "+ childList[y].toString())
//                Log.d("insertingCommentToParen", "y  "+ parentList[i].toString())
            if (childList[y].parentId == parentList[i].id) {
                if (parentList.indexOf(parentList[i]) != parentList.size - 1) {
                    //Log.d("insertingCommentToParen", "setting level to  $commentLevel"+ parentList[i].toString())
                    childList[y].commentLevel = commentLevel
                    parentList.add(parentList.indexOf(parentList[i]) + 1, childList[y])
                } else {
                    childList[y].commentLevel = commentLevel
                    parentList.add(childList[y])
                }
                childList.remove(childList[y])
                counter -= 1

//                    Log.d("insertingCommentToParen", childList.size.toString())
            }
        }
    }
    if (childList.size > 0) {
//            Log.d("insertingCommentToParen", childList.toString())
        insertingCommentToParent(parentList, childList, commentLevel + 1)
    } else {
//            Log.d("insertingCommentToParen", "returning parent")
        return@insertingCommentToParent parentList
    }
//        Log.d("insertingCommentToParen", "returning empty" + "$childList.size")
    return parentList
}


sealed class Result<out Success, out Failure>

data class Success<out Success>(val value: Success) : Result<Success, Nothing>()
data class Failure<out Failure>(val reason: Failure) : Result<Nothing, Failure>()


fun List<User>.fromListUsersToStrings(): List<String> {
    val listString = mutableListOf<String>()
    for (user in this) {
        listString.add(user.id)
    }
    return listString
}

fun List<ProjectEntity>.toListProjectIds(): MutableList<Int> {
    val listString = mutableListOf<Int>()
    for (project in this) {
        listString.add(project.id)
    }
    return listString
}

fun List<TaskDto>.toListTaskIds(): MutableList<Int> {
    val listString = mutableListOf<Int>()
    for (task in this) {
        listString.add(task.id)
    }
    return listString
}


fun Date.dateToString(): String {
    return SimpleDateFormat(FORMAT_API_DATE).format(this)
}

fun List<User>.toStringIds(): String {
    var string = ""
    this.map {
        string += it.id + ","
    }
    return string.dropLast(1)
}

fun List<CommentDto>.toListCommentIds(): MutableList<String> {
    val listString = mutableListOf<String>()
    for (comment in this) {
        listString.add(comment.id)
    }
    return listString
}

fun List<MilestoneDto>.toListMilestoneIds(): MutableList<Int> {
    val listString = mutableListOf<Int>()
    for (milestone in this) {
        listString.add(milestone.id)
    }
    return listString
}

fun List<MessageDto>.toListMessageIds() : List<Int> {
    val listString = mutableListOf<Int>()
    for (message in this) {
        listString.add(message.id)
    }
    return listString
}

fun List<TaskDto>.setStatus(status: Int): List<TaskDto> {
    this.forEach { it.status = status }
    return this
}


suspend fun <T> networkCaller(
    call: suspend () -> T,
    onSuccess: suspend (T) -> Unit,
    onSuccessString: String = "",
    onFailureString: String = ""
): Result<String, String> {
    try {
        val result = call()
        if (result != null) {
            onSuccess(result)
            return Success(onSuccessString)
        } else {
            return Failure(onFailureString)
        }
    } catch (e: Exception) {
        return Failure(onFailureString)
    }
}