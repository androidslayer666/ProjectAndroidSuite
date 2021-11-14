package com.example.domain.interactorimpl.milestone

import com.example.domain.interactor.milestone.GetTaskAndMilestonesForProject
import com.example.domain.model.Milestone
import com.example.domain.model.Task
import com.example.domain.repository.MilestoneRepository
import com.example.domain.repository.TaskRepository
import com.example.domain.utils.arrangeMilestonesAndTasks
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class GetTaskAndMilestonesForProjectImpl(
    private val taskRepository: TaskRepository,
    private val milestoneRepository: MilestoneRepository,
): GetTaskAndMilestonesForProject {

    override operator fun invoke(projectId: Int): Flow<Map<Milestone?, List<Task>>> {
        CoroutineScope(Dispatchers.IO).launch {
            taskRepository.populateTasksByProject(projectId)
            milestoneRepository.populateMilestonesByProject(projectId)
        }
        return taskRepository.getTasksByProject(projectId)
            .combine (milestoneRepository.getMilestonesByProjectFlow(projectId))
            { taskList, milestoneList ->
                return@combine arrangeMilestonesAndTasks(milestoneList, taskList)
            }
    }
}