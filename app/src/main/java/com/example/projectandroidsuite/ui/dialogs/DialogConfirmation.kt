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
import com.example.projectandroidsuite.databinding.DialogConfirmationBinding
import com.example.projectandroidsuite.databinding.DialogCreateProjectBinding
import com.example.projectandroidsuite.databinding.DialogCreateTaskBinding
import com.example.projectandroidsuite.databinding.TeamMemberHolderBinding
import com.example.projectandroidsuite.ui.TaskDate
import com.example.projectandroidsuite.ui.loadImageGlide
import com.example.projectandroidsuite.viewmodel.ProjectCreateEditViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class DialogConfirmation(
    private val text: String,
    private val onConfirm: () -> Unit
) : DialogFragment() {
    val viewModel: ProjectCreateEditViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { it ->
            val builder = AlertDialog.Builder(it, R.style.CustomAlertDialog)
            val binding: DialogConfirmationBinding = DataBindingUtil.inflate(
                LayoutInflater.from(
                    context
                ), R.layout.dialog_confirmation, null, false
            )

            binding.textConfirmationDialog.text = text

            binding.lifecycleOwner = this

            builder.setView(binding.root)
                .setPositiveButton("Confirm") { dialog, id ->
                    onConfirm()
                    dialog.dismiss()
                }
                .setNegativeButton(
                    "Cancel"
                ) { dialog, _ ->
                    dialog.dismiss()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}