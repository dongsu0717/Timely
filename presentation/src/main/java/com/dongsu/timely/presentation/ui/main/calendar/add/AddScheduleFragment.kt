package com.dongsu.timely.presentation.ui.main.calendar.add

import android.Manifest
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dongsu.presentation.R
import com.dongsu.presentation.databinding.FragmentAddScheduleBinding
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.Schedule
import com.dongsu.timely.presentation.common.BaseFragment
import com.dongsu.timely.presentation.common.CommonUtils
import com.dongsu.timely.presentation.common.DialogUtils
import com.dongsu.timely.presentation.common.EnumAlarmTime
import com.dongsu.timely.presentation.common.EnumRepeat
import com.dongsu.timely.presentation.common.EnumScheduleColor
import com.dongsu.timely.presentation.common.PermissionUtils
import com.dongsu.timely.presentation.common.SAVE_ERROR
import com.dongsu.timely.presentation.common.SAVE_LOADING
import com.dongsu.timely.presentation.common.SAVE_SUCCESS
import com.dongsu.timely.presentation.common.formatDate
import com.dongsu.timely.presentation.common.formatTimeToString
import com.dongsu.timely.presentation.common.launchRepeatOnLifecycle
import com.dongsu.timely.presentation.common.throttledClickListener
import com.dongsu.timely.presentation.viewmodel.calendar.add.AddScheduleViewModel
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddScheduleFragment : BaseFragment<FragmentAddScheduleBinding>(FragmentAddScheduleBinding::inflate) {

    private val addScheduleViewModel: AddScheduleViewModel by viewModels()
    private val args: AddScheduleFragmentArgs by navArgs()

    private val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    @RequiresApi(Build.VERSION_CODES.Q)
    private val backgroundLocationPermission =
        Manifest.permission.ACCESS_BACKGROUND_LOCATION

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val postNotificationPermission = arrayOf(
        Manifest.permission.POST_NOTIFICATIONS
    )

    private val requestPermissionLauncher = PermissionUtils.requestMultiplePermissions(
        fragment = this,
        permissions = locationPermissions,
        onGranted = { checkBackgroundLocationNeeded() },
        onDenied = { reRequestLocationPermissions() }
    )

    private val requestBackgroundLocationLauncher = PermissionUtils.requestSinglePermission(
        fragment = this,
        onGranted = { goSearchLocationFragment() },
        onDenied = { DialogUtils.showLocationPermissionsDeniedDialog(requireContext(),parentFragmentManager) }
    )

    private var repeatDays = EnumRepeat.NO.repeat
    private var appointmentAlarmTime = EnumAlarmTime.BEFORE_1_HOUR.time
    private var color = EnumScheduleColor.LAVENDER.color
    private var latitude = 0.0
    private var longitude = 0.0

    override fun initView() {
        setupArgument()
        toolbarAction()
        getCurrentDataAndTime()
        setupSpinnerAdapter()
        choiceSchedule()
        checkUIState()
    }

    private fun setupArgument(){
            latitude = args.latitude.toDouble()
            longitude = args.longitude.toDouble()
            binding.tvAppointmentPlace.text = args.place
    }

    private fun toolbarAction(){
        binding.toolbar.toolbarCommon.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.toolbar.toolbarCommon.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_save -> {
                    saveSchedule()
                    true
                }
                else -> false
            }
        }
    }

    private fun choiceSchedule() {
        with(binding) {
            tvStartDate.throttledClickListener(lifecycleScope) { chooseDate(tvStartDate) }
            tvLastDate.throttledClickListener(lifecycleScope) { chooseDate(tvLastDate) }
            tvStartTime.throttledClickListener(lifecycleScope) { chooseTime(tvStartTime) }
            tvEndTime.throttledClickListener(lifecycleScope) { chooseTime(tvEndTime) }
            chooseRepeat()

            iconPlace.throttledClickListener(lifecycleScope){ choosePlace() }
            // 알람 설정 스위치
            switchAppointmentAlarm.setOnCheckedChangeListener { _, isChecked ->
                chooseAlarmPresenceOrAbsence(isChecked)
            }
            //알람 시간 선택
            chooseAlarmTime()
            // 색상 선택
            chooseColor()
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

    private fun chooseDate(targetView: TextView) {
        with(binding.calendarView) {
            setOnDateChangeListener { _, year, month, dayOfMonth ->
                targetView.text = formatDate(year, month, dayOfMonth)
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
            setColorClickListener(iconColorLavender, R.color.dark_lavender,EnumScheduleColor.LAVENDER.color)
            setColorClickListener(iconColorSage, R.color.dark_sage,EnumScheduleColor.SAGE.color)
            setColorClickListener(iconColorGrape, R.color.dark_grape,EnumScheduleColor.GRAPE.color)
            setColorClickListener(iconColorFlamingo, R.color.dark_flamingo,EnumScheduleColor.FLAMINGO.color)
            setColorClickListener(iconColorBanana, R.color.dark_banana,EnumScheduleColor.BANANA.color)
        }
    }

    private fun setColorClickListener(imageView: ShapeableImageView, colorResId: Int, saveColor: Int) {
        imageView.throttledClickListener(lifecycleScope) {
            with(binding){
                iconColor.setColorFilter(ContextCompat.getColor(requireContext(), colorResId))
                color = saveColor
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

    private fun goSearchLocationFragment(){
        findNavController().navigate(R.id.action_addScheduleFragment_to_searchLocationFragment)
//        {
//            //밑에 데이터 저장하는거 나중에 실험
//            popUpTo(R.id.addSchedulerFragment){
//                inclusive = true
//            }
//            launchSingleTop = true
//            restoreState = true
//        }
    }

    private fun getCurrentDataAndTime() {
        with(binding){
            addScheduleViewModel.currentDate.observe(viewLifecycleOwner) { date ->
                tvStartDate.text = date
                tvLastDate.text = date
            }
            lifecycleScope.launch {
                addScheduleViewModel.currentTimeFlow.collect { time ->
                    tvStartTime.text = time
                    tvEndTime.text = time
                }
            }
        }
    }

    private fun choosePlace() =
        checkLocationServiceEnabled()

    private fun checkLocationServiceEnabled(){
        val isServiceEnabled = PermissionUtils.isLocationServiceEnabled(requireContext())
        if(isServiceEnabled){
            checkPermissionsGranted()
        } else {
            DialogUtils.showLocationServiceActivationDialog(requireContext(),parentFragmentManager)
        }
    }

    private fun checkPermissionsGranted(){
        val isPermissionGranted = PermissionUtils.arePermissionsGranted(requireContext(), locationPermissions)
        if(isPermissionGranted){
            checkBackgroundLocationNeeded()
        } else {
            requestLocationPermissions()
        }
    }

    private fun checkBackgroundLocationNeeded(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requestBackgroundLocationPermission()
        } else {
            goSearchLocationFragment()
        }
    }

    private fun reRequestLocationPermissions(){
        val isNotCheckedDoNotAskAgain = PermissionUtils.shouldShowRequestPermissionRationale(this, locationPermissions)
        if (isNotCheckedDoNotAskAgain) {
            DialogUtils.showLocationPermissionsNeededDialog(parentFragmentManager){
                requestLocationPermissions()
            }
        } else {
            DialogUtils.showLocationPermissionsDeniedDialog(requireContext(),parentFragmentManager)
        }
    }
    
    private fun requestLocationPermissions() =
        requestPermissionLauncher.launch(locationPermissions)


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestBackgroundLocationPermission() =
        requestBackgroundLocationLauncher.launch(backgroundLocationPermission)

    private fun chooseAlarmPresenceOrAbsence(isChecked: Boolean) {
        if (isChecked) {
            isPostNotificationPermissionGranted()
        } else {
            binding.spinnerAlarm.visibility = View.GONE
        }
    }

    private fun isPostNotificationPermissionGranted(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PermissionUtils.requestTedPermissions(
                permissions = postNotificationPermission,
                onGranted = {
                    binding.spinnerAlarm.visibility = View.VISIBLE
                },
                onDenied = {
                    binding.switchAppointmentAlarm.isChecked = false
                    binding.spinnerAlarm.visibility = View.GONE
                }
            )
        }
    }

    private fun checkUIState(){
        launchRepeatOnLifecycle {
            addScheduleViewModel.addScheduleState.collectLatest { result ->
                when (result) {
                    is TimelyResult.Loading -> {
                        CommonUtils.toastShort(requireContext(), SAVE_LOADING)
                    }

                    is TimelyResult.Success -> {
                        CommonUtils.toastShort(requireContext(), SAVE_SUCCESS)
                        findNavController().popBackStack()
                    }

                    is TimelyResult.LocalError -> {
                        Log.e("AddScheduleFragment", "스케줄 저장 error: ${result.exception}")
                        CommonUtils.toastShort(requireContext(), SAVE_ERROR)
                    }

                    else -> {

                    }
                }
            }
        }
    }
}