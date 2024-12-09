package com.dongsu.timely.presentation.ui.main.group.list

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dongsu.presentation.R
import com.dongsu.presentation.databinding.FragmentGroupListBinding
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.Group
import com.dongsu.timely.presentation.common.BaseFragment
import com.dongsu.timely.presentation.common.CommonUtils.toastShort
import com.dongsu.timely.presentation.common.GET_GROUP_LIST_EMPTY
import com.dongsu.timely.presentation.common.GET_GROUP_LIST_ERROR
import com.dongsu.timely.presentation.common.launchRepeatOnLifecycle
import com.dongsu.timely.presentation.common.throttledClickListener
import com.dongsu.timely.presentation.viewmodel.group.GroupListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class GroupListFragment :
    BaseFragment<FragmentGroupListBinding>(FragmentGroupListBinding::inflate) {

    private val groupListViewModel: GroupListViewModel by viewModels()
    private lateinit var groupListAdapter: GroupListAdapter

    override fun initView() {
        setLayoutManager()
        setAdapter()
        fetchGroupList()
        goToCreateGroup()
    }

    private fun setLayoutManager() {
        binding.rvGroupList.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setAdapter() {
        groupListAdapter = GroupListAdapter { actionGroupClick(it) }
        binding.rvGroupList.adapter = groupListAdapter
    }

    private fun actionGroupClick(group: Group) {
        val action = GroupListFragmentDirections.actionGroupListFragmentToGroupPageFragment(
            group.groupId,
            group.groupName
        )
        findNavController().navigate(action)
    }

    private fun fetchGroupList() {
        launchRepeatOnLifecycle {
            groupListViewModel.fetchMyGroupList()
            groupListViewModel.groupList.collectLatest { result ->
                when (result) {
                    is TimelyResult.Loading -> {
//                    toastShort(requireContext(), GET_LOADING)
                    }

                    is TimelyResult.Success -> {
                        groupListAdapter.submitList(result.resultData)
                    }

                    is TimelyResult.Empty -> {
                        toastShort(requireContext(), GET_GROUP_LIST_EMPTY)
                    }

                    is TimelyResult.NetworkError -> {
                        toastShort(requireContext(), GET_GROUP_LIST_ERROR)
                    }

                    else -> {}
                }
            }
        }
    }

    private fun goToCreateGroup() {
        binding.fabAddGroup.throttledClickListener(lifecycleScope) {
            findNavController().navigate(R.id.action_groupListFragment_to_groupCreateFragment)
        }
    }
}