package com.example.projectandroidsuite.di

import com.example.domain.interactor.file.GetAllFiles
import com.example.domain.interactor.milestone.GetAllMilestones
import com.example.domain.interactor.milestone.GetTaskAndMilestonesForProject
import com.example.domain.interactor.project.GetAllProjects
import com.example.domain.interactor.task.GetAllTasks
import com.example.domain.interactor.user.GetAllUsers
import com.example.domain.interactorimpl.file.GetAllFilesImpl
import com.example.domain.interactorimpl.milestone.GetAllMilestonesImpl
import com.example.domain.interactorimpl.milestone.GetTaskAndMilestonesForProjectImpl
import com.example.domain.interactorimpl.project.GetAllProjectsImpl
import com.example.domain.interactorimpl.task.GetAllTasksImpl
import com.example.domain.interactorimpl.user.GetAllUsersImpl
import com.example.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
class InteractorsViewModelScopeProvider {
    @Provides
    @ViewModelScoped
    fun provideGetAllProjects(projectRepository: ProjectRepository): GetAllProjects {
        return GetAllProjectsImpl(projectRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetAllUsers(teamRepository: TeamRepository): GetAllUsers {
        return GetAllUsersImpl(teamRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetAllTasks(taskRepository: TaskRepository): GetAllTasks {
        return GetAllTasksImpl(taskRepository)
    }


    @Provides
    @ViewModelScoped
    fun provideGetTaskAndMilestonesForProject(
        taskRepository: TaskRepository,
        milestoneRepository: MilestoneRepository,
    ): GetTaskAndMilestonesForProject {
        return GetTaskAndMilestonesForProjectImpl(taskRepository, milestoneRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetAllMilestones(
        projectRepository: ProjectRepository,
        milestoneRepository: MilestoneRepository,
    ): GetAllMilestones {
        return GetAllMilestonesImpl(projectRepository, milestoneRepository)
    }


    @Provides
    @ViewModelScoped
    fun provideGetAllFiles(
        fileRepository: FileRepository,
        projectRepository: ProjectRepository
    ): GetAllFiles {
        return GetAllFilesImpl(fileRepository, projectRepository)
    }


}