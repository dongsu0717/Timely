package com.dongsu.timely.presentation.ui.main.calendar.location

import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dongsu.presentation.R
import com.dongsu.timely.common.TimelyResult
import com.dongsu.presentation.databinding.FragmentSearchLocationBinding
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
        searchAdapter = SearchAdapter { selectedPlace ->
            val bundle = bundleOf(
                "latitude" to selectedPlace.noorLat,
                "longitude" to selectedPlace.noorLon,
                "place" to selectedPlace.name
            )
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.searchLocationFragment, true)
                .build()

            findNavController().navigate(
                R.id.action_searchLocationFragment_to_addScheduleFragment,
                bundle,
                navOptions
            )
        }
        binding.recyclerViewPoi.adapter = searchAdapter
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
                    is TimelyResult.Error -> {
                        // 에러 처리
                    }
                    else -> {}
                }
            }
        }
    }
}
