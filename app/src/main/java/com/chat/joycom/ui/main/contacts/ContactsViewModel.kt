package com.chat.joycom.ui.main.contacts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.chat.joycom.model.Member
import com.chat.joycom.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@OptIn(FlowPreview::class)
@HiltViewModel
class ContactsViewModel @Inject constructor() : BaseViewModel() {

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
    )

    var showSearchBool by mutableStateOf(false)

    val searchInputText = MutableStateFlow("")

    val searchContactList = MutableStateFlow(emptyList<Member>())

    init {
        viewModelScope.launch {
            searchInputText
                .debounce(500)
                .distinctUntilChanged()
                .collectLatest {
                    // TODO: filter onContactList inviteList
                    searchContactList.emit(emptyList())
                }
        }
    }
}