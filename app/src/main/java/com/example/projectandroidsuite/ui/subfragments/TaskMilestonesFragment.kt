package com.example.projectandroidsuite.ui.subfragments

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
import com.example.domain.SessionManager
import com.example.network.dto.UserDto
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.adapter.UniversalAdapter
import com.example.projectandroidsuite.databinding.FragmentProjectDetailBinding
import com.example.projectandroidsuite.databinding.TaskMilestoneFragmentBinding
import com.example.projectandroidsuite.databinding.TeamMemberHolderBinding
import com.example.projectandroidsuite.ui.ProjectDetailFragmentDirections
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
class TaskMilestonesFragment(private val projectId: Int?) : Fragment(), UniversalAdapter.OnItemClicker {

    val viewModel: ProjectDetailViewModel by activityViewModels()

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        val binding = TaskMilestoneFragmentBinding.inflate(layoutInflater, container, false)
        val adapter = UniversalAdapter(this)
        binding.projectDetailRecyclerView.adapter = adapter
        binding.projectDetailRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.lifecycleOwner = this


            lifecycleScope.launch {
                viewModel.getTaskAndMilestones(projectId).collectLatest { listTasksMilestones ->
                    //Log.d("TaskMilestonesFragment", "List and milestones flow " + listTasksMilestones.toString())
                    adapter.submitList(listTasksMilestones)
                }
            }
        return binding.root
    }

    override fun onItemClick(itemId: Int) {
        val action =
            ProjectDetailFragmentDirections.actionProjectDetailFragmentToTaskDetailFragment(itemId)
        findNavController().navigate(action)
    }


}