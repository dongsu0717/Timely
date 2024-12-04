package com.dongsu.timely.presentation.ui.main.group.add

import android.Manifest
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dongsu.presentation.R
import com.dongsu.presentation.databinding.FragmentGroupAddScheduleBinding
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.GroupSchedule
import com.dongsu.timely.presentation.common.BaseFragment
import com.dongsu.timely.presentation.common.CommonUtils
import com.dongsu.timely.presentation.common.DEFAULT_ALARM_TIME
import com.dongsu.timely.presentation.common.DialogUtils
import com.dongsu.timely.presentation.common.PermissionUtils
import com.dongsu.timely.presentation.common.SAVE_ERROR
import com.dongsu.timely.presentation.common.SAVE_LOADING
import com.dongsu.timely.presentation.common.SAVE_SUCCESS
import com.dongsu.timely.presentation.common.combineDateTime
import com.dongsu.timely.presentation.common.formatDate
import com.dongsu.timely.presentation.common.formatTimeToString
import com.dongsu.timely.presentation.common.throttledClickListener
import com.dongsu.timely.presentation.viewmodel.group.GroupAddScheduleViewModel
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GroupAddScheduleFragment: BaseFragment<FragmentGroupAddScheduleBinding>(FragmentGroupAddScheduleBinding::inflate) {

    private val groupAddScheduleViewModel: GroupAddScheduleViewModel by viewModels()
    private val args: GroupAddScheduleFragmentArgs by navArgs()

    private val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    @RequiresApi(Build.VERSION_CODES.Q)
    private val backgroundLocationPermission =
        Manifest.permission.ACCESS_BACKGROUND_LOCATION

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

    override fun initView() {
        Log.e("그룹스케줄추가", "$args")
        setupToolbar()
        setupArgs()
        getCurrentDataAndTime()
        choiceSchedule()
        checkUIState()
    }
    private fun setupToolbar(){
        binding.toolbar.toolbarCommon.apply {
            setNavigationOnClickListener { findNavController().popBackStack() }
            setOnMenuItemClickListener {
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
    }
    private fun setupArgs() {
        binding.tvAppointmentPlace.text = args.place
    }
    private fun choiceSchedule() {
        with(binding) {
            tvStartDate.throttledClickListener(lifecycleScope) { chooseDate(tvStartDate) }
            tvStartTime.throttledClickListener(lifecycleScope) { chooseTime(tvStartTime) }
            tvEndTime.throttledClickListener(lifecycleScope) { chooseTime(tvEndTime) }
            iconPlace.throttledClickListener(lifecycleScope){ choosePlace() }
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
    private fun goSearchLocationFragment() {
        val action = GroupAddScheduleFragmentDirections.actionGroupAddScheduleFragmentToSearchLocationFragment(args.groupId)
        findNavController().navigate(action)
    }
    private fun saveSchedule() {
        lifecycleScope.launch {
            groupAddScheduleViewModel.addSchedule(args.groupId, makeGroupSchedule())
        }
    }
    private fun makeGroupSchedule(): GroupSchedule {
        return with(binding) {
            GroupSchedule(
                title = etTitle.text.toString(),
                startTime = combineDateTime(tvStartDate.text.toString(), tvStartTime.text.toString()),
                endTime = combineDateTime(tvStartDate.text.toString(), tvEndTime.text.toString()),
                isAlarmEnabled = switchAppointmentAlarm.isChecked,
                alarmBeforeHours = DEFAULT_ALARM_TIME,
                location = tvAppointmentPlace.text.toString(),
                latitude = args.latitude.toDouble(),
                longitude = args.longitude.toDouble()
            )
        }
    }
    private fun getCurrentDataAndTime() {
        with(binding){
            groupAddScheduleViewModel.currentDate.observe(viewLifecycleOwner) { date ->
                tvStartDate.text = date
            }
            lifecycleScope.launch {
                groupAddScheduleViewModel.currentTimeFlow.collect { time ->
                    tvStartTime.text = time
                    tvEndTime.text = time
                }
            }
        }
    }
    private fun checkUIState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                groupAddScheduleViewModel.addScheduleState.collectLatest {result ->
                    when (result) {
                        is TimelyResult.Loading -> {
                            CommonUtils.toastShort(requireContext(), SAVE_LOADING)
                        }
                        is TimelyResult.Success -> {
                            CommonUtils.toastShort(requireContext(), SAVE_SUCCESS)
                        }
                        is TimelyResult.NetworkError -> {
                            CommonUtils.toastShort(requireContext(), SAVE_ERROR)
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}

