package com.example.crypto.model.settingsDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_info")
data class SettingsUserInfo(
    val firstName: String,
    val lastName: String,
    val dateOfBirth: String,
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1
)
