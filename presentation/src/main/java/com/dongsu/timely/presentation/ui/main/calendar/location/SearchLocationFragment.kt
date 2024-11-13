package com.dongsu.timely.presentation.ui.main.calendar.location

import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.dongsu.presentation.R
import com.dongsu.presentation.databinding.FragmentSearchLocationBinding
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.PoiItem
import com.dongsu.timely.presentation.common.BaseFragment
import com.dongsu.timely.presentation.viewmodel.calendar.location.SearchLocationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchLocationFragment :
    BaseFragment<FragmentSearchLocationBinding>(FragmentSearchLocationBinding::inflate) {

    private val searchViewModel: SearchLocationViewModel by viewModels()
    private val args: SearchLocationFragmentArgs by navArgs()
    private lateinit var searchAdapter: SearchAdapter

    override fun initView() {
        setLayoutManager()
        setAdapter()
        search()
        getSearchLocationList()
    }

    private fun setLayoutManager() {
        binding.recyclerViewPoi.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setAdapter() {
        searchAdapter = SearchAdapter { setNaviGraph(it) }
        binding.recyclerViewPoi.adapter = searchAdapter
    }
    private fun setNaviGraph(poiItem: PoiItem) {
        if (args.groupId != 0) {
            goGroupAddSchedule(poiItem)
        } else{
            goAddSchedule(poiItem)
        }
    }
    private fun search(){
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchViewModel.searchLocation(it) }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }
    private fun getSearchLocationList() {
        lifecycleScope.launch {
            searchViewModel.locationsList.collectLatest { results ->
                when (results) {
                    is TimelyResult.Success -> {
                        Log.d("SearchLocationFragment", "result: ${results.resultData}")
                        searchAdapter.submitList(results.resultData)
                    }
                    is TimelyResult.Loading -> {
                        // 로딩 중 처리
                    }
                    is TimelyResult.NetworkError -> {
                        // 에러 처리
                    }
                    else -> {}
                }
            }
        }
    }
    private fun goGroupAddSchedule(poiItem: PoiItem) {
        val action = SearchLocationFragmentDirections.actionSearchLocationFragmentToGroupAddScheduleFragment(
            poiItem.noorLat,
            poiItem.noorLon,
            poiItem.name,
            args.groupId
        )
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.searchLocationFragment, inclusive = true)
            .build()
        findNavController().navigate(action,navOptions)
    }
    private fun goAddSchedule(poiItem: PoiItem) {
        val action = SearchLocationFragmentDirections.actionSearchLocationFragmentToAddScheduleFragment(
            poiItem.noorLat,
            poiItem.noorLon,
            poiItem.name
        )
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.searchLocationFragment, inclusive = true)
            .build()
        findNavController().navigate(action,navOptions)
    }
}
