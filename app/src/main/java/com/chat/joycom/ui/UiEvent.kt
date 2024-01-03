package com.chat.joycom.ui

import androidx.annotation.StringRes
import com.chat.joycom.model.Config
import com.chat.joycom.model.Member

sealed class UiEvent {
    data object EmptyEvent : UiEvent()

    /*=======================================================================*/

    data object LoginSuccessEvent : UiEvent()

    data object GoLoginActEvent : UiEvent()

    data class ToastEvent(@StringRes val msgId: Int) : UiEvent()

    data class GoAddGroupEvent(val list: ArrayList<Member>) : UiEvent()

    /*=======================================================================*/

    data class LoadingEvent(val show: Boolean) : UiEvent()
}
