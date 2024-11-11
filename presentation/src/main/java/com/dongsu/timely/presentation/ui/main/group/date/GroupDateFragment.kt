package com.dongsu.timely.presentation.ui.main.group.date

import android.util.Log
import com.dongsu.presentation.databinding.FragmentGroupDateBinding
import com.dongsu.timely.presentation.common.BaseTabFragment

class GroupDateFragment : BaseTabFragment<FragmentGroupDateBinding>(FragmentGroupDateBinding::inflate) {

    override fun initView() {
        Log.e("일정", groupId.toString())

    }

}