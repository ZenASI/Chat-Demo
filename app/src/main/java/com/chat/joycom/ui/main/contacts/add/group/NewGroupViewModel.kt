package com.chat.joycom.ui.main.contacts.add.group

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.chat.joycom.R
import com.chat.joycom.model.Member
import com.chat.joycom.ui.BaseViewModel
import com.chat.joycom.ui.UiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.random.Random

class NewGroupViewModel : BaseViewModel() {

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
        ),
    )

    val inviteList = mutableStateListOf(
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
        ),
        Member(
            userId = Random.nextLong(),
            accountName = "jeff444",
            nickname = "jeff444",
            avatar = "jeff444"
        ),
        Member(
            userId = Random.nextLong(),
            accountName = "jeff555",
            nickname = "jeff555",
            avatar = "jeff555"
        ),
    )

    var showSearchBool by mutableStateOf(false)

    private var selectList = mutableStateListOf<Member>()

    val searchInputText = MutableStateFlow("")

    val searchContactList = MutableStateFlow(emptyList<Member>())

    fun updateSelectList() {
        val result = onContactList.filter { it.isSelect } + inviteList.filter { it.isSelect }
        selectList.clear()
        selectList.addAll(result)
    }

    fun createGroupCheck(){
        if (selectList.isEmpty()){
            sendUIAction(UiEvent.ToastEvent(R.string.need_select_at_least_one_member))
            return
        }
        sendUIAction(UiEvent.GoAddGroupEvent(ArrayList(selectList)))
    }
}