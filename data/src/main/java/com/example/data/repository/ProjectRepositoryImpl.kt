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

    override suspend fun getProjects(): Result<String, String> {
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
            //Log.d("ProjectRepository", list?.map{ it.title}.toString())
            if (list?.toListEntities()?.toListProjectIds()?.contains(project.id) != true) {
                //Log.d("ProjectRepository", project.title)
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
    ): Result<String, String> {
        Log.d(
            "ProjectRepository",
            "list users  " + project.fromEntityToPost().participants?.toString()
        )
        return networkCaller(
            call = {
                projectEndPoint.updateProject(projectId, project.fromEntityToPost())
                updateProjectStatus(projectId, projectStatus)
                if (project.fromEntityToPost().participants != null)
                    Log.d("ProjectRepository", project.fromEntityToPost().toString())
                projectEndPoint.updateProjectTeam(
                    projectId,
                    ProjectTeamPost(project.fromEntityToPost().participants!!, true)
                )
            },
            onSuccess = { getTeamAndInsertProjectToDb(projectId) },
            onSuccessString = "Project updated successfully",
            onFailureString = "Having problem while updating the project, please check the network connection"
        )
    }

    override suspend fun createProject(project: Project): Result<String, String> {
        return networkCaller(
            call = { projectEndPoint.createProject(project.fromEntityToPost()) },
            onSuccess = { getProjects() },
            onSuccessString = "Project created successfully",
            onFailureString = "Having problem while creating the project, please check the network connection"
        )
    }

    override suspend fun deleteProject(projectId: Int): Result<String, String> {
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