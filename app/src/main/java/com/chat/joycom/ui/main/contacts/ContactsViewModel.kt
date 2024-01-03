package com.chat.joycom.ui.main.contacts

import androidx.compose.runtime.mutableStateListOf
import com.chat.joycom.model.Member
import com.chat.joycom.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class ContactsViewModel @Inject constructor() : BaseViewModel() {

    val onContactList = mutableStateListOf<Member>(
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

    val inviteList = mutableStateListOf<Member>(
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
}