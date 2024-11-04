package com.dongsu.timely.presentation.viewmodel.calendar.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

//혼합
class CurrentTimeViewModel : ViewModel() {

    private val _currentDate = MutableLiveData<String>()
    val currentDate: LiveData<String> get() = _currentDate

    private val _currentTimeFlow = MutableStateFlow<String>(formatCurrentTime())
    val currentTimeFlow = _currentTimeFlow.asStateFlow()

    init {
        updateCurrentDate()
        updateCurrentTime()
    }

    private fun updateCurrentDate() {
        _currentDate.value = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Calendar.getInstance().time)
    }

    private fun updateCurrentTime() {
        _currentTimeFlow.value = formatCurrentTime()
    }

    private fun formatCurrentTime() = SimpleDateFormat("HH:mm", Locale.getDefault())
        .format(Calendar.getInstance().time)
}