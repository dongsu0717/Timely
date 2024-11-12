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
import com.dongsu.timely.presentation.common.GET_ERROR
import com.dongsu.timely.presentation.common.GET_LOADING
import com.dongsu.timely.presentation.common.debouncedClickListener
import com.dongsu.timely.presentation.viewmodel.group.GroupListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GroupListFragment: BaseFragment<FragmentGroupListBinding>(FragmentGroupListBinding::inflate)  {

    private val groupListViewModel: GroupListViewModel by viewModels()
    private lateinit var groupListAdapter: GroupListAdapter

    override fun initView() {
        setLayoutManager()
        setAdapter()
        getGroupList()
        goToCreateGroup()
    }
    private fun setLayoutManager(){
        binding.rvGroupList.layoutManager = LinearLayoutManager(requireContext())
    }
    private fun setAdapter(){
        groupListAdapter = GroupListAdapter{ actionGroupClick(it) }
        binding.rvGroupList.adapter = groupListAdapter
    }
    private fun actionGroupClick(group: Group){
        val action = GroupListFragmentDirections.actionGroupListFragmentToGroupPageFragment(group.groupId,group.groupName)
        findNavController().navigate(action)
    }
    private fun getGroupList(){
        lifecycleScope.launch {
            groupListViewModel.getGroupList()
            setGroupList()
        }
    }
    private suspend fun setGroupList(){
        groupListViewModel.groupList.collectLatest{ result ->
            when(result){
                is TimelyResult.Success -> {
                    groupListAdapter.submitList(result.resultData)
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
    private fun goToCreateGroup(){
        binding.fabAddGroup.debouncedClickListener(lifecycleScope){
            findNavController().navigate(R.id.action_groupListFragment_to_groupCreateFragment)
        }
    }
}