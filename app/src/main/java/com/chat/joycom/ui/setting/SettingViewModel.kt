package com.chat.joycom.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chat.joycom.BaseViewModel
import com.chat.joycom.network.ApiResult
import com.chat.joycom.network.AppApiRepo
import com.chat.joycom.ui.UiEvent
import com.chat.joycom.utils.DataStoreUtils
import com.chat.joycom.utils.RoomUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val appApiRepo: AppApiRepo,
    private val roomUtils: RoomUtils,
    private val dataStoreUtils: DataStoreUtils
): BaseViewModel() {

    fun logout() {
        viewModelScope.launch {
            when (val result = appApiRepo.logout()) {
                is ApiResult.OnSuccess -> {
                    dataStoreUtils.clearAll()
                    withContext(Dispatchers.IO) { roomUtils.db.clearAllTables() }
                    sendState(UiEvent.GoLoginActEvent)
                }

                is ApiResult.OnFail -> {
                    Timber.d("logout error => ${result.message}")
                }
            }
        }
    }
}