package com.chat.joycom.ui.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import com.chat.joycom.flow.MemberFlow
import com.chat.joycom.model.Contact
import com.chat.joycom.model.Group
import com.chat.joycom.model.GroupContact
import com.chat.joycom.model.Message
import com.chat.joycom.network.ApiResult
import com.chat.joycom.network.AppApiRepo
import com.chat.joycom.paging.MessagePagingSource
import com.chat.joycom.utils.RoomUtils
import com.chat.joycom.utils.SocketUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val roomUtils: RoomUtils,
    private val socketUtils: SocketUtils,
    private val appApiRepo: AppApiRepo,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val PAGING_SIZE = 30
    private val isGroup = savedStateHandle.get<Boolean>(IS_GROUP) ?: false
    private val contact = savedStateHandle.get<Contact>(CONTACT_INFO)
    private val group = savedStateHandle.get<Group>(GROUP_INFO)

    val memberInfo = MemberFlow.stateFlow
    var groupContactList = flowOf(emptyList<GroupContact>())
    var contactInfo = MutableStateFlow<Contact?>(null)

    // 目前先用這個展示訊息好與db切開
    var protoTypeMessage: MutableList<Message> =
        mutableListOf(
            Message.getFakeMsg(isGroup),
            Message.getFakeMsg(isGroup, msgType = 3),
            Message.getFakeMsg(isGroup, msgType = 3),
            Message.getFakeMsg(isGroup, msgType = 4),
            Message.getFakeMsg(isGroup, msgType = 4),
            Message.getFakeMsg(isGroup)
        )


    var pagingMessage: Flow<PagingData<Message>> =
        emptyFlow<PagingData<Message>>().cachedIn(viewModelScope)

    init {
        if (isGroup) {
            getGroupContact(groupId = group?.groupId)
            groupContactList = groupContact(groupId = group?.groupId)
            group?.groupId?.let {
                pagingMessage = Pager(
                    config = PagingConfig(
                        pageSize = PAGING_SIZE,
                        prefetchDistance = 1,
                        initialLoadSize = PAGING_SIZE,
                        enablePlaceholders = true,
                        maxSize = PAGING_SIZE * 10
                    ),
                    pagingSourceFactory = {
                        MessagePagingSource(
                            id = group.groupId,
                            roomUtils = roomUtils
                        )
                    }
                ).flow
                    .map { pagingData ->
                        pagingData.insertSeparators { beforeItem: Message?, afterItem: Message? ->
                            if (afterItem == null && beforeItem != null) {
                                return@insertSeparators beforeItem.copy(msgType = -1).apply {
                                    showIcon = false
                                }
                            } else if (afterItem != null && beforeItem != null) {
                                val afterDate = Instant.ofEpochMilli(afterItem.sendTicks)
                                    .atZone(ZoneId.systemDefault()).toLocalDate()
                                val beforeDate = Instant.ofEpochMilli(beforeItem.sendTicks)
                                    .atZone(ZoneId.systemDefault()).toLocalDate()
                                if (afterDate.isEqual(beforeDate)) {
                                    return@insertSeparators null
                                } else {
                                    return@insertSeparators beforeItem.copy(msgType = -1).apply {
                                        showIcon = false
                                    }
                                }
                            } else {
                                return@insertSeparators null
                            }
                        }
                    }
                    .cachedIn(viewModelScope)
            }
        } else {
            contactInfo.value = contact
            val selfId = memberInfo.value?.userId
            val userId = contact?.userId
            if (selfId != null && userId != null) {
                pagingMessage = Pager(
                    config = PagingConfig(
                        pageSize = PAGING_SIZE,
                        prefetchDistance = 1,
                        initialLoadSize = PAGING_SIZE,
                        enablePlaceholders = true,
                        maxSize = PAGING_SIZE * 10
                    ),
                    pagingSourceFactory = { MessagePagingSource(selfId, userId, roomUtils) }
                ).flow
                    .map { pagingData ->
                        pagingData.insertSeparators { beforeItem: Message?, afterItem: Message? ->
                            if (afterItem == null && beforeItem != null) {
                                return@insertSeparators beforeItem.copy(msgType = -1).apply {
                                    showIcon = false
                                }
                            } else if (afterItem != null && beforeItem != null) {
                                val afterDate = Instant.ofEpochMilli(afterItem.sendTicks)
                                    .atZone(ZoneId.systemDefault()).toLocalDate()
                                val beforeDate = Instant.ofEpochMilli(beforeItem.sendTicks)
                                    .atZone(ZoneId.systemDefault()).toLocalDate()
                                if (afterDate.isEqual(beforeDate)) {
                                    return@insertSeparators null
                                } else {
                                    return@insertSeparators beforeItem.copy(msgType = -1).apply {
                                        showIcon = false
                                    }
                                }
                            } else {
                                return@insertSeparators null
                            }
                        }
                    }
                    .cachedIn(viewModelScope)
            }
        }
    }

    private fun groupContact(groupId: Long?) = kotlin.run {
        groupId ?: return@run flowOf(mutableListOf())
        roomUtils.findGroupContact(groupId)
    }

    fun sentMessage(it: Message) = run {
//        socketUtils.send(it)
        protoTypeMessage.add(0, it)
    }

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