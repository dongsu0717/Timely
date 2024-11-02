package com.dongsu.timely.presentation.ui.main.calendar.add

import androidx.navigation.fragment.findNavController
import com.dongsu.timely.R
import com.dongsu.timely.databinding.FragmentAddScheduleBinding
import com.dongsu.timely.presentation.common.BaseFragment
import com.dongsu.timely.presentation.ui.main.TimelyActivity

class AddScheduleFragment :
    BaseFragment<FragmentAddScheduleBinding>(FragmentAddScheduleBinding::inflate) {
    override fun initView() {

        with(binding) {
            toolbar.inflateMenu(R.menu.toolbar_menu)
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_save -> {
                        true
                    }

                    R.id.action_back -> {
                        (activity as TimelyActivity).showBottomNavigation()
                        findNavController().popBackStack()
                    }
                    else -> false
                }
            }
        }

    }
}