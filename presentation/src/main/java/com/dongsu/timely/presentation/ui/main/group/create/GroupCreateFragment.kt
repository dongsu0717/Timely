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
import com.dongsu.timely.presentation.common.EnumColor
import com.dongsu.timely.presentation.common.LOADING
import com.dongsu.timely.presentation.common.OMG
import com.dongsu.timely.presentation.common.SAVE_ERROR
import com.dongsu.timely.presentation.common.SAVE_SUCCESS
import com.dongsu.timely.presentation.common.debouncedClickListener
import com.dongsu.timely.presentation.viewmodel.group.GroupCreateViewModel
import com.google.android.material.imageview.ShapeableImageView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GroupCreateFragment: BaseFragment<FragmentGroupCreateBinding>(FragmentGroupCreateBinding::inflate) {
    private val groupCreateViewModel: GroupCreateViewModel by viewModels()

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
    private fun chooseColor() {
        with(binding){
            setColorClickListener(colorTangerine, R.color.dark_tangerine,EnumColor.LAVENDER.color)
            setColorClickListener(colorPeacock, R.color.dark_peacock,EnumColor.SAGE.color)
            setColorClickListener(colorBlueberry, R.color.dark_blueberry,EnumColor.GRAPE.color)
            setColorClickListener(colorBasil, R.color.dark_basil,EnumColor.FLAMINGO.color)
            setColorClickListener(colorTomato, R.color.dark_tomato,EnumColor.BANANA.color)
        }
    }

    private fun setColorClickListener(
        imageView: ShapeableImageView,
        colorResId: Int,
        saveColor: Int
    ) {
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_text_background)
        imageView.debouncedClickListener(lifecycleScope) {
            drawable?.let {
                val color = ContextCompat.getColor(requireContext(), colorResId)
                it.setTint(color)
                binding.showGroupDesign.background = it
            }
        }
    }
}