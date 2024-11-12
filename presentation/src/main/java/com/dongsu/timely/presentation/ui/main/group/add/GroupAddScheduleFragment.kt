package com.dongsu.timely.presentation.ui.main.group.add

import PermissionUtils
import android.Manifest
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dongsu.presentation.R
import com.dongsu.presentation.databinding.FragmentGroupAddScheduleBinding
import com.dongsu.timely.domain.model.GroupSchedule
import com.dongsu.timely.presentation.common.BaseFragment
import com.dongsu.timely.presentation.common.DEFAULT_ALARM_TIME
import com.dongsu.timely.presentation.common.combineDateTime
import com.dongsu.timely.presentation.common.debouncedClickListener
import com.dongsu.timely.presentation.common.formatDate
import com.dongsu.timely.presentation.common.formatTimeToString
import com.dongsu.timely.presentation.viewmodel.group.GroupAddScheduleViewModel
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GroupAddScheduleFragment: BaseFragment<FragmentGroupAddScheduleBinding>(FragmentGroupAddScheduleBinding::inflate) {

    private val groupAddScheduleViewModel: GroupAddScheduleViewModel by viewModels()
    private val args: GroupAddScheduleFragmentArgs by navArgs()

    override fun initView() {
        Log.e("그룹스케줄추가", "$args")
        setupToolbar()
        toolbarAction()
        getCurrentDataAndTime()
        choiceSchedule()
    }
    private fun setupToolbar() = binding.toolbar.inflateMenu(R.menu.toolbar_menu)
    private fun toolbarAction() {
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
    private fun choiceSchedule() {
        with(binding) {
            tvStartDate.debouncedClickListener(lifecycleScope) { chooseDate(tvStartDate) }
            tvStartTime.debouncedClickListener(lifecycleScope) { chooseTime(tvStartTime) }
            tvEndTime.debouncedClickListener(lifecycleScope) { chooseTime(tvEndTime) }
            iconPlace.debouncedClickListener(lifecycleScope){ choosePlace() }
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
    private val requestPermissionLauncher = PermissionUtils.registerLocationPermissions(
        this,
        onPermissionsGranted = {
            goSearchLocationFragment()
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
                goSearchLocationFragment()
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
}


//    private fun postData() {
//        binding.etTitle.addTextChangedListener { changedTitle ->
//            groupAddScheduleViewModel.onTitleChanged(changedTitle.toString())
//            Log.e("title바뀌는중", changedTitle.toString())
//        }
//    }
//    private fun observeData() {
//        lifecycleScope.launch {
//            observeTitle()
//        }
//    }
//    private suspend fun observeTitle() {
//        groupAddScheduleViewModel.title.collectLatest { changedTitle ->
//            binding.etTitle.setText(changedTitle)
//        }
//        groupAddScheduleViewModel.title.observe(viewLifecycleOwner) { observeTitle ->
//            binding.etTitle.setText(observeTitle)
//            Log.e("title가져옴", observeTitle)
//        }
//    }

