package com.chat.joycom.ui

import com.chat.joycom.model.Config
import com.chat.joycom.model.UserInfo

sealed class UiEvent {
    data object EmptyEvent : UiEvent()

    data class LoadingEvent(val show: Boolean) : UiEvent()

    data object SendSmsSuccessEvent : UiEvent()

    data object LoginSuccessEvent : UiEvent()

    data class GetConfigSuccessEvent(val config: Config) : UiEvent()

    data object GoLoginActEvent : UiEvent()
}
