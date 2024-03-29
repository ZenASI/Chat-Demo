package com.chat.joycom.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chat.joycom.model.Group.Companion.TABLE_NAME
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Entity(tableName = TABLE_NAME)
@Parcelize
@JsonClass(generateAdapter = true)
data class Group(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "avatar", typeAffinity = ColumnInfo.TEXT)
    @field:Json(name = "Avatar")
    val avatar: String,

    @ColumnInfo(name = "group_id", typeAffinity = ColumnInfo.INTEGER)
    @field:Json(name = "GroupId")
    val groupId: Long,

    @ColumnInfo(name = "group_name", typeAffinity = ColumnInfo.TEXT)
    @field:Json(name = "GroupName")
    val groupName: String,

    @ColumnInfo(name = "is_no_disturb", typeAffinity = ColumnInfo.INTEGER)
    @field:Json(name = "IsNoDisturb")
    val isNoDisturb: Int,

    @ColumnInfo(name = "user_id", typeAffinity = ColumnInfo.INTEGER)
    @field:Json(name = "UserId")
    val userId: Long,
): Parcelable  {
    companion object{
        const val TABLE_NAME = "GroupEntity"
    }
}
