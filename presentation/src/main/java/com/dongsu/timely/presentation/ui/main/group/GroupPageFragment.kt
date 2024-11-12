package com.dongsu.timely.presentation.ui.main.group

import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.dongsu.presentation.databinding.FragmentGroupPageBinding
import com.dongsu.timely.presentation.common.BaseFragment
import com.dongsu.timely.presentation.common.GROUP_LOCATION
import com.dongsu.timely.presentation.common.GROUP_MANAGEMENT
import com.dongsu.timely.presentation.common.GROUP_SCHEDULE
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupPageFragment : BaseFragment<FragmentGroupPageBinding>(FragmentGroupPageBinding::inflate) {

    private lateinit var toolbar: Toolbar
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var tabAdapter: TabAdapter
    val args: GroupPageFragmentArgs by navArgs()

    override fun initView() {
        checkArgs()
        setupToolbar()
        setupTabLayoutAndViewPager()
    }
    private fun checkArgs() {
        Log.e("GroupPageFragment", "checkArgs: ${args}")
    }

    private fun setupToolbar() {
        toolbar = binding.toolbar.apply {
            setTitle(args.groupName)
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }
    private fun setupTabLayoutAndViewPager() {
        viewPager = binding.viewPager
        tabLayout = binding.tabLayout
        setupAdapter()
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getTabTitle(position)
        }.attach()
    }
    private fun setupAdapter() {
        tabAdapter = TabAdapter(this)
        viewPager.adapter = tabAdapter
    }
    private fun getTabTitle(position: Int): String {
        return when (position) {
            0 -> GROUP_SCHEDULE
            1 -> GROUP_LOCATION
            2 -> GROUP_MANAGEMENT
            else -> ""
        }
    }
}