package com.dongsu.timely.presentation.ui.main.calendar.add

import PermissionUtils
import android.Manifest
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dongsu.presentation.R
import com.dongsu.presentation.databinding.FragmentAddScheduleBinding
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.Schedule
import com.dongsu.timely.presentation.common.BaseFragment
import com.dongsu.timely.presentation.common.CommonUtils
import com.dongsu.timely.presentation.common.EnumAlarmTime
import com.dongsu.timely.presentation.common.EnumColor
import com.dongsu.timely.presentation.common.EnumRepeat
import com.dongsu.timely.presentation.common.SAVE_ERROR
import com.dongsu.timely.presentation.common.SAVE_LOADING
import com.dongsu.timely.presentation.common.SAVE_SUCCESS
import com.dongsu.timely.presentation.common.debouncedClickListener
import com.dongsu.timely.presentation.common.formatDate
import com.dongsu.timely.presentation.common.formatTimeToString
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

    private var repeatDays = EnumRepeat.NO.repeat
    private var appointmentAlarmTime = EnumAlarmTime.BEFORE_1_HOUR.time
    private var color = EnumColor.LAVENDER.color
    private var latitude = 0.0
    private var longitude = 0.0

    private val args: AddScheduleFragmentArgs by navArgs()

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
                    findNavController().popBackStack()
                    true
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
            CommonUtils.toastShort(requireContext(), "일정이 추가되었습니다.")
//            if (scheduleId != NO_SCHEDULE_ID) {
//                viewModel.updateSchedule(schedule)
//            } else {
//                viewModel.insertSchedule(schedule)
//            }
        }
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
            chooseColor()
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
    private val requestPermissionLauncher = PermissionUtils.registerLocationPermissions(
        this,
        onPermissionsGranted = {
            // ACCESS_FINE_LOCATION 및 ACCESS_COARSE_LOCATION 권한이 승인된 후 호출됨
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // Android 10(API 29) 이상일 때만
                requestBackgroundLocationPermission()
            } else {
                goSearchLocationFragment() // BACKGROUND 권한 없이도 다음 화면으로 이동
            }
        },
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
                // FINE 및 COARSE 권한이 승인되었으면 BACKGROUND 권한도 요청 (Android 10 이상)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    requestBackgroundLocationPermission()
                } else {
                    goSearchLocationFragment()
                }
            } else {
                requestLocationPermissions()
            }
        } else {
            PermissionUtils.showLocationServiceDialog(requireContext())
        }
    }

    private fun requestLocationPermissions() {
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun requestBackgroundLocationPermission() {
        val requestBackgroundLocationLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                goSearchLocationFragment() // 모든 권한이 승인된 경우 다음 화면으로 이동
            } else {
                PermissionUtils.showPermissionsDeniedDialog(requireContext())
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requestBackgroundLocationLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
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
    private fun checkUIState(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                addScheduleViewModel.addScheduleState.collectLatest { result ->
                    when (result) {
                        is TimelyResult.Loading -> {
                            CommonUtils.toastShort(requireContext(), SAVE_LOADING)
                        }

                        is TimelyResult.Success -> {
                            CommonUtils.toastShort(requireContext(), SAVE_SUCCESS)
                        }

                        is TimelyResult.LocalError -> {
                            CommonUtils.toastShort(requireContext(), SAVE_ERROR)
                        }

                        else -> {

                        }
                    }
                }
            }
        }
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