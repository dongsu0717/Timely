package com.dongsu.timely.presentation.ui.main.group.date

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dongsu.presentation.R
import com.dongsu.presentation.databinding.ItemCardGroupScheduleBinding
import com.dongsu.timely.domain.model.TotalGroupScheduleInfo
import com.dongsu.timely.presentation.common.formatDateTime


class GroupScheduleListAdapter(
    private val onCheckBoxClick: (TotalGroupScheduleInfo, Boolean) -> Unit
) : ListAdapter<TotalGroupScheduleInfo, GroupScheduleListAdapter.GroupScheduleListViewHolder>(
    GroupDiffCallback()
) {

    inner class GroupScheduleListViewHolder(private val binding: ItemCardGroupScheduleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            groupSchedule: TotalGroupScheduleInfo,
            onCheckBoxClick: (TotalGroupScheduleInfo, Boolean) -> Unit
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

    class GroupDiffCallback : DiffUtil.ItemCallback<TotalGroupScheduleInfo>() {
        override fun areItemsTheSame(
            oldItem: TotalGroupScheduleInfo,
            newItem: TotalGroupScheduleInfo
        ): Boolean {
            return oldItem.groupSchedule.scheduleId == newItem.groupSchedule.scheduleId
        }

        override fun areContentsTheSame(
            oldItem: TotalGroupScheduleInfo,
            newItem: TotalGroupScheduleInfo
        ): Boolean {
            return oldItem == newItem
        }

    }

    private fun setCardView(
        binding: ItemCardGroupScheduleBinding,
        groupSchedule: TotalGroupScheduleInfo
    ) {
        with(binding) {
            tvGroupName.text = groupSchedule.groupSchedule.title
            tvGroupPlace.text = groupSchedule.groupSchedule.location
            tvGroupTime.text = formatDateTime(groupSchedule.groupSchedule.startTime)
            if(!groupSchedule.groupSchedule.isAlarmEnabled) sivNotification.setImageResource(R.drawable.baseline_notifications_off_24)
            if(groupSchedule.isParticipating) checkBoxParticipation.isChecked = true
        }
    }
}