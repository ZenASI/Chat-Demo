package com.chat.joycom.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.chat.joycom.model.Message.Companion.TABLE_NAME
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import kotlin.random.Random

@Parcelize
@Entity(tableName = TABLE_NAME, indices = [Index(value = ["msg_id"], unique = true)])
@JsonClass(generateAdapter = true)
data class Message(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id", typeAffinity = ColumnInfo.INTEGER)
    val id: Long = 0L,

    @ColumnInfo(name = "at_user_ids", typeAffinity = ColumnInfo.TEXT)
    @field:Json(name = "AtUserIds")
    val atUserIds: String,

    @ColumnInfo(name = "auto_del", typeAffinity = ColumnInfo.INTEGER)
    @field:Json(name = "AutoDel")
    val autoDel: Int,

    @ColumnInfo(name = "content", typeAffinity = ColumnInfo.TEXT)
    @field:Json(name = "Content")
    val content: String,

    @ColumnInfo(name = "encryption_type", typeAffinity = ColumnInfo.INTEGER)
    @field:Json(name = "EncryptionType")
    val encryptionType: Int,

    @ColumnInfo(name = "from_user_id", typeAffinity = ColumnInfo.INTEGER)
    @field:Json(name = "FromUserId")
    val fromUserId: Long,

    @ColumnInfo(name = "msg_id", typeAffinity = ColumnInfo.TEXT)
    @field:Json(name = "MsgId")
    val msgId: String,

    @ColumnInfo(name = "msg_type", typeAffinity = ColumnInfo.INTEGER)
    @field:Json(name = "MsgType")
    val msgType: Int,

    @ColumnInfo(name = "reply_msg_id", typeAffinity = ColumnInfo.TEXT)
    @field:Json(name = "ReplyMsgId")
    val replyMsgId: String,

    @ColumnInfo(name = "send_status", typeAffinity = ColumnInfo.INTEGER)
    @field:Json(name = "SendStatus")
    val sendStatus: Int,

    @ColumnInfo(name = "send_ticks", typeAffinity = ColumnInfo.INTEGER)
    @field:Json(name = "SendTicks")
    val sendTicks: Long,

    @ColumnInfo(name = "send_time", typeAffinity = ColumnInfo.TEXT)
    @field:Json(name = "SendTime")
    val sendTime: String,

    @ColumnInfo(name = "to_group_id", typeAffinity = ColumnInfo.INTEGER)
    @field:Json(name = "ToGroupId")
    val toGroupId: Long,

    @ColumnInfo(name = "to_user_id", typeAffinity = ColumnInfo.INTEGER)
    @field:Json(name = "ToUserId")
    val toUserId: Long,
) : Parcelable {
    companion object {
        const val TABLE_NAME = "MessageEntity"

        fun getFakeMsg(isGroup:Boolean = false, msgType: Int = 2) = Message(
            id = Random.nextLong(),
            atUserIds = "",
            autoDel = 0,
            content = "Random Long => ${Random.nextLong()}",
            encryptionType = 0,
            fromUserId = Random.nextLong(),
            msgId = Random.nextLong().toString(),
            msgType = msgType,
            replyMsgId = "0",
            sendStatus = 0,
            sendTicks = System.currentTimeMillis(),
            sendTime = "${SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(System.currentTimeMillis())}",
            toGroupId = if (isGroup) 123 else 0,
            toUserId = if (isGroup) 0 else 456
        )
    }

    @Ignore
    var isGroup: Boolean = toGroupId != 0L

    @Ignore
    var isSelect: Boolean = false

    @Ignore
    var showIcon = true
}
