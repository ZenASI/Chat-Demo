package com.chat.joycom.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

open class BaseViewModel: ViewModel() {

    private val _uiAction =
        MutableSharedFlow<UiEvent>()
    val uiAction = _uiAction.asSharedFlow()

    private val _uiState =
        MutableSharedFlow<UiEvent>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val uiState = _uiState.asSharedFlow()

    fun sendUIState(event: UiEvent) =
        viewModelScope.launch {
            _uiState.tryEmit(event)
        }

    fun sendUIAction(event: UiEvent) =
        viewModelScope.launch {
            _uiAction.emit(event)
        }
}