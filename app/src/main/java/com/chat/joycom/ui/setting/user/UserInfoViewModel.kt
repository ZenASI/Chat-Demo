package com.chat.joycom.ui.setting.user

import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chat.joycom.ds.DSKey
import com.chat.joycom.flow.MemberFlow
import com.chat.joycom.utils.DataStoreUtils
import com.chat.joycom.utils.RoomUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    private val roomUtils: RoomUtils,
    private val dataStoreUtils: DataStoreUtils
) : ViewModel() {

    val memberInfo = MemberFlow.stateFlow

    val currentAbout = roomUtils.querySelectAbout()

    val phone = mutableStateOf("")

    init {
        viewModelScope.launch {
            val phoneNumber = dataStoreUtils.readDataStoreValue(stringPreferencesKey(DSKey.PHONE), "")
            val code = dataStoreUtils.readDataStoreValue(stringPreferencesKey(DSKey.PHONE_CODE), "")
            phone.value = "$code$phoneNumber"
        }
    }
}