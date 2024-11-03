package com.dongsu.timely.presentation.ui.main.calendar.add

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dongsu.timely.R
import com.dongsu.timely.databinding.FragmentAddScheduleBinding
import com.dongsu.timely.domain.model.Schedule
import com.dongsu.timely.presentation.common.BaseFragment
import com.dongsu.timely.presentation.ui.main.TimelyActivity
import com.dongsu.timely.presentation.viewmodel.calendar.add.AddScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddScheduleFragment : BaseFragment<FragmentAddScheduleBinding>(FragmentAddScheduleBinding::inflate) {

    private val viewModel: AddScheduleViewModel by viewModels()

    override fun initView() {
        toolbarSetting()
        toolbarAction()
    }

    private fun toolbarSetting() = binding.toolbar.inflateMenu(R.menu.toolbar_menu)
    private fun toolbarAction(){
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_save -> {
                    saveSchedule()
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
    private fun saveSchedule() {
        val title = binding.etTitle.text.toString()
        val schedule= Schedule(title)
       viewModel.insertSchedule(schedule)
    }
}