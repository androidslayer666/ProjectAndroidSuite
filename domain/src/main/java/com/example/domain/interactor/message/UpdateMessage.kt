package com.example.domain.interactor.message

import com.example.domain.Result
import com.example.domain.model.Message
import com.example.domain.model.Milestone
import com.example.domain.model.Project
import com.example.domain.model.User
import com.example.domain.repository.MessageRepository
import com.example.domain.repository.MilestoneRepository
import com.example.domain.repository.ProjectRepository

class UpdateMessage(
    private val messageRepository:MessageRepository
) {

    suspend operator fun invoke(
        projectId: Int,
        message: Message,
        participants: List<User>
    ): Result<String, String> {
        return messageRepository.updateMessage(projectId, message, participants)
    }

}