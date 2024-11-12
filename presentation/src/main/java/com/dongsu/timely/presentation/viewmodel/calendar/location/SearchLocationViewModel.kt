package com.dongsu.timely.presentation.viewmodel.calendar.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.PoiItem
import com.dongsu.timely.domain.usecase.SearchLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchLocationViewModel @Inject constructor(
    private val searchLocationUseCase: SearchLocationUseCase,
) : ViewModel() {

    private val _locationList = MutableStateFlow<TimelyResult<MutableList<PoiItem>>>(TimelyResult.Empty)
    val locationsList = _locationList.asStateFlow()

    fun searchLocation(query: String) {
        viewModelScope.launch {
            _locationList.value = searchLocationUseCase(query)
        }
    }
}
