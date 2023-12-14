package com.chat.joycom.ui.commom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.delay

@Composable
fun OtpInput(
    isEnable: Boolean = false,
    otpText: String,
    updateOtp: (text: String) -> Unit,
    sentSms: () -> Unit
) {
    var timeLeft by rememberSaveable { mutableIntStateOf(60) }
    var isCounting by rememberSaveable { mutableStateOf(false) }
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
    ) {
        TextField(
            value = otpText,
            onValueChange = { updateOtp.invoke(it) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier,
            label = { Text(text = "otp", color = Color.Gray) }
        )
        Button(
            enabled = isEnable,
            onClick = {
                isCounting = true
                sentSms.invoke()
            },
            modifier = Modifier,
        ) {
            if (isCounting) {
                LaunchedEffect(key1 = timeLeft) {
                    if (timeLeft > 0) {
                        delay(1_000)
                        timeLeft -= 1
                    } else {
                        isCounting = false
                        timeLeft = 60
                    }
                }
                Text(text = "${timeLeft}s", maxLines = 1, textAlign = TextAlign.Center)
            } else {
                Text(text = "Send", maxLines = 1, textAlign = TextAlign.Center)
            }
        }
    }
}