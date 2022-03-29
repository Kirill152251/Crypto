package com.example.crypto.model.settings_db

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.crypto.model.constans.USER_INFO_DATABASE_NAME

@Entity(tableName = USER_INFO_DATABASE_NAME)
data class SettingsUserInfo(
    val firstName: String,
    val lastName: String,
    val dateOfBirth: String,
    val profilePicture: Bitmap?,
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1
)

