package com.example.projectandroidsuite.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.SessionManager
import com.example.projectandroidsuite.adapter.ProjectAdapter

import com.example.projectandroidsuite.databinding.FragmentProjectBinding
import com.example.projectandroidsuite.viewmodel.ProjectViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.adapter.UniversalAdapter
import com.example.projectandroidsuite.databinding.FragmentTaskListBinding


@AndroidEntryPoint
class TaskListFragment : Fragment(), UniversalAdapter.OnItemClicker {

    val viewModel: ProjectViewModel by activityViewModels()

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentTaskListBinding.inflate(layoutInflater, container, false)
        val adapter = UniversalAdapter(this)
        binding.taskListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.taskListRecyclerView.adapter = adapter

        if (context != null) setRecyclerDecoration(binding.taskListRecyclerView, requireContext())

        lifecycleScope.launch() {
            viewModel.allTasks.observe(viewLifecycleOwner, {
                adapter.submitList(it)
            })
        }

        return binding.root
    }

    override fun onItemClick(itemId: Int) {
        val action =
            TaskProjectPagerFragmentDirections.actionTaskProjectPagerFragmentToTaskDetailFragment(itemId)
        findNavController().navigate(action)
    }
}