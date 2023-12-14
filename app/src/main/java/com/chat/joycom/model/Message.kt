package com.chat.joycom.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.chat.joycom.model.Message.Companion.TABLE_NAME
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = TABLE_NAME)
@JsonClass(generateAdapter = true)
data class Message(
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
    val fromUserId: Int,

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
    val sendTicks: Int,

    @ColumnInfo(name = "send_time", typeAffinity = ColumnInfo.INTEGER)
    @field:Json(name = "SendTime")
    val sendTime: Int,

    @ColumnInfo(name = "to_group_id", typeAffinity = ColumnInfo.INTEGER)
    @field:Json(name = "ToGroupId")
    val toGroupId: Int,

    @ColumnInfo(name = "to_user_id", typeAffinity = ColumnInfo.INTEGER)
    @field:Json(name = "ToUserId")
    val toUserId: Int,
){
    companion object{
        const val TABLE_NAME = "MessageEntity"
    }
}
