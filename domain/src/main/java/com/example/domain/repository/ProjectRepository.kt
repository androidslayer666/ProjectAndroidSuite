package com.example.domain.repository

import android.util.Log
import com.example.database.dao.ProjectDao
import com.example.database.dao.TeamDao
import com.example.database.entities.ProjectEntity
import com.example.domain.mappers.fromEntityToPost
import com.example.domain.mappers.toListEntities
import com.example.network.dto.ProjectDto
import com.example.network.endpoints.ProjectEndPoint
import com.example.network.endpoints.TeamEndPoint
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectRepository @Inject constructor(
    private val projectDao: ProjectDao,
    private val projectEndPoint: ProjectEndPoint,
    private val teamEndPoint: TeamEndPoint,
    private val teamDao: TeamDao
) {
    suspend fun getProjects() {
        val projectsFromEndPoint = projectEndPoint.getAllProjects().listProjectDtos
        val newListOfProjects = mutableListOf<ProjectDto>()
        if (projectsFromEndPoint != null) {
            for (project in projectsFromEndPoint) {
                val projectFrom = projectEndPoint.getProjectById(project.id)
                val team = teamEndPoint.getProjectTeam(project.id)
                projectFrom.projectDto?.team = team.ids?.toMutableList()
                newListOfProjects.add(projectFrom.projectDto!!)
            }
        }
        projectDao.deleteAllProjects()
        projectDao.insertProjects(newListOfProjects.toListEntities())
    }

    suspend fun getProjectFromDbById(projectId: Int): ProjectEntity {
        return projectDao.getProjectFromDbById(projectId)
    }

    fun getAllStoredProjects(): Flow<List<ProjectEntity>> {
        return projectDao.getAll()
    }

    val projectsFromDb = projectDao.getAll()

    suspend fun updateProject(project: ProjectEntity) {
        Log.d("ProjectRepository", "updating project" + project.fromEntityToPost().toString())
        val response = projectEndPoint.updateProject(project.id, project.fromEntityToPost())
        Log.d("ProjectRepository", "got a response when updating" + project.toString())
        if (response.projectDto != null) {
            Log.d("ProjectRepository", "deleting project from db" + project.toString())
            projectDao.updateProject(project)
        }
    }

    suspend fun createProject(project: ProjectEntity) {
        val response = projectEndPoint.createProject(project.fromEntityToPost())
        Log.d("ProjectRepository", response.toString())
        if (response != null) {
            Log.d("ProjectRepository", "Task deleted")
            projectDao.insertProject(project)
        } else {

        }
    }

    suspend fun deleteProject(project: ProjectEntity) {
        Log.d("ProjectRepository", "Start deleting the task")

        val response = projectEndPoint.deleteProject(project.id)
        if (response.projectDto != null) {
            Log.d("ProjectRepository", "Task deleted")
            getProjects()
        } else {
            Log.d("TaskRepository", "Failed to delete the task")
        }

    }
}