package com.example.crypto.view_models.interfaces

import android.graphics.Bitmap
import com.example.crypto.model.settings_db.SettingsUserInfo

interface SettingsScreenVM {

    fun saveAvatar(bitmap: Bitmap?)

    fun insertInfo(settingsUserInfo: SettingsUserInfo)

    fun getInfo()

    fun isInputValid(firstName: String, lastName: String): Boolean
}