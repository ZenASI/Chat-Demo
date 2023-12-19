package com.chat.joycom.flow

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class SingletonStateFlow<T> {

    private val _stateFlow: MutableStateFlow<T?> =
        MutableStateFlow(null)

    val stateFlow = _stateFlow.asStateFlow()

    fun updateValue(value: T) =
        _stateFlow.tryEmit(value)
}