package com.dongsu.timely.presentation.viewmodel.calendar.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dongsu.timely.domain.model.Schedule
import com.dongsu.timely.domain.usecase.schedule.AddScheduleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddScheduleViewModel @Inject constructor(
    private val addScheduleUseCase: AddScheduleUseCase
) : ViewModel() {

    fun insertSchedule(newSchedule: Schedule) {
        viewModelScope.launch {
            addScheduleUseCase(newSchedule)
        }
    }

}

/*
class AddScheduleViewModel(private val scheduleRepository: ScheduleRepository) : ViewModel()  {

    fun insertSchedule(newSchedule: ScheduleInfo) {
        viewModelScope.launch {
            withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                scheduleRepository.insertSchedule(newSchedule).collectLatest {
                    when (it) {
                        is TimelyResult.Success -> {
                            viewModelScope.launch {
                                Log.e("스케줄 저장(성공)", it.toString())
                            }
                        }

                        is TimelyResult.Loading -> {
                            viewModelScope.launch {
                               Log.e("스케줄 저장(로딩)", it.toString())
                            }
                        }

                        is TimelyResult.RoomDBError -> {
                            Log.e("스케줄 저장(DB에러)", it.exception.toString())
                        }

                        else -> {
                            Log.e("스케줄 저장(너가 왜뜨니?)", it.toString())
                        }
                    }
                }
            }
        }
    }
}
 */