package com.chat.joycom.ui.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chat.joycom.flow.MemberFlow
import com.chat.joycom.model.Contact
import com.chat.joycom.model.Group
import com.chat.joycom.model.GroupContact
import com.chat.joycom.model.Message
import com.chat.joycom.network.ApiResult
import com.chat.joycom.network.AppApiRepo
import com.chat.joycom.utils.RoomUtils
import com.chat.joycom.utils.SocketUtils
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val roomUtils: RoomUtils,
    private val socketUtils: SocketUtils,
    private val appApiRepo: AppApiRepo,
    private val savedStateHandle: SavedStateHandle,
    private val moshi: Moshi,
) : ViewModel() {
    val memberInfo = MemberFlow.stateFlow
    var messageList = flowOf(emptyList<Message>())
    var groupContactList = flowOf(emptyList<GroupContact>())
    var contactInfo = MutableStateFlow<Contact?>(null)

    init {
        val isGroup = savedStateHandle.get<Boolean>(IS_GROUP) ?: false
        val contact = savedStateHandle.get<Contact>(CONTACT_INFO)
        val group = savedStateHandle.get<Group>(GROUP_INFO)
        if (isGroup) {
            getGroupContact(groupId = group?.groupId)
            messageList = queryByGroupId(groupId = group?.groupId)
            groupContactList = groupContact(groupId = group?.groupId)
        } else {
            messageList = queryByUserId(contact?.userId)
            contactInfo.value = contact
        }
    }

    private fun groupContact(groupId: Long?) = kotlin.run {
        groupId ?: return@run flowOf(mutableListOf())
        roomUtils.findGroupContact(groupId)
    }

    private fun queryByUserId(id: Long?) = run {
        // TODO: 處理content to data class
        id ?: return@run flowOf(mutableListOf())
        val selfId = memberInfo.value?.userId ?: return@run flowOf(mutableListOf())
        roomUtils.queryMessageByUserId(id, selfId)
    }

    private fun queryByGroupId(groupId: Long?) = run {
        // TODO: 處理content to data class
        groupId ?: return@run flowOf(mutableListOf())
        roomUtils.queryMessageByGroupId(groupId)
    }

    fun sentMessage(it: Message) = socketUtils.send(it)

    private fun getGroupContact(groupId: Long?) {
        groupId ?: return
        viewModelScope.launch {
            when (val result = appApiRepo.queryGroupContacts(groupId)) {
                is ApiResult.OnSuccess -> {
                    val list = result.data.map { it.copy(groupId = groupId) }
                    roomUtils.insertGroupContact(list)
                }

                is ApiResult.OnFail -> {
                    Timber.d("getGroupContact error ${result.message}")
                }
            }
        }
    }
}