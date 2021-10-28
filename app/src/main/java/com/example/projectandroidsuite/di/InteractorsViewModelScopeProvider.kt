package com.example.projectandroidsuite.di

import com.example.domain.interactor.files.GetAllFiles
import com.example.domain.interactor.milestone.GetAllMilestones
import com.example.domain.interactor.milestone.GetTaskAndMilestonesForProject
import com.example.domain.interactor.project.GetAllProjects
import com.example.domain.interactor.task.GetAllTasks
import com.example.domain.interactor.user.GetAllUsers
import com.example.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@InstallIn(ViewModelComponent::class)
@Module
class InteractorsViewModelScopeProvider {
    @Provides
    @ViewModelScoped
    fun provideGetAllProjects(projectRepository: ProjectRepository): GetAllProjects {
        return GetAllProjects(projectRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetAllUsers(teamRepository: TeamRepository): GetAllUsers {
        return GetAllUsers(teamRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetAllTasks(taskRepository: TaskRepository): GetAllTasks {
        return GetAllTasks(taskRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetTaskAndMilestonesForProject(
        taskRepository: TaskRepository,
        milestoneRepository: MilestoneRepository,
    ): GetTaskAndMilestonesForProject {
        return GetTaskAndMilestonesForProject(taskRepository, milestoneRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetAllMilestones(
        projectRepository: ProjectRepository,
        milestoneRepository: MilestoneRepository,
    ): GetAllMilestones {
        return GetAllMilestones(projectRepository, milestoneRepository)
    }


    @Provides
    @ViewModelScoped
    fun provideGetAllFiles(
        fileRepository: FileRepository,
        projectRepository: ProjectRepository
    ): GetAllFiles {
        return GetAllFiles(fileRepository, projectRepository)
    }


}