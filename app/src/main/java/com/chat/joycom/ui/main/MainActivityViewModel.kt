package com.chat.joycom.ui.main

import android.os.Parcelable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.lifecycle.viewModelScope
import com.chat.joycom.ui.BaseViewModel
import com.chat.joycom.ds.DSKey
import com.chat.joycom.flow.AccountFlow
import com.chat.joycom.flow.MemberFlow
import com.chat.joycom.model.Contact
import com.chat.joycom.model.Group
import com.chat.joycom.model.Member
import com.chat.joycom.model.post.MessageServerRequest
import com.chat.joycom.model.post.MessageServerRequestJsonAdapter
import com.chat.joycom.network.ApiResult
import com.chat.joycom.network.AppApiRepo
import com.chat.joycom.utils.SocketUtils
import com.chat.joycom.network.UrlPath
import com.chat.joycom.utils.RoomUtils
import com.chat.joycom.utils.DataStoreUtils
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val appApiRepo: AppApiRepo,
    private val dataStoreUtils: DataStoreUtils,
    private val roomUtils: RoomUtils,
    private val socketUtils: SocketUtils,
    private val moshi: Moshi
) : BaseViewModel() {

    var searchState by mutableStateOf(false)
    var searchText by mutableStateOf("")
    var filterState by mutableStateOf(false)

    val userInfo = AccountFlow.stateFlow
    val memberInfo = MemberFlow.stateFlow
    private val sid = mutableStateOf("")
    private val phone = mutableStateOf("")
    private val code = mutableStateOf("")
    private val set = mutableStateOf(setOf<String>())
    val phoneCallList = mutableListOf<Member>()

    //    private val groupsFlow = roomUtils.findAllGroup().distinctUntilChanged()
//    private val contactsFlow = roomUtils.findAllContact().distinctUntilChanged()
    private val groupsFlow = flow<List<Group>> {
        emit(
            listOf<Group>(
                Group(
                    id = Random.nextLong(),
                    avatar = UUID.randomUUID().toString(),
                    groupId = Random.nextLong(),
                    groupName = "App Team RD",
                    isNoDisturb = 0,
                    userId = 0L
                ),
                Group(
                    id = Random.nextLong(),
                    avatar = UUID.randomUUID().toString(),
                    groupId = Random.nextLong(),
                    groupName = "App Team",
                    isNoDisturb = 0,
                    userId = 0L
                ),
            )
        )

    }
    private val contactsFlow = flow<List<Contact>> {
        emit(
            listOf<Contact>(
                Contact(
                    id = Random.nextLong(),
                    avatar = UUID.randomUUID().toString(),
                    userId = Random.nextLong(),
                    nickname = "Jeff",
                    account = "Jeff",
                    countryCode = "",
                    phoneNumber = "",
                    isNoDisturb = 0,
                    friendRemark = ""
                ),
                Contact(
                    id = Random.nextLong(),
                    avatar = UUID.randomUUID().toString(),
                    userId = Random.nextLong(),
                    nickname = "Kale",
                    account = "Kale",
                    countryCode = "",
                    phoneNumber = "",
                    isNoDisturb = 0,
                    friendRemark = ""
                ),
            )
        )
    }

    fun combineFlow(): Flow<List<Parcelable>> {
        return combine(groupsFlow, contactsFlow) { groupList, contactList ->
            val list = mutableListOf<Parcelable>().apply {
                addAll(contactList)
                addAll(groupList)
            }
            list
        }
    }

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
                        upDateMessageToDB()
                    }

                    is ApiResult.OnFail -> {
//                        sendState(UiEvent.GoLoginActEvent)
                    }
                }
            } else {
//                sendState(UiEvent.GoLoginActEvent)
            }
            MemberFlow.updateValue(
                Member(
                    userId = 123456,
                    accountName = "jeff",
                    nickname = "jeff",
                    avatar = UUID.randomUUID().toString()
                )
            )
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
//                        sendState(UiEvent.GoLoginActEvent)
                    }
                }
            }
        }
    }

    private fun upDateMessageToDB() {
        viewModelScope.launch {
            memberInfo.collect {
                it ?: return@collect
                val lastAckId =
                    dataStoreUtils.readDataStoreValue(longPreferencesKey(DSKey.LAST_ACK_ID), 1L)

                val json = MessageServerRequestJsonAdapter(moshi).toJson(
                    MessageServerRequest(
                        cmd = "applogin",
                        uid = it.userId,
                        token = UrlPath.config.cookie,
                        lastackmid = lastAckId.toString(),
                    )
                )
                socketUtils.send(json)
            }
        }
    }

    fun updateSearchFilter(pos: Int?) {
        filterState = pos != null
    }

    fun queryFromInputText(text: String) {
        searchText = text
    }
}