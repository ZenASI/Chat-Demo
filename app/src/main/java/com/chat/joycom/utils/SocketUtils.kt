package com.chat.joycom.utils

import androidx.datastore.preferences.core.longPreferencesKey
import com.chat.joycom.ds.DSKey
import com.chat.joycom.flow.MessageFlow
import com.chat.joycom.model.Message
import com.chat.joycom.network.SocketOkhttp
import com.chat.joycom.network.UrlPath
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class SocketUtils @Inject constructor(
    @SocketOkhttp private val okHttpClient: OkHttpClient,
    private val jsonUtils: JsonUtils,
    private val roomUtils: RoomUtils,
    private val dataStoreUtils: DataStoreUtils
) : WebSocketListener(), CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    private var webSocket: WebSocket? = null

    fun connect() {
        val request: Request = Request.Builder()
            .url(UrlPath.getSocketFullUrl())
            .build()
        Timber.d("connect url => ${UrlPath.getSocketFullUrl()}")
        webSocket = okHttpClient.newWebSocket(request, this)
    }

    fun disConnect() {
        job.cancel()
        webSocket?.cancel()
    }

    fun send(text: String) {
        Timber.d("send => $text")
        webSocket?.send(text)
    }

    fun send(message: Message) {
        Timber.d("send => $message")
        webSocket?.send(jsonUtils.toJson(message))
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
        try {
            val jsonObject = JSONObject(text)
            when (jsonObject.getInt("Type")) {
                1 -> {
                    val jsonArray = jsonObject.getJSONArray("Data")

                    val list = jsonUtils.fromJson<Message>(jsonArray.toString())
                    launch {
                        list.forEach {
                            Timber.d("onMessage item $it")
                            dataStoreUtils.saveDataStoreValue(
                                longPreferencesKey(DSKey.LAST_ACK_ID),
                                it.msgId.toLong()
                            )
                            if (it.msgType in 1..5) {
                                roomUtils.insertMessage(it)
                            }
                            MessageFlow.updateValue(it)
                        }
                    }
                }

                else -> {

                    Timber.d("onMessage else $text")
                }
            }

        } catch (e: Throwable) {
            Timber.d("onMessage text error ${e.message}")
            e.printStackTrace()
        }
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