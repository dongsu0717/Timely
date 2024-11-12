package com.dongsu.timely.presentation.ui.main.group.list


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dongsu.presentation.databinding.ItemGroupListBinding
import com.dongsu.timely.domain.model.Group

class GroupListAdapter(
    private val onGroupClick: (Group) -> Unit
) : ListAdapter<Group, GroupListAdapter.GroupListViewHolder>(GroupDiffCallback()) {

    class GroupListViewHolder(private val binding: ItemGroupListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(group: Group, onGroupClick: (Group) -> Unit) {
            binding.tvGroupName.text = group.groupName
            binding.root.setOnClickListener {
                onGroupClick(group)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupListViewHolder {
        val binding =
            ItemGroupListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupListViewHolder, position: Int) {
        val group = getItem(position)
        holder.bind(group, onGroupClick)
    }

    class GroupDiffCallback : DiffUtil.ItemCallback<Group>() {
        override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem.groupId == newItem.groupId
        }

        override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem == newItem
        }

    }
}
