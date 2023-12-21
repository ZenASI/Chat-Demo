package com.chat.joycom.ui.main

import androidx.annotation.StringRes
import com.chat.joycom.R

enum class MainDropDown(
    @StringRes val itemName: Int,
) {
    CreateGroup(itemName = R.string.create_group),
    CreateBroadCast(itemName = R.string.create_broadcast),
    LinkedDevice(itemName = R.string.linked_device),
    MarkMessage(itemName = R.string.marked_msg),
    Setting(itemName = R.string.setting)
}