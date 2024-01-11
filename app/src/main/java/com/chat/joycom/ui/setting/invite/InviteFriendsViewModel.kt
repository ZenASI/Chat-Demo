package com.chat.joycom.ui.setting.invite

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.chat.joycom.model.Member
import com.chat.joycom.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class InviteFriendsViewModel @Inject constructor() : BaseViewModel() {

    val onContactList = mutableStateListOf(
        Member(
            userId = Random.nextLong(),
            accountName = "jeff111",
            nickname = "jeff111",
            avatar = "jeff111"
        ),
        Member(
            userId = Random.nextLong(),
            accountName = "jeff222",
            nickname = "jeff222",
            avatar = "jeff222"
        ),
        Member(
            userId = Random.nextLong(),
            accountName = "jeff333",
            nickname = "jeff333",
            avatar = "jeff333"
        )
    )

    var showSearchBool by mutableStateOf(false)

    val searchInputText = MutableStateFlow("")
}