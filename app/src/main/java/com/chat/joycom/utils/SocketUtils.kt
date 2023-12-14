package com.chat.joycom.utils

import com.chat.joycom.network.SocketOkhttp
import com.chat.joycom.network.UrlPath
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocketUtils @Inject constructor(@SocketOkhttp private val okHttpClient: OkHttpClient) :
    WebSocketListener() {
    private var webSocket: WebSocket? = null

    fun connect() {
        val request: Request = Request.Builder()
            .url(UrlPath.getSocketFullUrl())
            .build()
        Timber.d("connect url => ${UrlPath.getSocketFullUrl()}")
        webSocket = okHttpClient.newWebSocket(request, this)
    }

    fun disConnect() {
        webSocket?.cancel()
    }

    fun send(text: String) {
        Timber.d("send => $text")
        webSocket?.send(text)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        Timber.d("onClosed code => $code, reason => $reason")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        Timber.d("onClosing code => $code, reason => $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        t.printStackTrace()
        Timber.d("onFailure ${t.message}, response $response")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        Timber.d("onMessage text $text")
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        super.onMessage(webSocket, bytes)
        Timber.d("onMessage bytes $bytes")
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        Timber.d("onOpen")
    }
}