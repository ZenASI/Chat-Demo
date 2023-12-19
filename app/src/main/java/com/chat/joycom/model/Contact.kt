package com.chat.joycom.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chat.joycom.model.Contact.Companion.TABLE_NAME
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Entity(tableName = TABLE_NAME)
@Parcelize
@JsonClass(generateAdapter = true)
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "user_id", typeAffinity = ColumnInfo.INTEGER)
    @field:Json(name = "UserId")
    val userId: Long,

    @ColumnInfo(name = "account", typeAffinity = ColumnInfo.TEXT)
    @field:Json(name = "Account") val account: String,

    @ColumnInfo(name = "nick_name", typeAffinity = ColumnInfo.TEXT)
    @field:Json(name = "Nickname")
    val nickname: String,

    @ColumnInfo(name = "avatar", typeAffinity = ColumnInfo.TEXT)
    @field:Json(name = "Avatar")
    val avatar: String,

    @ColumnInfo(name = "country_code", typeAffinity = ColumnInfo.TEXT)
    @field:Json(name = "CountryCode")
    val countryCode: String,

    @ColumnInfo(name = "phone_number", typeAffinity = ColumnInfo.TEXT)
    @field:Json(name = "PhoneNumber")
    val phoneNumber: String,

    @ColumnInfo(name = "friend_remark", typeAffinity = ColumnInfo.TEXT)
    @field:Json(name = "FriendRemark")
    val friendRemark: String?,

    @ColumnInfo(name = "is_no_disturb", typeAffinity = ColumnInfo.INTEGER)
    @field:Json(name = "IsNoDisturb")
    val isNoDisturb: Int,
) : Parcelable {
    companion object{
        const val TABLE_NAME = "ContactEntity"
    }
}
