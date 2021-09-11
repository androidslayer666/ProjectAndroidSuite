package com.example.domain.repository

import com.example.database.entities.UserEntity
import com.example.network.dto.CommentDto

fun arrangingComments(list: List<CommentDto>): List<CommentDto> {
    val listCommentWithParent = mutableListOf<CommentDto>()
    val listCommentWithoutParent = mutableListOf<CommentDto>()
    for (comment in list) {
        if (comment.parentId != "00000000-0000-0000-0000-000000000000") {
            listCommentWithParent.add(comment)
        } else {
            listCommentWithoutParent.add(comment)
        }
    }
    return insertingCommentToParentOne(listCommentWithoutParent, listCommentWithParent)
}

fun insertingCommentToParentOne(
    parentList: MutableList<CommentDto>,
    childList: MutableList<CommentDto>
): List<CommentDto> {

//        val newChildList = childList.toMutableList()
//        val newParentList = parentList.toMutableList()
//        Log.d("insertingCommentToParen", childList.toString())
//        Log.d("insertingCommentToParen", parentList.toString())
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
                    parentList.add(parentList.indexOf(parentList[i]) + 1, childList[y])
                } else {
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
        insertingCommentToParentOne(parentList, childList)
    } else {
//            Log.d("insertingCommentToParen", "returning parent")
        return@insertingCommentToParentOne parentList
    }
//        Log.d("insertingCommentToParen", "returning empty" + "$childList.size")
    return parentList
}


sealed class Result<out Success, out Failure>

data class Success<out Success>(val value: Success) : Result<Success, Nothing>()
data class Failure<out Failure>(val reason: Failure) : Result<Nothing, Failure>()



fun List<UserEntity>.fromListUsersToStrings(): List<String> {
    val listString = mutableListOf<String>()
    for (user in this) {
        listString.add(user.id)
    }
    return listString
}


