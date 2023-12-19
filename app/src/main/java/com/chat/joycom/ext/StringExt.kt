package com.chat.joycom.ext

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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