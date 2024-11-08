package com.dongsu.timely.presentation.ui.main.calendar.add

import android.Manifest
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dongsu.timely.R
import com.dongsu.timely.databinding.FragmentAddScheduleBinding
import com.dongsu.timely.domain.model.Schedule
import com.dongsu.timely.presentation.common.BaseFragment
import com.dongsu.timely.presentation.common.EnumAlarmTime
import com.dongsu.timely.presentation.common.EnumColor
import com.dongsu.timely.presentation.common.EnumRepeat
import com.dongsu.timely.presentation.common.debouncedClickListener
import com.dongsu.timely.presentation.common.formatDate
import com.dongsu.timely.presentation.common.formatTimeToString
import com.dongsu.timely.presentation.viewmodel.calendar.add.AddScheduleViewModel
import com.dongsu.timely.presentation.viewmodel.calendar.add.CurrentTimeViewModel
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddScheduleFragment : BaseFragment<FragmentAddScheduleBinding>(FragmentAddScheduleBinding::inflate) {

    private val addScheduleViewModel: AddScheduleViewModel by viewModels()
    private val currentViewModel: CurrentTimeViewModel by viewModels()
//    private var scheduleId: Int = NO_SCHEDULE_ID

    private var repeatDays = EnumRepeat.NO.repeat
    private var appointmentAlarmTime = EnumAlarmTime.BEFORE_1_HOUR.time
    private var color = EnumColor.LAVENDER.color
    private var latitude = 0.0
    private var longitude = 0.0

    override fun initView() {
        setupArgument()
        toolbarSetting()
        toolbarAction()
        getCurrentDataAndTime()
        setupSpinnerAdapter()
        choiceSchedule()
    }
    private fun setupArgument(){
        arguments?.let {
            latitude = it.getString("latitude")?.toDoubleOrNull() ?: 0.0
            longitude = it.getString("longitude")?.toDoubleOrNull() ?: 0.0
            binding.tvAppointmentPlace.text = it.getString("place") ?: "약속 장소"
        }
    }
    private fun toolbarSetting() = binding.toolbar.inflateMenu(R.menu.toolbar_menu)
    private fun toolbarAction(){
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_save -> {
                    saveSchedule()
                    true
                }
                R.id.action_back -> {
                    findNavController().popBackStack()
                }
                else -> false
            }
        }
    }
    private fun saveSchedule() {
        with(binding) {
            val title = etTitle.text.toString()
            val startDate = tvStartDate.text.toString()
            val lastDate = tvLastDate.text.toString()
            val startTime = tvStartTime.text.toString()
            val endTime = tvEndTime.text.toString()
            val appointmentPlace = tvAppointmentPlace.text.toString()
            val appointmentAlarm = switchAppointmentAlarm.isChecked

            val schedule = Schedule(
                title, startDate, lastDate, startTime, endTime,
                repeatDays, appointmentPlace, latitude, longitude,
                appointmentAlarm, appointmentAlarmTime, color
            )
            addScheduleViewModel.insertSchedule(schedule)
//            if (scheduleId != NO_SCHEDULE_ID) {
//                viewModel.updateSchedule(schedule)
//            } else {
//                viewModel.insertSchedule(schedule)
//            }
        }
    }
    private fun getCurrentDataAndTime() {
        with(binding){
            currentViewModel.currentDate.observe(viewLifecycleOwner) { date ->
                tvStartDate.text = date
                tvLastDate.text = date
            }
            lifecycleScope.launch {
                currentViewModel.currentTimeFlow.collect { time ->
                    tvStartTime.text = time
                    tvEndTime.text = time
                }
            }
        }
    }
    private fun choiceSchedule() {
        with(binding) {
            tvStartDate.debouncedClickListener(lifecycleScope) { chooseDate(tvStartDate) }
            tvLastDate.debouncedClickListener(lifecycleScope) { chooseDate(tvLastDate) }
            tvStartTime.debouncedClickListener(lifecycleScope) { chooseTime(tvStartTime) }
            tvEndTime.debouncedClickListener(lifecycleScope) { chooseTime(tvEndTime) }
            chooseRepeat()
            iconPlace.debouncedClickListener(lifecycleScope){ choosePlace() }
            // 알람 설정 스위치
            switchAppointmentAlarm.setOnCheckedChangeListener { _, isChecked ->
                spinnerAlarm.visibility = if (isChecked) View.VISIBLE else View.GONE
            }
            //알람 시간 선택
            chooseAlarmTime()
            // 색상 선택
            iconColor.setOnClickListener { chooseColor() }
        }
    }
    private fun chooseDate(targetView: TextView) {
        with(binding.calendarView) {
            visibility = View.VISIBLE
            setOnDateChangeListener { _, year, month, dayOfMonth ->
                targetView.text = formatDate(year, month, dayOfMonth)
                visibility = View.GONE
            }
        }
    }
    private fun chooseTime(targetView: TextView) {
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("시간 선택해 보거라~~")
            .build()
        picker.show(parentFragmentManager, "MaterialTimePicker")
        picker.addOnPositiveButtonClickListener {
            val formattedTime = formatTimeToString(picker.hour, picker.minute)
            targetView.text = formattedTime
        }
    }
    private fun chooseRepeat(){
        binding.spinnerRepeat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long ) {
                repeatDays = when (position) {
                    EnumRepeat.NO.order -> EnumRepeat.NO.repeat
                    EnumRepeat.EVERY_DAY.order -> EnumRepeat.EVERY_DAY.repeat
                    EnumRepeat.EVERY_WEEK.order -> EnumRepeat.EVERY_WEEK.repeat
                    EnumRepeat.EVERY_MONTH.order -> EnumRepeat.EVERY_MONTH.repeat
                    else -> EnumRepeat.NO.repeat
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                repeatDays = EnumRepeat.NO.repeat
            }
        }
    }
    private val requestPermissionLauncher = PermissionUtils.registerLocationPermissions(
        this,
        onPermissionsGranted = { findNavController().navigate(R.id.action_addScheduleFragment_to_searchLocationFragment) },
        onPermissionsDenied = {
            if (PermissionUtils.shouldShowRequestPermissionRationaleForLocation(this)) {
                PermissionUtils.showExplanationDialog(requireContext()) { requestLocationPermissions() }
            } else {
                PermissionUtils.showPermissionsDeniedDialog(requireContext())
            }
        }
    )
    private fun choosePlace() {
        if (PermissionUtils.isLocationServiceEnabled(requireContext())) {
            if (PermissionUtils.areLocationPermissionsGranted(requireContext())) {
                findNavController().navigate(R.id.action_addScheduleFragment_to_searchLocationFragment)
            } else {
                requestLocationPermissions()
            }
        } else {
            PermissionUtils.showLocationServiceDialog(requireContext())
        }
//        {
//            //밑에 데이터 저장하는거 나중에 실험
//            popUpTo(R.id.addSchedulerFragment){
//                inclusive = true
//            }
//            launchSingleTop = true
//            restoreState = true
//        }
    }
    private fun requestLocationPermissions() {
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
    private fun chooseAlarmTime(){
        binding.spinnerAlarm.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                appointmentAlarmTime = when (position) {
                    EnumAlarmTime.BEFORE_1_HOUR.order -> EnumAlarmTime.BEFORE_1_HOUR.time
                    EnumAlarmTime.BEFORE_1_HALF_HOUR.order -> EnumAlarmTime.BEFORE_1_HALF_HOUR.time
                    EnumAlarmTime.BEFORE_2_HOUR.order -> EnumAlarmTime.BEFORE_2_HOUR.time
                    EnumAlarmTime.BEFORE_2_HALF_HOUR.order -> EnumAlarmTime.BEFORE_2_HALF_HOUR.time
                    EnumAlarmTime.BEFORE_3_HOUR.order -> EnumAlarmTime.BEFORE_3_HOUR.time
                    else -> EnumAlarmTime.BEFORE_1_HOUR.time
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                appointmentAlarmTime = EnumAlarmTime.BEFORE_1_HOUR.time
            }
        }
    }
    //만약 다크모드, 라이트모드 할때는 이부분 어떻게 바꿔야할까
    private fun chooseColor() {
        with(binding){
            flowColor.visibility = View.VISIBLE
            setColorClickListener(iconColorLavender, R.color.dark_lavender,EnumColor.LAVENDER.color)
            setColorClickListener(iconColorSage, R.color.dark_sage,EnumColor.SAGE.color)
            setColorClickListener(iconColorGrape, R.color.dark_grape,EnumColor.GRAPE.color)
            setColorClickListener(iconColorFlamingo, R.color.dark_flamingo,EnumColor.FLAMINGO.color)
            setColorClickListener(iconColorBanana, R.color.dark_banana,EnumColor.BANANA.color)
        }
    }
    private fun setColorClickListener(imageView: ShapeableImageView, colorResId: Int, saveColor: Int) {
        imageView.debouncedClickListener(lifecycleScope) {
            with(binding){
                // icon_color의 tint 색상을 클릭된 아이콘의 색상으로 변경
                iconColor.setColorFilter(ContextCompat.getColor(requireContext(), colorResId))
                color = saveColor
                flowColor.visibility = View.GONE
            }
        }
    }
    private fun setupSpinnerAdapter(){
        setupSpinner(binding.spinnerRepeat, R.array.repeat_choice)
        setupSpinner(binding.spinnerAlarm, R.array.alarm_choice)
    }
    private fun setupSpinner(spinner: Spinner, arrayResId: Int) {
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            arrayResId,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        Log.e("AddScheduleFragment", "onResume()")
    }
    override fun onPause() {
        super.onPause()
        Log.e("AddScheduleFragment", "onPause()")
    }
    override fun onStop() {
        super.onStop()
        Log.e("AddScheduleFragment", "onStop()")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("AddScheduleFragment", "onDestroyView()")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.e("AddScheduleFragment", "onDestroy()")
    }
    override fun onDetach() {
        super.onDetach()
        Log.e("AddScheduleFragment", "onDetach()")
    }
}