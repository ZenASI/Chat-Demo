package com.chat.joycom.ext

import android.util.Patterns
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.zip.CRC32

fun String.toSendTimeFormat(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("aHH:mm", Locale.getDefault())

    val date = inputFormat.parse(this)
    val calendar = Calendar.getInstance()
    calendar.time = date

    return outputFormat.format(date)
}

fun String.toTopTimeFormat(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val date = inputFormat.parse(this)

    val calendar = Calendar.getInstance()
    val currentWeek = calendar.get(Calendar.WEEK_OF_YEAR)
    calendar.time = date
    val targetWeek = calendar.get(Calendar.WEEK_OF_YEAR)

    return if (currentWeek == targetWeek) {
        SimpleDateFormat("EEEE", Locale.getDefault()).format(date)
    } else {
        SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(date)
    }
}

// timestamp + content + "asdfghjkl"
fun String.toCrc32Value(): Long {
    val crc32 = CRC32()
    crc32.update(this.toByteArray())
    return crc32.value
}

fun String.isValidEmail(): Boolean {
    return if (this.isEmpty()) {
        false
    } else {
        Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }
}