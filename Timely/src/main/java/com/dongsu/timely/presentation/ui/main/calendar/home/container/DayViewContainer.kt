package com.dongsu.timely.presentation.ui.main.calendar.home.container

import android.view.View
import com.dongsu.timely.databinding.CalendarDayLayoutBinding
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.view.ViewContainer

class DayViewContainer(view: View) : ViewContainer(view) {
    // With ViewBinding
    val scheduleBox = CalendarDayLayoutBinding.bind(view).insideScheduleLayout
    val textView = CalendarDayLayoutBinding.bind(view).calendarDayText
    lateinit var day: CalendarDay
}