package com.chat.joycom.flow

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow

abstract class SingletonStateFlow<T> {

    private val _shareFlow: MutableSharedFlow<T> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    val stateFlow = _shareFlow.asSharedFlow()

    fun updateValue(value: T) =
        _shareFlow.tryEmit(value)
}