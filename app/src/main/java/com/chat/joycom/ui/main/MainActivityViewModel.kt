package com.chat.joycom.ui.main

import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.lifecycle.viewModelScope
import com.chat.joycom.BaseViewModel
import com.chat.joycom.ds.DSKey
import com.chat.joycom.flow.AccountFlow
import com.chat.joycom.flow.MemberFlow
import com.chat.joycom.model.Message
import com.chat.joycom.network.ApiResult
import com.chat.joycom.network.AppApiRepo
import com.chat.joycom.utils.SocketUtils
import com.chat.joycom.network.UrlPath
import com.chat.joycom.utils.RoomUtils
import com.chat.joycom.ui.UiEvent
import com.chat.joycom.utils.DataStoreUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val appApiRepo: AppApiRepo,
    private val dataStoreUtils: DataStoreUtils,
    private val roomUtils: RoomUtils,
    private val socketUtils: SocketUtils,
) : BaseViewModel() {

    val userInfo = AccountFlow.stateFlow
    val memberInfo = MemberFlow.stateFlow
    private val sid = mutableStateOf("")
    private val phone = mutableStateOf("")
    private val code = mutableStateOf("")
    private val set = mutableStateOf(setOf<String>())

     val groups = roomUtils.findAllGroup().distinctUntilChanged()
     val contacts = roomUtils.findAllContact().distinctUntilChanged()

    init {
        viewModelScope.launch {
            code.value =
                dataStoreUtils.readDataStoreValue(stringPreferencesKey(DSKey.PHONE_CODE), "")
            phone.value =
                dataStoreUtils.readDataStoreValue(stringPreferencesKey(DSKey.PHONE), "")
            sid.value =
                dataStoreUtils.readDataStoreValue(stringPreferencesKey(DSKey.COOKIE_KEY), "")
            set.value =
                dataStoreUtils.readDataStoreValue(
                    stringSetPreferencesKey(DSKey.COOKIE_SET_KEY),
                    setOf()
                )
            if (code.value.isNotEmpty() and phone.value.isNotEmpty()) {
                when (val result = appApiRepo.getBasicConfig()) {
                    is ApiResult.OnSuccess -> {
                        UrlPath.config = result.data
                        connectSocket()
                        querySelf()
                    }

                    is ApiResult.OnFail -> {
                        sendState(UiEvent.GoLoginActEvent)
                    }
                }
            } else {
                sendState(UiEvent.GoLoginActEvent)
            }
        }
    }

    private fun connectSocket() = socketUtils.connect()

    private fun querySelf() {
        viewModelScope.launch {
            when (val result = appApiRepo.queryMember(code.value.substring(1), phone.value)) {
                is ApiResult.OnSuccess -> {
                    MemberFlow.updateValue(result.data)
                }

                is ApiResult.OnFail -> {
                    Timber.d("querySelf error => ${result.message}")
                    if (result.code == 401) {
                        sendState(UiEvent.GoLoginActEvent)
                    }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            when (val result = appApiRepo.logout()) {
                is ApiResult.OnSuccess -> {
                    dataStoreUtils.clearAll()
                    sendState(UiEvent.GoLoginActEvent)
                }

                is ApiResult.OnFail -> {
                    Timber.d("logout error => ${result.message}")
                }
            }
        }
    }
}