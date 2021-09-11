package com.example.domain.repository

import android.util.Log
import com.example.database.dao.TaskDao
import com.example.database.dao.TeamDao
import com.example.database.dao.UserDao
import com.example.database.entities.TaskOrMilestoneEntity
import com.example.domain.mappers.toListEntities
import com.example.domain.mappers.toListUserEntity
import com.example.network.dto.Team
import com.example.network.dto.UserDto
import com.example.network.endpoints.MilestoneEndPoint
import com.example.network.endpoints.TaskEndPoint
import com.example.network.endpoints.TeamEndPoint
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

    suspend fun populateAllPortalUsers(){
        teamEndPoint.getAllPortalUsers().ids?.let {
            val newList = mutableListOf<UserDto>()
            for (user in it) {
                if(user.isVisitor != true){
                    newList.add(user)
                }
            }

            userDao.insertUsers(newList.toListUserEntity()) }
    }

    suspend fun getAllPortalUsers() = userDao.getAll()

}