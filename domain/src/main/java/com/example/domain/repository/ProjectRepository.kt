package com.example.domain.repository

import android.util.Log
import com.example.database.dao.ProjectDao
import com.example.database.entities.ProjectEntity
import com.example.domain.mappers.toListEntities
import com.example.domain.mappers.toListUserEntity
import com.example.domain.mappers.toProjectEntity
import com.example.network.dto.ProjectPost
import com.example.network.dto.ProjectStatusPost
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
        try {
            // getting a list with a limited information about projects
            val projectsFromNetwork =
                projectEndPoint.getAllProjects().listProjectDtos?.toListEntities()
            if (projectsFromNetwork == null) {
                return Failure("Something wrong with network or server")
            }
            //Log.d("ProjectRepository", "Projects from network" + projectsFromNetwork.toString())

            projectsFromNetwork.forEach { project ->
                //gathering full info about each project
                //Log.d("ProjectRepository", project.toString())
                CoroutineScope(IO).launch {
                    try {
                        val projectFrom = projectEndPoint.getProjectById(project.id).projectDto
                        val team = teamEndPoint.getProjectTeam(project.id)
                        // adding team
                        projectFrom?.team = team.ids?.toMutableList()
                        projectFrom?.toProjectEntity()?.let { projectDao.insertProject(it) }
                    } catch (e: Exception) {
                        //Log.e("ProjectRepository", e.toString())
                    }
                }
            }
            //Log.d("ProjectRepository", "Projects are populated")
            projectDao.getAll().forEach { project ->
                if (!projectsFromNetwork.toListProjectIds().contains(project.id))
                //Log.d("ProjectRepository", project.title)
                    projectDao.deleteProject(project.id)
            }
            return Success("Projects are populated")
        } catch (e: Exception) {
            //Log.e("ProjectRepository", e.toString())
            return Failure("Something wrong with network or server")
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
        try {
            //Log.d("ProjectRepository", "updating project" + project.toString())
            val response = projectEndPoint.updateProject(projectId, project)

            updateProjectStatus(projectId , projectStatus)
            //Log.d("ProjectRepository", "got a response when updating" + project.toString())
            if (response.projectDto != null) {
                //todo update in dao
                val renewedProject =
                    projectEndPoint.getProjectById(projectId).projectDto?.toProjectEntity()
                val team = teamEndPoint.getProjectTeam(projectId)
                renewedProject?.team = team.ids?.toListUserEntity()
                //Log.d("ProjectRepository", "updating project from db" + renewedProject.toString())
                if (renewedProject != null)
                    projectDao.updateProject(renewedProject)

                return Success("Project successfully updated")
            } else {
                return Failure("Error during updating the project")
            }
        } catch (e: IOException) {
            Log.d(
                "ProjectRepository",
                "tried to create a project but caught an exception: ${e.message}"
            )
            return Failure("Error during updating the project")
        }
    }

    suspend fun createProject(project: ProjectPost): Result<String, String> {
        Log.d("ProjectRepository", "Started creating pproject")
        try {
            val response = projectEndPoint.createProject(project)
            Log.d("ProjectRepository", response.toString())
            if (response != null) {
                Log.d("ProjectRepository", "Project created")
                //because response from server doesn't give id
                getProjects()
                return Success("Project successfully create")
            } else {
                return Failure("Project was not created due to a server problem")
            }
        } catch (e: Exception) {
            Log.d(
                "ProjectRepository",
                "tried to create a project but caught an exception: ${e.message}"
            )
            return Failure("Project was not created, please check network or ask the developer to fix this")
        }
    }

    suspend fun deleteProject(projectId: Int): Result<String, String> {
        Log.d("ProjectRepository", "Start deleting the project")
        try {
            val response = projectEndPoint.deleteProject(projectId)
            if (response.projectDto != null) {
                Log.d("ProjectRepository", "project deleted")
                //projectDao.deleteProject(projectId)
                //getProjects()
                return Success("Project successfully deleted")
            } else {
                Log.d("ProjectRepository", "Failed to delete the project")
                return Failure("Project was not created due to a server problem")
            }
        } catch (e: IOException) {
            Log.d(
                "ProjectRepository",
                "tried to create a project but caught an exception: ${e.message}"
            )
            return Failure("Project was not deleted, please check network or ask the developer to fix this")
        }
    }

    suspend fun updateProjectStatus(projectId: Int, projectStatus: String): Result<String, String> {
        try {
            Log.d("ProjectRepository", "updating project" + projectId.toString())
            val response =
                projectEndPoint.updateProjectStatus(projectId, ProjectStatusPost(projectStatus))
            Log.d("ProjectRepository", "got a response when updating" + projectStatus)
            if (response != null) {
                val renewedProject =
                    projectEndPoint.getProjectById(projectId).projectDto?.toProjectEntity()
                val team = teamEndPoint.getProjectTeam(projectId)
                renewedProject?.team = team.ids?.toListUserEntity()
                Log.d("ProjectRepository", "updating project from db" + renewedProject.toString())
                if (renewedProject != null)
                    projectDao.insertProject(renewedProject)

                return Success("Project successfully updated")
            } else {
                return Failure("Error during updating the project")
            }
        } catch (e: IOException) {
            Log.d(
                "ProjectRepository",
                "tried to create a project but caught an exception: ${e.message}"
            )
            return Failure("Error during updating the project")
        }
    }

}