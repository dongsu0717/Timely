package com.dongsu.timely.presentation.ui.main.group.create

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dongsu.presentation.R
import com.dongsu.presentation.databinding.FragmentGroupCreateBinding
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.presentation.common.BaseFragment
import com.dongsu.timely.presentation.common.CommonUtils.toastShort
import com.dongsu.timely.presentation.common.LOADING
import com.dongsu.timely.presentation.common.OMG
import com.dongsu.timely.presentation.common.SAVE_ERROR
import com.dongsu.timely.presentation.common.SAVE_SUCCESS
import com.dongsu.timely.presentation.viewmodel.group.GroupCreateViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GroupCreateFragment: BaseFragment<FragmentGroupCreateBinding>(FragmentGroupCreateBinding::inflate) {
    private val groupCreateViewModel: GroupCreateViewModel by viewModels()

    override fun initView() {
        toolbarSetting()
        toolbarAction()
    }
    private fun toolbarSetting() = binding.toolbar.inflateMenu(R.menu.toolbar_menu)
    private fun toolbarAction(){
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_save -> {
                    saveGroup()
                    true
                }
                R.id.action_back -> {
                    findNavController().popBackStack()
                }
                else -> false
            }
        }
    }
    private fun saveGroup(){
        val groupName = binding.etGroupName.text.toString()
        lifecycleScope.launch {
            val result = groupCreateViewModel.createGroup(groupName)
            when (result) {
                is TimelyResult.Success -> {
                    toastShort(requireContext(), SAVE_SUCCESS)
                    findNavController().popBackStack()
                }
                is TimelyResult.Loading -> {
                    toastShort(requireContext(), LOADING)
                }
                is TimelyResult.NetworkError -> {
                    toastShort(requireContext(), SAVE_ERROR)
                }
                else -> {
                    toastShort(requireContext(), OMG)
                }
            }
        }
    }

}