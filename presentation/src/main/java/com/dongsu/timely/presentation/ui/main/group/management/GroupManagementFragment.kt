package com.dongsu.timely.presentation.ui.main.group.management

import android.util.Log
import com.dongsu.presentation.databinding.FragmentGroupManagementBinding
import com.dongsu.timely.presentation.common.BaseTabFragment

class GroupManagementFragment: BaseTabFragment<FragmentGroupManagementBinding>(FragmentGroupManagementBinding::inflate) {


    override fun initView() {
        Log.e("관리", groupId.toString())
    }

}