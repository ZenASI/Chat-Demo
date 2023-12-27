package com.chat.joycom.ui.setting.user.about

import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.chat.joycom.ds.DSKey
import com.chat.joycom.model.About
import com.chat.joycom.ui.BaseViewModel
import com.chat.joycom.utils.DataStoreUtils
import com.chat.joycom.utils.RoomUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AboutMeViewModel @Inject constructor(
    private val dataStoreUtils: DataStoreUtils,
    private val roomUtils: RoomUtils,
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    private val installAboutBool = mutableStateOf(false)

    val aboutList = roomUtils.queryAllAbout()

    init {
        viewModelScope.launch {
            installAboutBool.value =
                dataStoreUtils.readDataStoreValue(booleanPreferencesKey(DSKey.INSTALL_ABOUT), false)
            val group = savedStateHandle.get<Array<String>>(DEFAULT_ABOUT_LIST)
            if (!installAboutBool.value) {
                group?.forEachIndexed { index, text ->
                    val about = About(aboutText = text, isSelect = index == 0)
                    roomUtils.insertAbout(about)
                }
                dataStoreUtils.saveDataStoreValue(booleanPreferencesKey(DSKey.INSTALL_ABOUT), true)
            }
        }
    }

    fun upDateSelect(about: About) =
        viewModelScope.launch(Dispatchers.IO) { roomUtils.updateAboutSelect(about) }

    fun deleteAbout(about: About) =
        viewModelScope.launch(Dispatchers.IO) { roomUtils.deleteAbout(about) }

    fun deleteAllAbout() =
        viewModelScope.launch(Dispatchers.IO) { roomUtils.deleteAllAbout() }
}