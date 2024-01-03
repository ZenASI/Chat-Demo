package com.chat.joycom.ui.setting.qrcode

import com.chat.joycom.flow.MemberFlow
import com.chat.joycom.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QRCodeViewModel @Inject constructor() : BaseViewModel() {

    val memberInfo = MemberFlow.stateFlow
}