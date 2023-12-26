package com.chat.joycom.ui.setting.user

import androidx.lifecycle.ViewModel
import com.chat.joycom.flow.MemberFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor() : ViewModel() {

    val memberInfo = MemberFlow.stateFlow
}