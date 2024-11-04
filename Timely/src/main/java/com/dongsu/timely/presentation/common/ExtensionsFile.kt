package com.dongsu.timely.presentation.common

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import reactivecircus.flowbinding.android.view.clicks

//fun <T> LifecycleOwner.collectWhenStarted(flow: StateFlow<T>, action: suspend (value: T) -> Unit) {
//    lifecycleScope.launch {
//        flow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collect(action)
//    }
//}
//
//fun Activity.hideSoftInput() {
//    // 포커스 있는지 체크
//    window.currentFocus?.let { view ->
//        val inputMethodManager = getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager // 키보드 관리 객체 가져옴
//        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0) // 키보드 내리기
//        view.clearFocus() // 포커스 제거
//    }
//}

@OptIn(FlowPreview::class)
fun View.debouncedClickListener(
    scope: CoroutineScope,
    debounceTime: Long = 200,
    action: () -> Unit
) {
    this.clicks()
        .debounce(debounceTime)
        .onEach { action() }
        .launchIn(scope)
}