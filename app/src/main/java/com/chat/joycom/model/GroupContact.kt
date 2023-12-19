package com.chat.joycom.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.chat.joycom.model.GroupContact.Companion.TABLE_NAME
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Entity(tableName = TABLE_NAME, primaryKeys = ["group_id", "user_id"])
@Parcelize
@JsonClass(generateAdapter = true)
data class GroupContact(
    @ColumnInfo(name = "group_id", typeAffinity = ColumnInfo.INTEGER)
    var groupId: Long = 0L,

    @ColumnInfo(name = "user_id", typeAffinity = ColumnInfo.INTEGER)
    @field:Json(name = "UserId")
    val userId: Long,

    @ColumnInfo(name = "group_nickname", typeAffinity = ColumnInfo.TEXT)
    @field:Json(name = "GroupNickname")
    val groupNickname: String?,

    @ColumnInfo(name = "nick_name", typeAffinity = ColumnInfo.TEXT)
    @field:Json(name = "Nickname")
    val nickname: String,

    @ColumnInfo(name = "avatar", typeAffinity = ColumnInfo.TEXT)
    @field:Json(name = "Avatar")
    val avatar: String,

    @ColumnInfo(name = "status", typeAffinity = ColumnInfo.INTEGER)
    @field:Json(name = "Status")
    val status: Int,

    @ColumnInfo(name = "is_master", typeAffinity = ColumnInfo.INTEGER)
    @field:Json(name = "IsMaster")
    val isMaster: Boolean,
) : Parcelable {
    companion object{
        const val TABLE_NAME = "GroupContactEntity"
    }
}
