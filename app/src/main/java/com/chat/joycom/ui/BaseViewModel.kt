package com.chat.joycom.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber

open class BaseViewModel: ViewModel() {

    private val _uiState =
        MutableSharedFlow<UiEvent>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val uiState = _uiState.asSharedFlow()

    fun sendState(event: UiEvent) =
        viewModelScope.launch {
            Timber.d("sendState $event")
            _uiState.tryEmit(event)
        }
}