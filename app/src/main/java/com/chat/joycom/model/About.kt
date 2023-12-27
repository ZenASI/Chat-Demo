package com.chat.joycom.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chat.joycom.model.About.Companion.TABLE_NAME
import kotlinx.parcelize.Parcelize

@Entity(tableName = TABLE_NAME)
@Parcelize
class About(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "about_text", typeAffinity = ColumnInfo.TEXT)
    val aboutText: String,

    @ColumnInfo(name = "is_select", typeAffinity = ColumnInfo.INTEGER)
    val isSelect: Boolean,
) : Parcelable {
    companion object {
        const val TABLE_NAME = "AboutEntity"
    }
}