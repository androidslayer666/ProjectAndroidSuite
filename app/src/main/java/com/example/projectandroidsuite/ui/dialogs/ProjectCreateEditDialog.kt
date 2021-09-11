package com.example.projectandroidsuite.ui.dialogs


import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.database.entities.ProjectEntity
import com.example.database.entities.UserEntity
import com.example.projectandroidsuite.Constants
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.databinding.DialogCreateProjectBinding
import com.example.projectandroidsuite.databinding.DialogCreateTaskBinding
import com.example.projectandroidsuite.databinding.TeamMemberHolderBinding
import com.example.projectandroidsuite.ui.DialogOptions
import com.example.projectandroidsuite.ui.TaskDate
import com.example.projectandroidsuite.ui.loadImageGlide
import com.example.projectandroidsuite.viewmodel.ProjectCreateEditViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ProjectCreateEditDialog(
    private val dialogOption: DialogOptions,
    private val project: ProjectEntity? = null
) : DialogFragment() {
    val viewModel: ProjectCreateEditViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { it ->
            Log.d("onCreateDialog", project.toString())
            val builder = AlertDialog.Builder(it, R.style.CustomAlertDialog)
            val binding: DialogCreateProjectBinding = DataBindingUtil.inflate(
                LayoutInflater.from(
                    context
                ), R.layout.dialog_create_project, null, false
            )
            if (project != null) {
                viewModel.setProject(project)
            } else {
                Log.d("setProject", "no project")
                viewModel.setProject(
                    ProjectEntity(
                        id = 0,
                    )
                )
            }
            viewModel.project.observe(requireActivity(), {
                Log.d(
                    "ProjectCreateEditDialog",
                    "Get a project" + viewModel.project.value.toString()
                )
                binding.taskTitle.setText(
                    viewModel.project.value?.title,
                    TextView.BufferType.EDITABLE
                )
                binding.taskDescription.setText(
                    viewModel.project.value?.description,
                    TextView.BufferType.EDITABLE
                )
//                if (!viewModel.task.value?.responsibles.isNullOrEmpty()) {
//                    viewModel.setChosenUsers(viewModel.task.value?.responsibles!!)
//                }
                binding.teamLayout.removeAllViews()

                if (viewModel.project.value?.team != null) {
                    Log.d("TaskCreateEditDialog", viewModel.project.value.toString())
                    for (user in viewModel.project.value!!.team!!) {
                        inflateProjectTeam(
                            LayoutInflater.from(
                                context
                            ), user, binding.teamLayout
                        )
                    }
                }
            })

            binding.lifecycleOwner = this

            binding.buttonChooseManager.setOnClickListener {
                if (viewModel.userList.value != null) {
                    TeamPickerDialog(
                        viewModel.userList.value!!,
                        listOf(viewModel.project.value!!.responsible),
                        { user -> viewModel.setResponsible(user) }, true
                    ).show(parentFragmentManager, "Team")
                }
            }

            binding.buttonChooseTeam.setOnClickListener {
                if (viewModel.userList.value != null)
                    TeamPickerDialog(
                        viewModel.userList.value!!,
                        viewModel.project.value!!.team,
                        { user -> viewModel.addOrRemoveUser(user) }).show(
                        parentFragmentManager,
                        "Team"
                    )
            }

            binding.taskTitle.doOnTextChanged { text, start, before, count ->
                viewModel.setTitle(text.toString())
            }
            binding.taskDescription.doOnTextChanged { text, start, before, count ->
                viewModel.setDescription(text.toString())
            }

            builder.setView(binding.root)
                .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, id ->

                    if (dialogOption == DialogOptions.CREATE) {
                        viewModel.createProject()
                    } else {
                        viewModel.updateProject(viewModel.project.value)
                    }
                })
                .setNegativeButton("Dismiss",
                    DialogInterface.OnClickListener { dialog, _ ->
                        dialog.dismiss()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun inflateProjectTeam(
        inflater: LayoutInflater,
        user: UserEntity,
        teamLayout: GridLayout
    ) {
        val bindingTeam = TeamMemberHolderBinding.inflate(inflater, teamLayout, false)
        bindingTeam.teamMemberText.text = user.displayName?.substringBefore(' ')
        bindingTeam.ifCanBeCancelled = true
        bindingTeam.buttonDismiss.setOnClickListener {
            teamLayout.removeView(bindingTeam.root)
        }
        parentFragment?.let { loadImageGlide(user.avatarSmall, bindingTeam.teamMemberImage, it) }
        teamLayout.addView(bindingTeam.root)
    }
}