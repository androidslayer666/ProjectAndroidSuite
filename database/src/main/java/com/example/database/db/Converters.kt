package com.example.database.db

import androidx.room.TypeConverter
import com.example.database.entities.CommentEntity
import com.example.database.entities.SubtaskEntity
import com.example.database.entities.UserEntity
import com.example.network.dto.UserDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*


class Converters {

    @TypeConverter
    fun fromTimestamp(value: String?): Date? {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        return value?.let { format.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): String? {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        //Log.d("dateToTimestamp", format.format(date))
        return if (date!=null) format.format(date ) else format.format(Date())
    }


    @TypeConverter
    fun fromJsonToListUser(value: String): List<UserEntity>? {
        val listType: Type = object : TypeToken<List<UserEntity>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListUserToJson(list: List<UserEntity>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromJsonToListSubtasks(value: String): List<SubtaskEntity>? {
        val listType: Type = object : TypeToken<List<SubtaskEntity>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListSubtasksToJson(list: List<SubtaskEntity>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromJsonToListComments(value: String): List<CommentEntity>? {
        val listType: Type = object : TypeToken<List<CommentEntity>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListCommentsToJson(list: List<CommentEntity>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }


}
