package com.chat.joycom.ext

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import com.chat.joycom.R

fun Context.startShareIntent(@StringRes contentResId: Int = R.string.share_content) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, getString(contentResId))
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(shareIntent)
}