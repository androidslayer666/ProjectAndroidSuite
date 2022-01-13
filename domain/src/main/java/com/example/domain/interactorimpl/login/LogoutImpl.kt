package com.example.domain.interactorimpl.login

import com.example.domain.interactor.login.Logout
import com.example.domain.repository.*

class LogoutImpl(
    private val authRepository: AuthRepository,
    private val messageRepository: MessageRepository,
    private val milestoneRepository: MilestoneRepository,
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository,
    private val fileRepository: FileRepository,
    private val commentRepository: CommentRepository
) : Logout {
    override suspend operator fun invoke() {
        authRepository.logOut()
        messageRepository.clearLocalCache()
        milestoneRepository.clearLocalCache()
        projectRepository.clearLocalCache()
        taskRepository.clearLocalCache()
        fileRepository.clearLocalCache()
        commentRepository.clearLocalCache()

    }
}