package com.example.projectandroidsuite.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.database.entities.CommentEntity
import com.example.database.entities.UserEntity
import com.example.domain.SessionManager
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.adapter.CommentAdapter
import com.example.projectandroidsuite.adapter.UniversalAdapter
import com.example.projectandroidsuite.databinding.FragmentTaskDetailBinding
import com.example.projectandroidsuite.databinding.TeamMemberHolderBinding
import com.example.projectandroidsuite.ui.dialogs.DialogConfirmation
import com.example.projectandroidsuite.ui.dialogs.TaskCreateEditDialog
import com.example.projectandroidsuite.viewmodel.TaskDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class TaskDetailFragment : Fragment(), UniversalAdapter.OnItemClicker,
    CommentAdapter.OnCommentReplyClicker {

    private val viewModel: TaskDetailViewModel by viewModels()

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        val binding = FragmentTaskDetailBinding.inflate(layoutInflater, container, false)
        val fileAdapter = UniversalAdapter(this)
        val commentAdapter = CommentAdapter(this)
        binding.taskDetailFileRecyclerView.adapter = fileAdapter
        binding.taskDetailFileRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.taskDetailCommentRecyclerView.adapter = commentAdapter
        binding.taskDetailCommentRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        setExpandableOptions(
            binding.layoutCustomProjectDetail,
            binding.expendableActions.cardEdit,
            binding.expendableActions.cardDelete,
            binding.expendableActions.buttonExpandOptions
        )

        setClickListeners(binding)

//
        setRecyclerDecoration(binding.taskDetailFileRecyclerView, container?.context)

        viewModel.filesForTask.observe(viewLifecycleOwner, {
            //Log.d("TaskDetailFragment", it.toString())
            fileAdapter.submitList(it)
        })

        viewModel.commentsForTask.observe(viewLifecycleOwner, {
            //Log.d("TaskDetailFragment", "Comments " + it.toString())
            commentAdapter.submitList(it)
        })

        viewModel.team.observe(viewLifecycleOwner, {
            //Log.d("TaskDetailFragment", it.toString())
            for (user in it) {
                inflateProjectTeam(inflater, user, binding.gridTaskDetailTeamLayout)
            }
        })

        return binding.root
    }

    private fun setRecyclerDecoration(recycler: RecyclerView, context: Context?) {
        val dividerItemDecoration = DividerItemDecoration(
            context,
            DividerItemDecoration.VERTICAL
        )
        recycler.addItemDecoration(dividerItemDecoration)
    }

    private fun inflateProjectTeam(
        inflater: LayoutInflater,
        user: UserEntity,
        teamLayout: GridLayout
    ) {
        teamLayout.parent

        val bindingTeam = TeamMemberHolderBinding.inflate(inflater, teamLayout, false)
        bindingTeam.teamMemberText.text = user.displayName?.substringBefore(' ')
        bindingTeam.buttonDismiss.setOnClickListener {
            viewModel.dismissTeamMember(user)
            teamLayout.removeView(bindingTeam.root)
        }
        loadImageGlide(user.avatarSmall, bindingTeam.teamMemberImage, this@TaskDetailFragment)
        teamLayout.addView(bindingTeam.root)
    }

    override fun onCommentReplyClick(comment: CommentEntity) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(itemId: Int) {

    }

    private fun setClickListeners(binding: FragmentTaskDetailBinding) {
        binding.expendableActions.buttonDelete.setOnClickListener {
            DialogConfirmation(
                "Are you sure you want to delete the task?",
            ) { viewModel.currentTask.value?.let { task ->
                run {
                    viewModel.deleteTask(task)
                    val fragmentManager = requireActivity().supportFragmentManager
                    fragmentManager.popBackStack()
                }
            } }.show(
                parentFragmentManager,
                "createTask"
            )
        }

        binding.expendableActions.buttonEdit
            .setOnClickListener {
                TaskCreateEditDialog(viewModel.currentTask.value).show(
                    parentFragmentManager,
                    "createTask"
                )
            }
    }

}