package com.dongsu.timely.presentation.ui.main.group.date

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dongsu.presentation.R
import com.dongsu.presentation.databinding.ItemCardGroupScheduleBinding
import com.dongsu.timely.domain.model.GroupScheduleInfo
import com.dongsu.timely.presentation.common.formatDateTime


class GroupScheduleListAdapter(
    private val onCheckBoxClick: (GroupScheduleInfo, Boolean) -> Unit
) : ListAdapter<GroupScheduleInfo, GroupScheduleListAdapter.GroupScheduleListViewHolder>(
    GroupDiffCallback()
) {

    inner class GroupScheduleListViewHolder(private val binding: ItemCardGroupScheduleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            groupSchedule: GroupScheduleInfo,
            onCheckBoxClick: (GroupScheduleInfo, Boolean) -> Unit
        ) {
            setCardView(binding, groupSchedule)
            binding.checkBoxParticipation.setOnCheckedChangeListener { buttonView, isChecked ->
                onCheckBoxClick(groupSchedule, isChecked)
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

    class GroupDiffCallback : DiffUtil.ItemCallback<GroupScheduleInfo>() {
        override fun areItemsTheSame(
            oldItem: GroupScheduleInfo,
            newItem: GroupScheduleInfo
        ): Boolean {
            return oldItem.scheduleId == newItem.scheduleId
        }

        override fun areContentsTheSame(
            oldItem: GroupScheduleInfo,
            newItem: GroupScheduleInfo
        ): Boolean {
            return oldItem == newItem
        }

    }

    private fun setCardView(
        binding: ItemCardGroupScheduleBinding,
        groupSchedule: GroupScheduleInfo
    ) {
        with(binding) {
            tvGroupName.text = groupSchedule.title
            tvGroupPlace.text = groupSchedule.location
            tvGroupTime.text = formatDateTime(groupSchedule.startTime)
            if(!groupSchedule.isAlarmEnabled) sivNotification.setImageResource(R.drawable.baseline_notifications_off_24)
        }
    }
}