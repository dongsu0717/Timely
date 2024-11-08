package com.dongsu.timely.presentation.ui.main.calendar.home

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dongsu.timely.R
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.databinding.FragmentCalendarBinding
import com.dongsu.timely.databinding.ScheduleBoxBinding
import com.dongsu.timely.presentation.common.BaseFragment
import com.dongsu.timely.presentation.common.EnumColor
import com.dongsu.timely.presentation.common.collectWhenStarted
import com.dongsu.timely.presentation.common.debouncedClickListener
import com.dongsu.timely.presentation.common.showToast
import com.dongsu.timely.presentation.ui.main.calendar.home.container.DayViewContainer
import com.dongsu.timely.presentation.ui.main.calendar.home.container.MonthViewContainer
import com.dongsu.timely.presentation.viewmodel.calendar.home.CalendarViewModel
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@AndroidEntryPoint
class CalendarFragment : BaseFragment<FragmentCalendarBinding>(FragmentCalendarBinding::inflate) {

    private var selectedDate: LocalDate = LocalDate.now()
    private var selectedDayView: LinearLayout? = null

    private val calendarViewModel: CalendarViewModel by viewModels()

    override fun initView() {
        settingAddScheduleButton()
        settingKizitonwoseCalendar()
    }

    private fun settingAddScheduleButton() {

        binding.fabAddSchedule.clicks()
            .onEach {
                if (areLocationPermissionsGranted()) { //ACCESS_FINE_LOCATION 및 ACCESS_COARSE_LOCATION 권한이 모두 부여되었는지 확인
                    findNavController().navigate(R.id.action_calendarFragment_to_addScheduleFragment)
                } else {
                    requestLocationPermissions() //권한이 부여되지 않은 경우 앱은 requestPermissions()를 사용하여 권한을 요청
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    //    private fun addSchedule(){
//        val title = binding.editTextSchedule.text.toString()
//        if(title.isNotEmpty()){
//            val schedule = ScheduleInfo(
//                title = title,
//                startDate = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
//                lastDate = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
//            )
//            viewModel.insertSchedule(schedule)
//            binding.editTextSchedule.text?.clear()
//            requireActivity().hideSoftInput()
//
//            collectWhenStarted(viewModel.scheduleComplete){
//                if(it) settingKizitonwoseCalendar()
//            }
//        }
//    }
    private fun setCalendarMonth(currentMonth: YearMonth) {
        with(binding.calendarView) {
            val startMonth = currentMonth.minusMonths(5) // Adjust as needed
            val endMonth = currentMonth.plusMonths(5) // Adjust as needed
            val firstDayOfWeek = firstDayOfWeekFromLocale() // Available from the library
            setup(startMonth, endMonth, firstDayOfWeek)
            scrollToMonth(currentMonth)
        }
    }

    private fun settingKizitonwoseCalendar() {
        setCalendarMonth(YearMonth.of(selectedDate.year, selectedDate.month))
        // kizitonwose 라이브러리 사용
        with(binding.calendarView) {
            // 달력 스크롤을 할 때마다 해당 월의 스케줄을 가져옴
//            binding.calendarView.monthScrollListener = {
//                if (scrollPaged) calendarViewModel.findScheduleByMonth(
//                    it.yearMonth.format(
//                        DateTimeFormatter.ofPattern(
//                            "yyyy-MM"
//                        )
//                    )
//                )
//            }
            // 일 레이아웃 바인딩
            dayBinder = object : MonthDayBinder<DayViewContainer> {
                // Called only when a new container is needed.
                override fun create(view: View) = DayViewContainer(view)

                // Called every time we need to reuse a container.
                override fun bind(container: DayViewContainer, data: CalendarDay) {
                    // Set text for the day of the week.
                    container.day = data
                    container.textView.text = data.date.dayOfMonth.toString()
                    container.textView.setTextColor(
                        resources.getColor(
                            if (data.position == DayPosition.MonthDate) {
                                // 평일과 주말 색 셋팅
                                when (data.date.dayOfWeek) {
                                    DayOfWeek.SUNDAY -> R.color.red
                                    DayOfWeek.SATURDAY -> R.color.blue
                                    else -> R.color.white
                                }
                            } else {
                                // 해당 월의 날짜가 아닌 경우에는 회색
                                R.color.gray
                            },
                            null
                        )
                    )
                    // 초기 선택일 표시
                    if (data.date == selectedDate) {
                        selectedDayView(data, container)
                    }
                    // 일정 데이터 불러와서 표시
                    showSchedule(data, container)
                    // 일정 클릭 이벤트
                    settingContainerClickEvent(container, data)
                }

            } // MonthDayBinder 끝

            // 요일, 월 레이아웃 바인딩
            monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
                override fun create(view: View) = MonthViewContainer(view)
                override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                    val daysOfWeek = daysOfWeek(firstDayOfWeek = DayOfWeek.SUNDAY)
                    // Remember that the header is reused so this will be called for each month.
                    // However, the first day of the week will not change so no need to bind
                    // the same view every time it is reused.
                    if (container.titlesContainer.tag == null) {
                        container.titlesContainer.tag = data.yearMonth
                        // 월 셋팅
                        (container.titlesContainer.children.first() as TextView).text =
                            data.yearMonth.month.getDisplayName(
                                TextStyle.SHORT,
                                Locale.getDefault()
                            )
                        // 요일 셋팅
                        (container.titlesContainer.children.last() as LinearLayout)
                            .children.map { it as TextView }
                            .forEachIndexed { index, textView ->
                                val dayOfWeek = daysOfWeek[index]
                                val title =
                                    dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                                textView.text = title
                                when (index) {
                                    0 -> textView.setTextColor(
                                        resources.getColor(
                                            R.color.red,
                                            null
                                        )
                                    )

                                    6 -> textView.setTextColor(
                                        resources.getColor(
                                            R.color.blue,
                                            null
                                        )
                                    )

                                    else -> textView.setTextColor(
                                        resources.getColor(
                                            R.color.white,
                                            null
                                        )
                                    )
                                }
                            }
                    }
                }
            }
        }
    }

    private fun settingContainerClickEvent(
        container: DayViewContainer,
        data: CalendarDay,
    ) {
        container.view.debouncedClickListener(viewLifecycleOwner.lifecycleScope) {
            if (data.date == selectedDate) {

            } else {
                selectedDayView(data, container)
            }
        }
    }

    private fun selectedDayView(data: CalendarDay, container: DayViewContainer) {
        if (selectedDayView != null) selectedDayView?.background = null
        selectedDayView = container.view as LinearLayout
        selectedDate = data.date
        selectedDayView?.background =
            resources.getDrawable(R.drawable.calendar_day_layout_selected, null)
    }

    // 일정 데이터 불러와서 표시
    private fun showSchedule(data: CalendarDay, container: DayViewContainer) {
        collectWhenStarted(calendarViewModel.scheduleList) { result ->
            when (result) {
                is TimelyResult.Success -> {
                    result.resultData.filter { data.date == LocalDate.parse(it.startDate) }
                        .also { filteredList ->
                            if (filteredList.isNotEmpty()) {
                                container.scheduleBox.removeAllViews()
                                filteredList.forEach { schedule ->
                                    val scheduleView =
                                        ScheduleBoxBinding.inflate(
                                            layoutInflater
                                        )
                                    scheduleView.textViewScheduleTitle.text =
                                        schedule.title
                                    scheduleView.textViewScheduleTitle.setBackgroundColor(
                                        resources.getColor(
                                            getScheduleColorResource(schedule.color),
                                            null
                                        )
                                    )
                                    container.scheduleBox.addView(scheduleView.root)
                                }
                            }
                        }
                }

                is TimelyResult.Loading -> {}
                is TimelyResult.Error -> {
                    toastShort("일정을 불러오는데 실패했습니다.")
                }

                else -> {}
            }
        }
    }

    private fun getScheduleColorResource(color: Int) =
        when (color) {
            EnumColor.LAVENDER.order -> R.color.dark_lavender
            EnumColor.SAGE.order -> R.color.dark_sage
            EnumColor.GRAPE.order -> R.color.dark_grape
            EnumColor.FLAMINGO.order -> R.color.dark_flamingo
            EnumColor.BANANA.order -> R.color.dark_banana
            else -> {
                R.color.dark_lavender
            }
        }

    override fun onResume() {
        super.onResume()
        calendarViewModel.findAllSchedule()
    }
}