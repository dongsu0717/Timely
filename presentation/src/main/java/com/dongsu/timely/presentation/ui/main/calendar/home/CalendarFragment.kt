package com.dongsu.timely.presentation.ui.main.calendar.home

//import com.dongsu.timely.presentation.common.toastShort
import android.util.Log
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
import com.dongsu.timely.domain.model.Schedule
import com.dongsu.timely.presentation.common.BaseFragment
import com.dongsu.timely.presentation.common.CommonUtils
import com.dongsu.timely.presentation.common.CommonUtils.toastShort
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

    private var now: LocalDate = LocalDate.now()
    private var selectedDayView: LinearLayout? = null

    private val calendarViewModel: CalendarViewModel by viewModels()

    override fun initView() {
        setAddScheduleButton()
        setCalendar()
    }

    private fun setAddScheduleButton() {
        binding.fabAddSchedule.throttledClickListener(viewLifecycleOwner.lifecycleScope){
            moveAddScheduleFragment()
        }
    }

    private fun setCalendarMonth(currentMonth: YearMonth) {
        with(binding.calendarView) {
            val startMonth = currentMonth.minusMonths(100)
            val endMonth = currentMonth.plusMonths(100)
            val firstDayOfWeek = firstDayOfWeekFromLocale()
            setup(startMonth, endMonth, firstDayOfWeek)
            scrollToMonth(currentMonth)
        }
    }

    private fun setCalendar() {
        setCalendarMonth(YearMonth.of(now.year, now.month))
        setDayLayoutBind() //일 레이아웃 바인딩
        setMonthHeaderLayoutBind() //요일, 월 레이아웃 바인딩
    }

    private fun setDayLayoutBind(){
        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                setDayDate(container, data)
                setDayText(container, data)
                setDayTextColor(container, data)
                // 초기 선택일 표시
                if (data.date == now) {
                    selectedDayView(data, container)
                }
                getSchedule(data, container)
                settingContainerClickEvent(container, data)
            }
        }
    }

    private fun setMonthHeaderLayoutBind() {
        binding.calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                if (container.titlesContainer.tag == null) {
                    setMonth(container,data)
                    setDayOfWeeks(container)
                }
            }
        }
    }

    private fun setDayDate(container: DayViewContainer, data: CalendarDay) {
        container.day = data
    }

    private fun setDayText(container: DayViewContainer, data: CalendarDay) {
        container.textView.text = data.date.dayOfMonth.toString()
    }

    private fun setDayTextColor(container: DayViewContainer, data: CalendarDay) {
        container.textView.setTextColor(
            resources.getColor(
                if (data.position == DayPosition.MonthDate) {
                    when (data.date.dayOfWeek) {
                        DayOfWeek.SUNDAY -> R.color.red
                        DayOfWeek.SATURDAY -> R.color.blue
                        else -> R.color.white
                    }
                } else {
                    R.color.gray //해당 날짜가 아닌경우
                },
                null
            )
        )
    }

    private fun setMonth(container: MonthViewContainer, data: CalendarMonth) {
        container.titlesContainer.tag = data.yearMonth
        (container.titlesContainer.children.first() as TextView).text =
            data.yearMonth.month.getDisplayName(
                TextStyle.SHORT,
                Locale.getDefault()
            )
    }

    private fun setDayOfWeeks(container: MonthViewContainer) {
        (container.titlesContainer.children.last() as LinearLayout)
            .children.map { it as TextView }
            .forEachIndexed { index, textView ->
                val daysOfWeek = daysOfWeek(firstDayOfWeek = DayOfWeek.SUNDAY)
                val dayOfWeek = daysOfWeek[index]
                val title = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                textView.text = title
                when (index) {
                    0 -> textView.setTextColor(
                        resources.getColor(R.color.red, null)
                    )

                    6 -> textView.setTextColor(
                        resources.getColor(R.color.blue, null)
                    )

                    else -> textView.setTextColor(
                        resources.getColor(R.color.white, null)
                    )
                }
            }
    }

    private fun settingContainerClickEvent(
        container: DayViewContainer,
        data: CalendarDay,
    ) {
        container.view.throttledClickListener(viewLifecycleOwner.lifecycleScope) {
            if (data.date == now) {

            } else {
                selectedDayView(data, container)
            }
        }
    }

    private fun selectedDayView(data: CalendarDay, container: DayViewContainer) {
        if (selectedDayView != null) selectedDayView?.background = null
        selectedDayView = container.view as LinearLayout
        now = data.date
        selectedDayView?.background =
            resources.getDrawable(R.drawable.calendar_day_layout_selected, null)
    }

    // 일정 데이터 불러와서 표시
    private fun getSchedule(data: CalendarDay, container: DayViewContainer) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                calendarViewModel.scheduleList.collectLatest { result ->
                    when (result) {

                        is TimelyResult.Loading -> {
                            Log.e("CalendarFragment", "리스트 가져오기 - Loading")
                        }

                        is TimelyResult.Success -> {
                            Log.e("CalendarFragment", "리스트 가져오기 - Success")
                            loadSchedule(data, container, result.resultData)
                        }

                        is TimelyResult.Empty -> {
                            Log.e("CalendarFragment", "리스트 가져오기 - Empty")
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

    private fun loadSchedule(
        data: CalendarDay,
        container: DayViewContainer,
        scheduleList: List<Schedule>
    ) {
        scheduleList.filter { data.date == LocalDate.parse(it.startDate) }
            .also { filteredList ->
                if (filteredList.isNotEmpty()) {
                    container.scheduleBox.removeAllViews()
                    filteredList.forEach { schedule ->
                        setScheduleView(container, schedule)
                    }
                }
            }

    }

    private fun setScheduleView(container: DayViewContainer, schedule: Schedule) {
        val scheduleView = ScheduleBoxBinding.inflate(layoutInflater)
        scheduleView.textViewScheduleTitle.text = schedule.title
        scheduleView.textViewScheduleTitle.setBackgroundColor(
            resources.getColor(CommonUtils.setScheduleBackgroundColor(schedule.color), null)
        )
        container.scheduleBox.addView(scheduleView.root)
    }

    private fun moveAddScheduleFragment() = findNavController().navigate(R.id.action_calendarFragment_to_addScheduleFragment)

    override fun onResume() {
        super.onResume()
        calendarViewModel.loadAllSchedule()
    }
}