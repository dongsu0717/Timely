package com.dongsu.timely.presentation.ui.main.group

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dongsu.timely.common.GROUP_ID
import com.dongsu.timely.presentation.ui.main.group.date.GroupDateFragment
import com.dongsu.timely.presentation.ui.main.group.location.GroupLocationFragment
import com.dongsu.timely.presentation.ui.main.group.management.GroupManagementFragment

class TabAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    private val groupId = (fragment as GroupPageFragment).args.groupId

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            0 -> GroupDateFragment()
            1 -> GroupLocationFragment()
            2 -> GroupManagementFragment()
            else -> throw IllegalArgumentException("니가 왜뜨냐?: $position")
        }
        return fragment.apply { arguments = createArguments() }
    }

    private fun createArguments(): Bundle {
        return Bundle().apply { putInt(GROUP_ID, groupId) }
    }
}