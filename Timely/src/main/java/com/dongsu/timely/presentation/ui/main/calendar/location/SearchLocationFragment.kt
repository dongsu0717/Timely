package com.dongsu.timely.presentation.ui.main.calendar.location

import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dongsu.timely.R
import com.dongsu.timely.databinding.FragmentSearchLocationBinding
import com.dongsu.timely.presentation.common.BaseFragment
import com.google.android.material.search.SearchBar
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call

@AndroidEntryPoint
class SearchLocationFragment : BaseFragment<FragmentSearchLocationBinding>(FragmentSearchLocationBinding::inflate) {

    private val apiKey = "l7xx7daab04e0de142cf800ce73e929f55e3"

    override fun initView() {
        with(binding) {
            recyclerViewPoi.layoutManager = LinearLayoutManager(requireContext())
            val searchAdapter = SearchAdapter { selectedPlace ->
                val place = selectedPlace.name
                val latitude = selectedPlace.noorLat
                val longitude = selectedPlace.noorLon
                val bundle = bundleOf("latitude" to latitude, "longitude" to longitude, "place" to place)
                findNavController().navigate(R.id.action_searchLocationFragment_to_addScheduleFragment, bundle)
            }
            recyclerViewPoi.adapter = searchAdapter

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        searchPlaces(query, searchAdapter)
                    } else {
                        searchView.requestFocus()
                    }
                    return false
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })
        }
    }

    private fun searchPlaces(place: String, adapter: SearchAdapter) {
        val call = RetrofitInstance.api.searchPlaces(keyword = place, apiKey = apiKey)

        call.enqueue(object : retrofit2.Callback<TmapResponse> {
            override fun onResponse(call: Call<TmapResponse>, response: retrofit2.Response<TmapResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    adapter.submitList(response.body()!!.searchPoiInfo.pois.poi)
                }
            }

            override fun onFailure(call: Call<TmapResponse>, t: Throwable) {
                // 에러 처리
            }
        })
    }
}
