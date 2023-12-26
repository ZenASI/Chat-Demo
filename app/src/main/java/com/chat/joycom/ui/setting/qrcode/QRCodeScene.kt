package com.chat.joycom.ui.setting.qrcode

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import com.chat.joycom.R

enum class QRCodeScene(
    @StringRes val title: Int,
    val body: @Composable () -> Unit = {}
) {
    MyQrCode(title = R.string.my_qrcode, body = { MyQrcode() }),
    ScanQrCode(title = R.string.scan_qrcode, body = { ScanQrcode() })
}