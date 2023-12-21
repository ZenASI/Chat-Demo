package com.chat.joycom.ui.setting

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.chat.joycom.R

enum class SettingFeatures(
    @StringRes val title: Int,
    @StringRes val content: Int? = null,
    @DrawableRes val icon: Int,
) {
    Account(
        title = R.string.account,
        content = R.string.account_desc,
        icon = R.drawable.ic_def_group
    ),
    Privacy(
        title = R.string.privacy,
        content = R.string.privacy_desc,
        icon = R.drawable.ic_def_group
    ),
    Avatar(title = R.string.avatar, content = R.string.avatar_desc, icon = R.drawable.ic_def_group),
    Conversation(
        title = R.string.conversation,
        content = R.string.conversation_desc,
        icon = R.drawable.ic_def_group
    ),
    Notification(
        title = R.string.notification,
        content = R.string.notification_desc,
        icon = R.drawable.ic_def_group
    ),
    Storage(
        title = R.string.storage,
        content = R.string.storage_desc,
        icon = R.drawable.ic_def_group
    ),
    Language(
        title = R.string.language,
        content = R.string.language_desc,
        icon = R.drawable.ic_def_group
    ),
    Help(
        title = R.string.help,
        content = R.string.help_desc,
        icon = R.drawable.ic_def_group),
    Invite(
        title = R.string.invite_friend,
        icon = R.drawable.ic_def_group),
}