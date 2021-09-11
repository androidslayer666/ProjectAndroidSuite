package com.example.projectandroidsuite.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.database.entities.ProjectEntity
import com.example.database.entities.UserEntity
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.adapter.ProjectPickerAdapter
import com.example.projectandroidsuite.databinding.DialogTeamPickerBinding
import com.example.projectandroidsuite.viewmodel.TaskCreateEditViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectPickerDialog(
    private val list: List<ProjectEntity>,
    private val func: (ProjectEntity) -> Unit
) : DialogFragment(), ProjectPickerAdapter.OnProjectClicker {
//    val viewModel: TaskCreateEditViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { it ->
            val builder = AlertDialog.Builder(it)
            val binding: DialogTeamPickerBinding = DataBindingUtil.inflate(
                LayoutInflater.from(
                    context
                ), R.layout.dialog_team_picker, null, false
            )
            val adapter = ProjectPickerAdapter(this)
            binding.teamRecyclerView.adapter = adapter

            binding.lifecycleOwner = parentFragment
            binding.teamRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            adapter.submitList(list)
            //Log.d("ProjectPickerDialog", viewModel..value.toString())

//            viewModel.projectList.observe(requireActivity(), {
//                Log.d("ProjectPickerDialog", it.toString())
//                adapter.submitList(it)
//            })

            dialog?.setCanceledOnTouchOutside(true)

            builder.setView(binding.root)

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onProjectClick(project: ProjectEntity) {
        func(project)
        // viewModel.chooseProject(project)
        dialog?.dismiss()
    }

}