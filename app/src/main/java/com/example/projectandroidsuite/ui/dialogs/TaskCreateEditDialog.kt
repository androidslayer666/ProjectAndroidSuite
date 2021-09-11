package com.example.projectandroidsuite.ui.dialogs


import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.example.database.entities.TaskEntity
import com.example.database.entities.UserEntity
import com.example.projectandroidsuite.Constants
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.databinding.DialogCreateTaskBinding
import com.example.projectandroidsuite.databinding.TeamMemberHolderBinding
import com.example.projectandroidsuite.ui.TaskDate
import com.example.projectandroidsuite.ui.loadImageGlide
import com.example.projectandroidsuite.viewmodel.TaskCreateEditViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class TaskCreateEditDialog(private val task: TaskEntity? = null) : DialogFragment() {
    val viewModel: TaskCreateEditViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { it ->
            Log.d("onCreateDialog", task.toString())
            val builder = AlertDialog.Builder(it, R.style.CustomAlertDialog)
            val binding: DialogCreateTaskBinding = DataBindingUtil.inflate(
                LayoutInflater.from(
                    context
                ), R.layout.dialog_create_task, null, false
            )
            if (task != null) {
                viewModel.setTask(task)
            } else {
                Log.d("setTask", "no task")
                viewModel.setTask(
                    TaskEntity(
                        id = 0,
                        subtasks = null,
                        responsibles = mutableListOf()
                    )
                )
            }
            viewModel.task.observe(requireActivity(), {
                task?.projectOwner?.let { it1 -> viewModel.chooseProject(it1) }
                Log.d("TaskCreateEditDialog", viewModel.task.value.toString())
                binding.taskTitle.setText(viewModel.task.value?.title, TextView.BufferType.EDITABLE)
                binding.taskDescription.setText(
                    viewModel.task.value?.description,
                    TextView.BufferType.EDITABLE
                )
                if (viewModel.task.value?.created != null)
                    binding.taskStartDateText.text =
                        SimpleDateFormat(Constants.FORMAT_SHOW_DATE).format(viewModel.task.value?.created)
                if (viewModel.task.value?.deadline != null)
                    binding.taskEndDateText.text =
                        SimpleDateFormat(Constants.FORMAT_SHOW_DATE).format(viewModel.task.value?.deadline)
//                if (!viewModel.task.value?.responsibles.isNullOrEmpty()) {
//                    viewModel.setChosenUsers(viewModel.task.value?.responsibles!!)
//                }

                Log.d("TaskCreateEditDialog", task.toString())
                binding.teamLayout.removeAllViews()
                binding.chosenProjectText.text = it.projectOwner?.title

                if (viewModel.task.value?.responsibles != null) {
                    for (user in viewModel.task.value!!.responsibles!!) {
                        inflateProjectTeam(
                            LayoutInflater.from(
                                context
                            ), user, binding.teamLayout
                        )
                    }
                }

            })


            setUpPickersBinding(binding)

            binding.lifecycleOwner = this

            binding.buttonChooseTeam.setOnClickListener {
                if(viewModel.userList.value != null && viewModel.task.value?.responsibles != null)
                TeamPickerDialog(
                    viewModel.userList.value!!,
                    viewModel.task.value!!.responsibles!!,
                    {user ->viewModel.addOrRemoveUser(user)}
                ).show(parentFragmentManager, "Team")
            }

            binding.buttonChooseProject.setOnClickListener {
                viewModel.projectList.value?.let { it1 ->
                    ProjectPickerDialog(it1) { project ->
                        viewModel.chooseProject(
                            project
                        )
                    }.show(parentFragmentManager, "Project")
                }
            }

            viewModel.projectList.observe(requireActivity(), {
                binding.buttonChooseProject.isEnabled = it != null

            })

            binding.taskTitle.doOnTextChanged { text, start, before, count ->
                viewModel.setTitle(text.toString())
            }
            binding.taskDescription.doOnTextChanged { text, start, before, count ->
                viewModel.setDescription(text.toString())
            }

            binding.buttonOk.text = if (task != null) "Update" else "Create"
            binding.buttonOk.setOnClickListener {
                if (viewModel.task.value?.projectOwner != null) {
                    //Log.d("setPositiveButton", viewModel.task.value?.projectOwner.toString())
                    if (task != null) {
                        Log.d("setPositiveButton", "Trying to update the task")
                        viewModel.updateTask(viewModel.task.value)
                        dialog?.dismiss()
                    }
                    //Log.d("setPositiveButton", "Trying to create the task")
                    viewModel.createTask()
                    dialog?.dismiss()
                } else {
                    //Log.d("setPositiveButton", "No project Id")
                    binding.projectWarning.visibility = View.VISIBLE
                }
            }

            binding.buttonDismiss.setOnClickListener {
                dialog?.dismiss()
            }

            builder.setView(binding.root)

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onPause() {
        super.onPause()
    }


    private fun setUpPickersBinding(binding: DialogCreateTaskBinding) {
        val calendar = Calendar.getInstance()
        binding.buttonPickStartDate.text =
            SimpleDateFormat(Constants.FORMAT_SHOW_DATE).format(Date())
        binding.buttonPickEndDate.text = SimpleDateFormat(Constants.FORMAT_SHOW_DATE).format(Date())
        binding.buttonPickStartDate.setOnClickListener {
            dateDialog(TaskDate.STARTDATE, binding, calendar).show()
        }
        binding.buttonPickEndDate.setOnClickListener {
            dateDialog(TaskDate.ENDDATE, binding, calendar).show()
        }
    }

    private fun dateDialog(task: TaskDate, binding: DialogCreateTaskBinding, calendar: Calendar) =
        DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                when (task) {
                    TaskDate.STARTDATE -> {
                        val dateString = "$dayOfMonth/${monthOfYear.plus(1)}/$year"
                        binding.buttonPickStartDate.text = dateString
                        val format = SimpleDateFormat("dd/MM/yy")
                        viewModel.setDate(format.parse(dateString), TaskDate.STARTDATE)
                    }
                    TaskDate.ENDDATE -> {
                        val dateString = "$dayOfMonth/${monthOfYear.plus(1)}/$year"
                        binding.buttonPickStartDate.text = dateString
                        val format = SimpleDateFormat("dd/MM/yy")
                        viewModel.setDate(format.parse(dateString), TaskDate.STARTDATE)
                    }
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
        )

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