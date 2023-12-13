package com.chat.joycom.ui

import com.chat.joycom.model.Config
import com.chat.joycom.model.UserInfo

sealed class UiEvent {
    object EmptyEvent : UiEvent()

    data class LoadingEvent(val show: Boolean) : UiEvent()

    object SendSmsSuccessEvent : UiEvent()

    data class LoginSuccessEvent(val userInfo: UserInfo) : UiEvent()

    data class GetConfigSuccessEvent(val config: Config) : UiEvent()
}
