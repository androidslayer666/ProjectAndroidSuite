package com.example.domain.interactor.milestone

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.domain.model.Milestone
import com.example.domain.model.Task
import com.example.domain.repository.MilestoneRepository
import com.example.domain.repository.TaskRepository
import com.example.domain.utils.arrangeMilestonesAndTasks
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class GetTaskAndMilestonesForProject(
    private val taskRepository: TaskRepository,
    private val milestoneRepository: MilestoneRepository,
) {

    operator fun invoke(projectId: Int): LiveData<Map<Milestone?, List<Task>>> {
        CoroutineScope(Dispatchers.IO).launch {
            taskRepository.populateTasksByProject(projectId)
            milestoneRepository.populateMilestonesByProject(projectId)
        }
        return taskRepository.getTasksByProject(projectId)
            .combine (milestoneRepository.getMilestonesByProjectFlow(projectId))
            { taskList, milestoneList ->
                return@combine arrangeMilestonesAndTasks(milestoneList, taskList)
            }.asLiveData()
    }
}