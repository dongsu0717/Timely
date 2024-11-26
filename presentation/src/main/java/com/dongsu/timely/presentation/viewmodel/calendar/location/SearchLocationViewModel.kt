package com.dongsu.timely.presentation.viewmodel.calendar.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.PoiItem
import com.dongsu.timely.domain.repository.TMapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchLocationViewModel @Inject constructor(
    private val tMapRepository: TMapRepository
) : ViewModel() {

    private val _locationList = MutableStateFlow<TimelyResult<List<PoiItem>>>(TimelyResult.Empty)
    val locationsList = _locationList.asStateFlow()

    fun searchLocation(query: String) {
        viewModelScope.launch {
            _locationList.value = TimelyResult.Loading
            _locationList.value = tMapRepository.searchPlaces(query)
        }
    }
}
