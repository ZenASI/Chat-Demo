package com.chat.joycom.ui.login

import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.viewModelScope
import com.chat.joycom.ui.BaseViewModel
import com.chat.joycom.ds.DSKey
import com.chat.joycom.flow.AccountFlow
import com.chat.joycom.network.ApiResult
import com.chat.joycom.network.AppApiRepo
import com.chat.joycom.network.UrlPath
import com.chat.joycom.utils.RoomUtils
import com.chat.joycom.ui.UiEvent
import com.chat.joycom.utils.DataStoreUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val appApiRepo: AppApiRepo,
    private val dataStoreUtils: DataStoreUtils,
    private val roomUtils: RoomUtils,
) : BaseViewModel() {
    init {
        viewModelScope.launch {
            when (val result = appApiRepo.getBasicConfig()) {
                is ApiResult.OnSuccess -> {
                    UrlPath.config = result.data // set to object
//                    dataStoreUtils.saveDataStoreValue(
//                        stringPreferencesKey(DSKey.COOKIE_KEY),
//                        result.data.cookie
//                    )
                }

                is ApiResult.OnFail -> {
                    Timber.d("getBasicConfig error ${result.message}")
                }
            }
        }
    }

    fun sendSms(countryCode: String, phone: String) {
        if (phone.isEmpty()) return
        viewModelScope.launch {
            when (val result = appApiRepo.sendSms(countryCode.substring(1), phone)) {
                is ApiResult.OnSuccess -> {
                    // TODO: maybe send ui event notice user?
                }

                is ApiResult.OnFail -> {
                    Timber.d("sendSms error ${result.message}")
                }
            }
        }
    }

    fun goLogin(countryCode: String, phone: String, otp: String) {
        if (countryCode.isEmpty() or phone.isEmpty() or otp.isEmpty()) return
        viewModelScope.launch {
            when (val result = appApiRepo.goPhoneLogin(countryCode.substring(1), phone, otp)) {
                is ApiResult.OnSuccess -> {
                    dataStoreUtils.saveDataStoreValue(
                        stringPreferencesKey(DSKey.PHONE),
                        phone
                    )
                    dataStoreUtils.saveDataStoreValue(
                        stringPreferencesKey(DSKey.PHONE_CODE),
                        countryCode
                    )
                    dataStoreUtils.saveDataStoreValue(
                        longPreferencesKey(DSKey.LAST_ACK_ID),
                        1L
//                        result.data.lastAckId
                    )
                    dataStoreUtils.saveDataStoreValue(
                        stringPreferencesKey(DSKey.COOKIE_KEY),
                        UrlPath.config.cookie
                    )
                    AccountFlow.updateValue(result.data)
                    roomUtils.insertContact(result.data.contacts)
                    roomUtils.insertGroup(result.data.groups)
                    sendUIAction(UiEvent.LoginSuccessEvent)
                }

                is ApiResult.OnFail -> {
                    Timber.d("goLogin error ${result.message}")
                    sendUIAction(UiEvent.LoginSuccessEvent)
                }
            }
        }
    }

    fun goRegister(nickName: String, countryCode: String, phone: String, otp: String) {
        if (phone.isEmpty() or otp.isEmpty() or nickName.isEmpty()) return
        viewModelScope.launch {
            when (val result =
                appApiRepo.goRegister(nickName, countryCode.substring(1), phone, otp)) {
                is ApiResult.OnSuccess -> {
                    AccountFlow.updateValue(result.data)
                }

                is ApiResult.OnFail -> {
                    Timber.d("goRegister error ${result.message}")
                }
            }
        }
    }
}