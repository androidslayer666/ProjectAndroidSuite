package com.example.data.repository

import android.util.Log
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TeamRepositoryImpl @Inject constructor(
    private val teamEndPoint: TeamEndPoint,
    private val userDao: UserDao
) : TeamRepository {

    override suspend fun populateAllPortalUsers(): Result<String, String> {
        try {
            val users = teamEndPoint.getAllPortalUsers().ids
            return if (users != null) {
                val newList = mutableListOf<UserDto>()
                for (user in users) {
                    if (user.isVisitor != true) {
                        newList.add(user)
                    }
                }
                userDao.insertUsers(newList.toListUserEntity())
                Success("Users are populated")
            } else {
                Failure("Network/server problem")
            }
        } catch (e: Exception) {
            Log.e("TeamRepository", "caught an exception$e")
            return Failure("Network/server problem")
        }
    }

    override fun getAllPortalUsers(): Flow<List<User>> {
        return userDao.getAll().transform { emit(it.fromListUserEntitiesToListUsers()) }
    }

    override suspend fun getSelfProfile(): User? {
        return teamEndPoint.getSelfProfile().user?.toUserEntity()?.fromUserEntityToUser()
    }
}