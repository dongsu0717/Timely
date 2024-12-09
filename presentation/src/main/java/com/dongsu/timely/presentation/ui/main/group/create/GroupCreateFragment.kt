package com.dongsu.timely.presentation.ui.main.group.create

import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dongsu.presentation.R
import com.dongsu.presentation.databinding.FragmentGroupCreateBinding
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.presentation.common.BaseFragment
import com.dongsu.timely.presentation.common.CommonUtils.toastShort
import com.dongsu.timely.presentation.common.EnumGroupScheduleColor
import com.dongsu.timely.presentation.common.EnumScheduleColor
import com.dongsu.timely.presentation.common.OMG
import com.dongsu.timely.presentation.common.SAVE_ERROR
import com.dongsu.timely.presentation.common.SAVE_LOADING
import com.dongsu.timely.presentation.common.SAVE_SUCCESS
import com.dongsu.timely.presentation.common.launchRepeatOnLifecycle
import com.dongsu.timely.presentation.common.throttledClickListener
import com.dongsu.timely.presentation.viewmodel.group.GroupCreateViewModel
import com.google.android.material.imageview.ShapeableImageView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class GroupCreateFragment: BaseFragment<FragmentGroupCreateBinding>(FragmentGroupCreateBinding::inflate) {
    private val groupCreateViewModel: GroupCreateViewModel by viewModels()

    private var groupColor = EnumGroupScheduleColor.TANGERINE.color

    override fun initView() {
        setUpToolbar()
        chooseColor()
    }
    private fun setUpToolbar(){
        binding.toolbar.toolbarCommon.apply {
            title = getString(R.string.add_group)
            setNavigationOnClickListener { findNavController().popBackStack() }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_save -> {
                        saveGroup()
                        true
                    }
                    else -> false
                }
            }
        }
    }
    private fun saveGroup(){
        launchRepeatOnLifecycle {
            val groupName = binding.etGroupName.text.toString()
            groupCreateViewModel.createGroup(groupName, groupColor)
            groupCreateViewModel.createGroupState.collectLatest { result ->
                when (result) {
                    is TimelyResult.Loading -> {
                        toastShort(requireContext(), SAVE_LOADING)
                    }

                    is TimelyResult.Success -> {
                        toastShort(requireContext(), SAVE_SUCCESS)
                        findNavController().popBackStack()
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
    private fun chooseColor() {
        with(binding){
            setColorClickListener(colorTangerine, R.color.dark_tangerine,EnumScheduleColor.LAVENDER.color)
            setColorClickListener(colorPeacock, R.color.dark_peacock,EnumScheduleColor.SAGE.color)
            setColorClickListener(colorBlueberry, R.color.dark_blueberry,EnumScheduleColor.GRAPE.color)
            setColorClickListener(colorBasil, R.color.dark_basil,EnumScheduleColor.FLAMINGO.color)
            setColorClickListener(colorTomato, R.color.dark_tomato,EnumScheduleColor.BANANA.color)
        }
    }

    private fun setColorClickListener(
        imageView: ShapeableImageView,
        colorResId: Int,
        saveColor: Int
    ) {
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_text_background)
        imageView.throttledClickListener(lifecycleScope) {
            drawable?.let {
                val color = ContextCompat.getColor(requireContext(), colorResId)
                it.setTint(color)
                binding.showGroupDesign.background = it
                groupColor = saveColor
            }
        }
    }
}