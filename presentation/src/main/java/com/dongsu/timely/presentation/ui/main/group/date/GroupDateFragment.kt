package com.dongsu.timely.presentation.ui.main.group.date

import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dongsu.presentation.databinding.FragmentGroupDateBinding
import com.dongsu.timely.presentation.common.BaseTabFragment
import com.dongsu.timely.presentation.common.debouncedClickListener
import com.dongsu.timely.presentation.ui.main.group.GroupPageFragmentDirections

class GroupDateFragment : BaseTabFragment<FragmentGroupDateBinding>(FragmentGroupDateBinding::inflate) {

    override fun initView() {
        Log.e("일정", groupId.toString())
        goAddGroupSchedule()
    }
    private fun goAddGroupSchedule() {
        binding.fabAddGroup.debouncedClickListener(lifecycleScope){
            val action = GroupPageFragmentDirections.actionGroupPageFragmentToGroupAddScheduleFragment(groupId)
            findNavController().navigate(action)
        }
    }

}