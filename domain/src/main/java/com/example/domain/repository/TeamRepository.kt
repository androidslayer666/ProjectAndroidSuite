package com.example.domain.repository

import android.util.Log
import com.example.database.dao.TaskDao
import com.example.database.dao.TeamDao
import com.example.database.dao.UserDao
import com.example.database.entities.TaskOrMilestoneEntity
import com.example.database.entities.UserEntity
import com.example.domain.mappers.toListEntities
import com.example.domain.mappers.toListUserEntity
import com.example.domain.mappers.toUserEntity
import com.example.network.dto.Team
import com.example.network.dto.UserDto
import com.example.network.dto.UserTransporter
import com.example.network.endpoints.MilestoneEndPoint
import com.example.network.endpoints.TaskEndPoint
import com.example.network.endpoints.TeamEndPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TeamRepository @Inject constructor(
    private val teamEndPoint: TeamEndPoint,
    private val userDao: UserDao
) {

    suspend fun getProjectTeam(projectId : Int): List<UserDto>? {
        return teamEndPoint.getProjectTeam(projectId).ids
    }

    suspend fun populateAllPortalUsers() : Result<String, String> {
        try {
            val users = teamEndPoint.getAllPortalUsers().ids
            if(users != null ){
                val newList = mutableListOf<UserDto>()
                for (user in users) {
                    if (user.isVisitor != true) {
                        newList.add(user)
                    }
                }
                userDao.insertUsers(newList.toListUserEntity())
                return Success("Comments are populated")
            }else {
                return Failure("Network/server problem")
            }
        }catch (e: Exception){
            Log.e("TeamRepository", "caught an exception" + e.toString())
            return Failure("Network/server problem")
        }
    }

    fun getAllPortalUsers() = userDao.getAll()

    suspend fun getSelfProfile(): UserEntity? {
        return teamEndPoint.getSelfProfile().user?.toUserEntity()
    }
}