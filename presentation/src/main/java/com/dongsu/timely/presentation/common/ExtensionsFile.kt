package com.dongsu.timely.presentation.common

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
import kotlinx.coroutines.launch
import reactivecircus.flowbinding.android.view.clicks

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

fun LifecycleOwner.launchRepeatOnLifecycle(
    state: Lifecycle.State = Lifecycle.State.STARTED,
    block: suspend CoroutineScope.() -> Unit
) {
    val lifecycleOwner = if (this is Fragment) viewLifecycleOwner else this
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.lifecycle.repeatOnLifecycle(state, block)
    }
}
