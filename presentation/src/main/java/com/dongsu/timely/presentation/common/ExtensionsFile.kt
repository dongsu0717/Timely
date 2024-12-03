package com.dongsu.timely.presentation.common

import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks

//fun <T> LifecycleOwner.collectWhenStarted(flow: StateFlow<T>, action: suspend (value: T) -> Unit) {
//    lifecycleScope.launch {
//        flow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collect(action)
//    }
//}

//fun Activity.hideSoftInput() {
//    // 포커스 있는지 체크
//    window.currentFocus?.let { view ->
//        val inputMethodManager = getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager // 키보드 관리 객체 가져옴
//        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0) // 키보드 내리기
//        view.clearFocus() // 포커스 제거
//    }
//}

//Flow Binding
const val DEBOUNCE_DEFAULT_TIME = 300L
const val THROTTLE_DEFAULT_TIME = 1000L
const val THROTTLE_TIME = 0L

@OptIn(FlowPreview::class)
fun View.debouncedClick(
    scope: CoroutineScope,
    debounceTime: Long = DEBOUNCE_DEFAULT_TIME,
    action: () -> Unit
) {
    this.clicks()
        .debounce(debounceTime)
        .onEach { action() }
        .launchIn(scope)
}

fun View.throttledClickListener(
    scope: CoroutineScope,
    intervalTime: Long = THROTTLE_DEFAULT_TIME,
    action: () -> Unit
) {
    flowClicks()
        .throttleFirst(intervalTime)
        .onEach { action() }
        .launchIn(scope)
}

fun View.flowClicks(): Flow<Unit> = callbackFlow {
    setOnClickListener { trySend(Unit).isSuccess }
    awaitClose { setOnClickListener(null) }
}.buffer(0)

fun <T> Flow<T>.throttleFirst(intervalTime: Long): Flow<T> = flow {
    var throttleTime = THROTTLE_TIME
    collect { value ->
        val currentTime = System.currentTimeMillis()
        if ((currentTime - throttleTime) > intervalTime) {
            throttleTime = currentTime
            emit(value)
        }
    }
}
