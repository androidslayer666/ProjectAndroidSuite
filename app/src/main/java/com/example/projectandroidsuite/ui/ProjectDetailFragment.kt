package com.example.projectandroidsuite.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.database.entities.UserEntity
import com.example.domain.SessionManager
import com.example.network.dto.UserDto
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.adapter.TaskMilestonePagerAdapter
import com.example.projectandroidsuite.adapter.UniversalAdapter
import com.example.projectandroidsuite.databinding.FragmentProjectDetailBinding
import com.example.projectandroidsuite.databinding.TeamMemberHolderBinding
import com.example.projectandroidsuite.ui.dialogs.DialogConfirmation
import com.example.projectandroidsuite.ui.dialogs.ProjectCreateEditDialog
import com.example.projectandroidsuite.ui.dialogs.TaskCreateEditDialog
import com.example.projectandroidsuite.viewmodel.ProjectDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class ProjectDetailFragment : Fragment(), UniversalAdapter.OnItemClicker {

    val viewModel: ProjectDetailViewModel by activityViewModels()

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Set current project to view model for db queries and network updates
        viewModel.getCurrentProject(arguments?.get("projectId") as Int)
        val binding = FragmentProjectDetailBinding.inflate(layoutInflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Set pager for list tasks and milestones and for list messages
        binding.pagerTaskMilestones.adapter =
            TaskMilestonePagerAdapter(this, arguments?.get("projectId") as Int)

        viewModel.currentProject.observe(viewLifecycleOwner, {
            Log.d("projectManagerHolder", it.toString())
            binding.projectManagerHolder.teamMemberText.text = it.responsible?.displayName
        })

        // Set edit and delete buttons
        setExpandableOptions(
            binding.layoutCustomProjectDetail,
            binding.expendableActions.cardEdit,
            binding.expendableActions.cardDelete,
            binding.expendableActions.buttonExpandOptions
        )
        setEditDeleteClicker(binding)

        // inflating team for the project
        // todo think about recycler instead
        viewModel.team.observe(viewLifecycleOwner, {
            for (user in it) {
                inflateProjectTeam(inflater, user, binding.teamLayout)
            }
        })

        return binding.root
    }

    //
    override fun onItemClick(itemId: Int) {
        val action =
            ProjectDetailFragmentDirections.actionProjectDetailFragmentToTaskDetailFragment(itemId)
        findNavController().navigate(action)
    }

    private fun inflateProjectTeam(
        inflater: LayoutInflater,
        user: UserEntity,
        teamLayout: GridLayout
    ) {
        val bindingTeam = TeamMemberHolderBinding.inflate(inflater, teamLayout, false)
        bindingTeam.teamMemberText.text = user.displayName?.substringBefore(' ')
        bindingTeam.ifCanBeCancelled = false
        loadImageGlide(user.avatarSmall, bindingTeam.teamMemberImage, this@ProjectDetailFragment)
        teamLayout.addView(bindingTeam.root)
    }

    private fun setEditDeleteClicker(binding: FragmentProjectDetailBinding) {

        // on edit button click do
        binding.expendableActions.buttonEdit.setOnClickListener {
            ProjectCreateEditDialog(DialogOptions.UPDATE, viewModel.currentProject.value).show(
                parentFragmentManager,
                "editProject"
            )
        }
        // on delete button click do
        binding.expendableActions.buttonDelete.setOnClickListener {
            DialogConfirmation(
                "Are you sure you want to delete the project?",
            ) { viewModel.deleteProject() }.show(
                parentFragmentManager,
                "deleteProject"
            )
        }
    }

}