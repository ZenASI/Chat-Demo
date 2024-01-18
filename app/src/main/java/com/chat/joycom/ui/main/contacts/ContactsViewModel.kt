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
import timber.log.Timber
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
            avatar = "https://picsum.photos/200?random=10"
        ),
        Member(
            userId = Random.nextLong(),
            accountName = "jeff222",
            nickname = "jeff222",
            avatar = "https://picsum.photos/200?random=11"
        ),
        Member(
            userId = Random.nextLong(),
            accountName = "jeff333",
            nickname = "jeff333",
            avatar = "https://picsum.photos/200?random=12"
        )
    )

    val inviteList = mutableStateListOf(
        Member(
            userId = Random.nextLong(),
            accountName = "jeff111",
            nickname = "jeff111",
            avatar = "https://picsum.photos/200?random=13"
        ),
        Member(
            userId = Random.nextLong(),
            accountName = "jeff222",
            nickname = "jeff222",
            avatar = "https://picsum.photos/200?random=14"
        ),
        Member(
            userId = Random.nextLong(),
            accountName = "jeff333",
            nickname = "jeff333",
            avatar = "https://picsum.photos/200?random=15"
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
                    val tempInviteList = inviteList.filter {
                        it.nickname.contains(searchInputText.value)
                    }
                    Timber.d(tempInviteList.size.toString())
                    searchContactList.emit(tempInviteList)
                }
        }
    }
}