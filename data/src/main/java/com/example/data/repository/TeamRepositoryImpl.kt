package com.example.data.repository

import android.util.Log
import com.example.data.ResponseIsEmptyException
import com.example.data.dao.UserDao
import com.example.domain.utils.Failure
import com.example.domain.utils.Result
import com.example.domain.utils.Success
import com.example.domain.mappers.fromListUserEntitiesToListUsers
import com.example.domain.mappers.fromUserEntityToUser
import com.example.domain.mappers.toListUserEntity
import com.example.domain.mappers.toUserEntity
import com.example.domain.model.User
import com.example.domain.repository.TeamRepository
import com.example.domain.dto.UserDto
import com.example.data.endpoints.TeamEndPoint
import com.example.domain.entities.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TeamRepositoryImpl @Inject constructor(
    private val teamEndPoint: TeamEndPoint,
    private val userDao: UserDao
) : TeamRepository {

    override suspend fun populateAllPortalUsers(): Result<String, Throwable> {
        return try {
            val users = teamEndPoint.getAllPortalUsers().ids
            if (users != null) {
                userDao.insertUsers(users.excludeVisitors().markSelf())
                Success("")
            } else {
                Failure(ResponseIsEmptyException())
            }
        } catch (e: Exception) {
            Failure(e)
        }
    }

    override fun getAllPortalUsers(): Flow<List<User>> {
        return userDao.getAll().transform { emit(it.fromListUserEntitiesToListUsers()) }
    }

    override suspend fun getSelfProfile(): Flow<User?> {
        return userDao.getSelfProfile().transform {
            emit(it?.fromUserEntityToUser())
        }
    }

    private fun List<UserDto>.excludeVisitors()  :List<UserEntity> {
        val newList = mutableListOf<UserEntity>()
        for (user in this) {
            if (user.isVisitor != true) {
                val userEntity = user.toUserEntity()
                newList.add(userEntity)
            }
        }
        return newList
    }

    private suspend fun List<UserEntity>.markSelf()  :List<UserEntity> {
        val self = teamEndPoint.getSelfProfile().user
        for (user in this) {
            if(user.id == self?.id) {
                user.self = true
            }
        }
        return this
    }
}