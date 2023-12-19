package com.chat.joycom.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.chat.joycom.model.Message.Companion.TABLE_NAME
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

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
) {
    companion object {
        const val TABLE_NAME = "MessageEntity"
        fun getFakeMessage() =
            Message(
                atUserIds = "",
                autoDel = 0,
                content = "{\"AtUsers\":[],\"Text\":\"001\"}",
                encryptionType = 0,
                fromUserId = 10005,
                msgId = "812146817968934912",
                msgType = 1,
                replyMsgId = "0",
                sendStatus = 1,
                sendTicks = 1702611210000,
                sendTime = "2023-12-15 11:33:30",
                toGroupId = 0,
                toUserId = 10003
            )
    }

    @Ignore
    var isGroup: Boolean = toGroupId != 0L

    @Ignore
    var isSelect: Boolean = false

    @Ignore
    var showTopTime: Boolean = true
}
