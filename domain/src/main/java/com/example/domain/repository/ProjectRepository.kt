package com.example.domain.repository

import android.util.Log
import com.example.database.dao.ProjectDao
import com.example.database.entities.ProjectEntity
import com.example.domain.mappers.toListEntities
import com.example.domain.mappers.toListUserEntity
import com.example.domain.mappers.toProjectEntity
import com.example.network.dto.ProjectDto
import com.example.network.dto.ProjectPost
import com.example.network.dto.ProjectStatusPost
import com.example.network.dto.ProjectTeamPost
import com.example.network.endpoints.ProjectEndPoint
import com.example.network.endpoints.TeamEndPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectRepository @Inject constructor(
    private val projectDao: ProjectDao,
    private val projectEndPoint: ProjectEndPoint,
    private val teamEndPoint: TeamEndPoint,
) {

    suspend fun getProjects(): Result<String, String> {
        return networkCaller(
            call = { projectEndPoint.getAllProjects().listProjectDtos },
            onSuccess = { projects ->
                projects?.forEach { project ->
                    //gathering full info about each project
                    CoroutineScope(IO).launch {
                        getTeamAndInsertProjectToDb(project.id)
                    }
                }
                clearDeletedProjects(projects)
            }
        )
    }

    private suspend fun clearDeletedProjects(list: List<ProjectDto>?) {
        projectDao.getAll().forEach { project ->
            //Log.d("ProjectRepository", list?.map{ it.title}.toString())
            if (list?.toListEntities()?.toListProjectIds()?.contains(project.id) != true) {
                //Log.d("ProjectRepository", project.title)
                projectDao.deleteProject(project.id)
            }
        }
    }

    fun getProjectFromDbById(projectId: Int): Flow<ProjectEntity?> {
        return projectDao.getProjectFromDbById(projectId)
    }

    fun getAllStoredProjects(): Flow<List<ProjectEntity>> {
        return projectDao.getAllFlow()
    }

    val projectsFromDb = projectDao.getAllFlow()

    suspend fun updateProject(
        projectId: Int,
        project: ProjectPost,
        projectStatus: String
    ): Result<String, String> {
        Log.d("ProjectRepository", "list users  " + project.participants?.toString())
        return networkCaller(
            call = {
                projectEndPoint.updateProject(projectId, project)
                updateProjectStatus(projectId, projectStatus)
                if(project.participants != null)
                    Log.d("ProjectRepository", project.participants.toString())
                projectEndPoint.updateProjectTeam(projectId, ProjectTeamPost(project.participants!!, true) )
            },
            onSuccess = { getTeamAndInsertProjectToDb(projectId) }
        )
    }

    suspend fun createProject(project: ProjectPost): Result<String, String> {
        return networkCaller(
            call = { projectEndPoint.createProject(project) },
            onSuccess = { getProjects() },
            onSuccessString = "Project created successfully",
            onFailureString = "Having problem while creating the project, please check the network connection"
        )
    }

    suspend fun deleteProject(projectId: Int): Result<String, String> {
        return networkCaller(
            call = { projectEndPoint.deleteProject(projectId) },
            onSuccess = { },
            onSuccessString = "Project deleted successfully",
            onFailureString = "Having problem while deleting the project, please check the network connection"
        )
    }

    private suspend fun updateProjectStatus(
        projectId: Int,
        projectStatus: String
    ): Result<String, String> {
        return networkCaller(
            call = {
                projectEndPoint.updateProjectStatus(
                    projectId,
                    ProjectStatusPost(projectStatus)
                )
            },
            onSuccess = { getTeamAndInsertProjectToDb(projectId) },
            onSuccessString = "Project status updated successfully",
            onFailureString = "Having problem while deleting the project, please check the network connection"
        )
    }

    private suspend fun getTeamAndInsertProjectToDb(projectId: Int) {
        networkCaller(
            call = { projectEndPoint.getProjectById(projectId).projectDto?.toProjectEntity() },
            onSuccess = { project ->
                val team = teamEndPoint.getProjectTeam(projectId)
                //Log.d("getTeamAndInsertToDb", team.toString())
                project?.team = team.ids?.toListUserEntity()
                //Log.d("getTeamAndInsertToDb", project.toString())
                if (project != null)
                    projectDao.insertProject(project)
            }
        )
    }
}




