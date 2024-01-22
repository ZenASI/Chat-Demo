package com.chat.joycom.ui.commom

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.emoji2.emojipicker.EmojiPickerView
import androidx.emoji2.emojipicker.EmojiViewItem

// ref:https://issuetracker.google.com/issues/288261054
@Composable
fun Emoji2KeyBoard(onEmojiPickListener: (EmojiViewItem) -> Unit){
    val context = LocalContext.current
    AndroidView(
        factory = {
            EmojiPickerView(context = context)
        },
        update = { pickerView ->
            pickerView.setOnEmojiPickedListener {
                onEmojiPickListener.invoke(it)
            }
        },
        modifier = Modifier
            .fillMaxSize()
    )
}