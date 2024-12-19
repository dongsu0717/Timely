package com.dongsu.timely.presentation.ui.main.group.date

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dongsu.presentation.databinding.FragmentGroupDateBinding
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.presentation.common.BaseTabFragment
import com.dongsu.timely.presentation.common.CommonUtils.toastShort
import com.dongsu.timely.presentation.common.GET_ERROR
import com.dongsu.timely.presentation.common.GET_LOADING
import com.dongsu.timely.presentation.common.launchRepeatOnLifecycle
import com.dongsu.timely.presentation.common.throttledClickListener
import com.dongsu.timely.presentation.ui.main.group.GroupPageFragmentDirections
import com.dongsu.timely.presentation.viewmodel.group.GroupDateViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@AndroidEntryPoint
class GroupDateFragment : BaseTabFragment<FragmentGroupDateBinding>(FragmentGroupDateBinding::inflate) {

    private val groupDateViewModel: GroupDateViewModel by viewModels()
    private lateinit var groupScheduleListAdapter: GroupScheduleListAdapter

    override fun initView() {
        Timber.e(arguments.toString())
        setLayoutManager()
        setAdapter()
        getGroupScheduleList()
        goAddGroupSchedule()
    }

    private fun setLayoutManager(){
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setAdapter() {
        groupScheduleListAdapter = GroupScheduleListAdapter{ groupScheduleInfo, isChecked ->
            if(isChecked) {
                groupDateViewModel.participationSchedule(groupId, groupScheduleInfo.groupSchedule.scheduleId)
            } else {
                groupDateViewModel.cancelParticipationSchedule(groupId, groupScheduleInfo.groupSchedule.scheduleId)
            }
        }
        binding.recyclerView.adapter= groupScheduleListAdapter
    }

    private fun getGroupScheduleList() {
        launchRepeatOnLifecycle {
            groupDateViewModel.getGroupSchedule(groupId)
            groupDateViewModel.groupScheduleList.collectLatest { result ->
                when (result) {
                    is TimelyResult.Success -> {
                        groupScheduleListAdapter.submitList(result.resultData)
                    }

                    is TimelyResult.Loading -> {
                        toastShort(requireContext(), GET_LOADING)
                    }

                    is TimelyResult.NetworkError -> {
                        toastShort(requireContext(), GET_ERROR)
                    }

                    else -> {}
                }
            }
        }
    }

    private fun goAddGroupSchedule() {
        binding.fabAddGroup.throttledClickListener(lifecycleScope){
            val action = GroupPageFragmentDirections.actionGroupPageFragmentToGroupAddScheduleFragment(groupId)
            findNavController().navigate(action)
        }
    }
}