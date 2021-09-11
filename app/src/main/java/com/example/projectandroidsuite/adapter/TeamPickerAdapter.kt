package com.example.projectandroidsuite.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.database.entities.UserEntity
import com.example.projectandroidsuite.databinding.TeamItemBinding

class TeamPickerAdapter(
    private val fragment: OnUserClicker,
    private val ifSetResponsible: Boolean = false,
    private val onClickOthersInvisible: (user: UserEntity) -> Unit = {(_) -> Unit}
) :
    ListAdapter<UserEntity, TeamPickerAdapter.UserViewHolder>(ProjectListAdapterCallBack()) {

    interface OnUserClicker {
        fun onUserClick(user: UserEntity)
    }

    class UserViewHolder(
        private val binding: TeamItemBinding,
        private val fragment: OnUserClicker,
        private val onClickOthersInvisible: (user: UserEntity) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserEntity) {
            binding.user = user
            if(user.chosen == true) {
                //Log.d("TeamPickerAdapter", "set visible")
                binding.imageChosen.visibility = View.VISIBLE
            }else {
                //Log.d("TeamPickerAdapter", "set invisible")
                binding.imageChosen.visibility = View.GONE
            }

            binding.root.setOnClickListener {
                fragment.onUserClick(user)
                if (binding.imageChosen.visibility == View.VISIBLE) binding.imageChosen.visibility =
                    View.GONE else binding.imageChosen.visibility = View.VISIBLE
                //Log.d("TeamPickerAdapter", "Start clearing chosen users")
                onClickOthersInvisible(user)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TeamItemBinding.inflate(layoutInflater, parent, false)
        return UserViewHolder(binding, fragment, onClickOthersInvisible)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ProjectListAdapterCallBack : DiffUtil.ItemCallback<UserEntity>() {
        override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
            //Log.d("ProjectListAdallBack", newItem.id + "  " + oldItem.id)
            return false
//            oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
            //Log.d("ProjectListAdallBack", newItem.chosen.toString() + "  " + oldItem.chosen.toString())
            return false
//            oldItem.chosen == newItem.chosen
        }
    }

    override fun submitList(list: MutableList<UserEntity>?) {
        //Log.d("TeamPickerAdapter", "list submitted" + list.toString())
        super.submitList(list)
    }
}