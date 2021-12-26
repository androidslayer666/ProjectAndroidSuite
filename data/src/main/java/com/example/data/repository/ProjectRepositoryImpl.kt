package com.example.data.repository

import android.util.Log
import com.example.data.dao.ProjectDao
import com.example.data.endpoints.ProjectEndPoint
import com.example.data.endpoints.TeamEndPoint
import com.example.domain.dto.ProjectDto
import com.example.domain.dto.ProjectStatusPost
import com.example.domain.dto.ProjectTeamPost
import com.example.domain.mappers.*
import com.example.domain.model.Project
import com.example.domain.repository.ProjectRepository
import com.example.domain.utils.Result
import com.example.domain.utils.log
import com.example.domain.utils.networkCaller
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectRepositoryImpl @Inject constructor(
    private val projectDao: ProjectDao,
    private val projectEndPoint: ProjectEndPoint,
    private val teamEndPoint: TeamEndPoint,
) : ProjectRepository {

    override suspend fun getProjects(): Result<String, Throwable> {
        return networkCaller(
            call = { projectEndPoint.getAllProjects().listProjectDtos },
            onSuccess = { projects ->
                projects?.forEach { project ->
                    //gathering full info about each project
                    CoroutineScope(Dispatchers.IO).launch {
                        getTeamAndInsertProjectToDb(project.id)
                    }
                }
                clearDeletedProjects(projects)
            }
        )
    }

    private suspend fun clearDeletedProjects(list: List<ProjectDto>?) {
        projectDao.getAll().forEach { project ->
            if (list?.toListEntities()?.toListProjectIds()?.contains(project.id) != true) {
                projectDao.deleteProject(project.id)
            }
        }
    }

    override fun getProjectFromDbById(projectId: Int): Flow<Project?> {
        return projectDao.getProjectFromDbById(projectId).transform { emit(it?.fromProjectEntityToProject()) }
    }

    override fun getAllStoredProjects(): Flow<List<Project>> {
        return projectDao.getAllFlow().transform { emit(it.fromListProjectEntitiesToListProjects()) }
    }

    override suspend fun updateProject(
        projectId: Int,
        project: Project,
        projectStatus: String
    ): Result<String, Throwable> {

        return networkCaller(
            call = {
                projectEndPoint.updateProject(projectId, project.fromEntityToPost())
                updateProjectStatus(projectId, projectStatus)
                if (project.fromEntityToPost().participants != null)
                projectEndPoint.updateProjectTeam(
                    projectId,
                    ProjectTeamPost(project.fromEntityToPost().participants!!, true)
                )
            },
            onSuccess = { getTeamAndInsertProjectToDb(projectId) },
        )
    }

    override suspend fun createProject(project: Project): Result<String, Throwable> {
        return networkCaller(
            call = { projectEndPoint.createProject(project.fromEntityToPost()) },
            onSuccess = { getProjects() }
        )
    }

    override suspend fun deleteProject(projectId: Int): Result<String, Throwable> {
        return networkCaller(
            call = { projectEndPoint.deleteProject(projectId) },
            onSuccess = { getProjects() }
        )
    }

    private suspend fun updateProjectStatus(
        projectId: Int,
        projectStatus: String
    ): Result<String, Throwable> {
        return networkCaller(
            call = {
                projectEndPoint.updateProjectStatus(
                    projectId,
                    ProjectStatusPost(projectStatus)
                )
            },
            onSuccess = { getTeamAndInsertProjectToDb(projectId) }
        )
    }

    private suspend fun getTeamAndInsertProjectToDb(projectId: Int) {
        networkCaller(
            call = { projectEndPoint.getProjectById(projectId).projectDto?.toProjectEntity() },
            onSuccess = { project ->
                val team = teamEndPoint.getProjectTeam(projectId)
                project?.team = team.ids?.toListUserEntity()
                if (project != null)
                    projectDao.insertProject(project)
            }
        )
    }
}