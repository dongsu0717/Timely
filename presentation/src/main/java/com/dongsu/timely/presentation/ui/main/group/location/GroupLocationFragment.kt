package com.dongsu.timely.presentation.ui.main.group.location

import android.util.Log
import com.dongsu.presentation.databinding.FragmentGroupLocationBinding
import com.dongsu.timely.presentation.common.BaseTabFragment

class GroupLocationFragment: BaseTabFragment<FragmentGroupLocationBinding>(FragmentGroupLocationBinding::inflate)  {

    override fun initView() {
        Log.e("위치", groupId.toString())
    }
}