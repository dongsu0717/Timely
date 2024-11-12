package com.dongsu.timely.presentation.ui.main.group.date

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dongsu.presentation.databinding.ItemCardGroupScheduleBinding
import com.dongsu.timely.domain.model.GroupSchedule


class GroupScheduleListAdapter(
    private val onCheckBoxClick: (GroupSchedule, Boolean) -> Unit
) : ListAdapter<GroupSchedule, GroupScheduleListAdapter.GroupScheduleListViewHolder>(GroupDiffCallback()) {

    class GroupScheduleListViewHolder(private val binding: ItemCardGroupScheduleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(groupSchedule: GroupSchedule, onCheckBoxClick: (GroupSchedule,Boolean) -> Unit){
            with(binding){
                tvGroupName.text = groupSchedule.title
                tvGroupPlace.text = groupSchedule.location
                tvGroupTime.text = groupSchedule.startTime
                checkBoxParticipation.isChecked = groupSchedule.isAlarmEnabled
                checkBoxParticipation.setOnCheckedChangeListener { buttonView, isChecked ->
                    onCheckBoxClick(groupSchedule, isChecked)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupScheduleListViewHolder {
        val binding =
            ItemCardGroupScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupScheduleListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupScheduleListViewHolder, position: Int) {
        val groupSchedule = getItem(position)
        holder.bind(groupSchedule, onCheckBoxClick)
    }

    class GroupDiffCallback : DiffUtil.ItemCallback<GroupSchedule>() {
        override fun areItemsTheSame(oldItem: GroupSchedule, newItem: GroupSchedule): Boolean {
            return oldItem.longitude == newItem.longitude
        }

        override fun areContentsTheSame(oldItem: GroupSchedule, newItem: GroupSchedule): Boolean {
            return oldItem == newItem
        }

    }
}