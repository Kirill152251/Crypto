package com.example.crypto.model.settingsDB

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.crypto.R

@Entity(tableName = "user_info")
data class SettingsUserInfo(
    val firstName: String,
    val lastName: String,
    val dateOfBirth: String,
    val profilePicture: Bitmap?,
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1
)

