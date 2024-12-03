package com.dongsu.timely.presentation.ui.main.calendar.home

//import com.dongsu.timely.presentation.common.toastShort
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.dongsu.presentation.R
import com.dongsu.presentation.databinding.FragmentCalendarBinding
import com.dongsu.presentation.databinding.ScheduleBoxBinding
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.presentation.common.BaseFragment
import com.dongsu.timely.presentation.common.CommonUtils.toastShort
import com.dongsu.timely.presentation.common.EnumColor
import com.dongsu.timely.presentation.common.LOAD_SCHEDULE_EMPTY
import com.dongsu.timely.presentation.common.LOAD_SCHEDULE_ERROR
import com.dongsu.timely.presentation.common.throttledClickListener
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
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
        binding.fabAddSchedule.throttledClickListener(viewLifecycleOwner.lifecycleScope){
            findNavController().navigate(R.id.action_calendarFragment_to_addScheduleFragment)
        }
    }

    private fun setCalendarMonth(currentMonth: YearMonth) {
        with(binding.calendarView) {
            val startMonth = currentMonth.minusMonths(5) // Adjust as needed
            val endMonth = currentMonth.plusMonths(5) // Adjust as needed
            val firstDayOfWeek = firstDayOfWeekFromLocale() // Available from the library
            setup(startMonth, endMonth, firstDayOfWeek)
            scrollToMonth(currentMonth)
        }
    }

    // kizitonwose 라이브러리 사용
    private fun settingKizitonwoseCalendar() {
        setCalendarMonth(YearMonth.of(selectedDate.year, selectedDate.month))
        with(binding.calendarView) {
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

            }

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
        container.view.throttledClickListener(viewLifecycleOwner.lifecycleScope) {
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
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                calendarViewModel.scheduleList.collectLatest { result ->
                    when (result) {
                        is TimelyResult.Loading -> {
//                            toastShort(requireContext(), LOAD_SCHEDULE_LOADING)
                        }
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

                        is TimelyResult.Empty -> {
                            toastShort(requireContext(), LOAD_SCHEDULE_EMPTY)
                        }

                        is TimelyResult.LocalError -> {
                            toastShort(requireContext(), LOAD_SCHEDULE_ERROR)
                        }

                        else -> {}
                    }
                }
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
        calendarViewModel.loadAllSchedule()
    }
}