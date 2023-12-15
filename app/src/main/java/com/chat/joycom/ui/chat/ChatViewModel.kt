package com.chat.joycom.ui.chat

import androidx.lifecycle.ViewModel
import com.chat.joycom.network.AppApiRepo
import com.chat.joycom.utils.RoomUtils
import com.chat.joycom.utils.SocketUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val roomUtils: RoomUtils,
    private val socketUtils: SocketUtils,
    private val appApiRepo: AppApiRepo,
) : ViewModel() {

}