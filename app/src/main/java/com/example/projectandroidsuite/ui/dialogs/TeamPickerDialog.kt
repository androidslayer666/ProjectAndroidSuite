package com.example.projectandroidsuite.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.database.entities.UserEntity
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.adapter.TeamPickerAdapter
import com.example.projectandroidsuite.databinding.DialogTeamPickerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeamPickerDialog(
    private val listUsers: List<UserEntity>,
    private val listChosenUsers: List<UserEntity?>?,
    private val doOnCLick: (user: UserEntity) -> Unit,
    private val ifSetResponsible: Boolean = false
) : DialogFragment(), TeamPickerAdapter.OnUserClicker {
    //val viewModel: TaskCreateEditViewModel by activityViewModels()

    val adapter = TeamPickerAdapter(this, ifSetResponsible) { user -> setOthersNotChosen(user) }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { it ->
            val builder = AlertDialog.Builder(it)
            val binding: DialogTeamPickerBinding = DataBindingUtil.inflate(
                LayoutInflater.from(
                    context
                ), R.layout.dialog_team_picker, null, false
            )
            //val adapter = TeamPickerAdapter(this,true)
            binding.teamRecyclerView.adapter = adapter

            binding.lifecycleOwner = parentFragment
            binding.teamRecyclerView.layoutManager = LinearLayoutManager(requireContext())

            for (user in listUsers) {
                if (listChosenUsers != null) {
                    for (userChosen in listChosenUsers) {
                        if (user.id == userChosen?.id) {
                            user.chosen = true
                        }
                    }
                }
            }
            adapter.submitList(listUsers.toMutableList())

            dialog?.setCanceledOnTouchOutside(true)

            builder.setView(binding.root)
                .setPositiveButton("Ok") { dialog, id ->
                    clearChosenUsers()
                }
                .setNegativeButton(
                    "Dismiss"
                ) { dialog, id ->
                    clearChosenUsers()
                    dialog.dismiss()
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


    override fun onUserClick(user: UserEntity) {
        doOnCLick(user)
    }

    private fun clearChosenUsers() {
        for (user in listUsers) {
            user.chosen = null
        }
    }

    private fun setOthersNotChosen(user: UserEntity) {
        if (ifSetResponsible) {
            Log.d("TeamPickerAdapter", "Start clearing chosen users" + user.toString())
            val newList = mutableListOf<UserEntity>()
            for (userInList in listUsers) {
                if (userInList.id != user.id) {
                    userInList.chosen = null
                    newList.add(userInList)
                } else {
                    userInList.chosen = true
                    newList.add(userInList)
                }
            }
            Log.d("TeamPickerAdapter", "submit to adapter " + listUsers.toString())
            adapter.submitList(newList)
        }
    }
}