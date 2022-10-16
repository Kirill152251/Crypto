package com.example.crypto.model.internal_storage

import android.graphics.Bitmap
import com.example.crypto.utils.Avatar

interface StorageCommunicator {

    fun deleteContentFromStorage()

    suspend fun getContentFromStorage(): List<Avatar>

    suspend fun saveContentToStorage(content: Bitmap): Boolean
}