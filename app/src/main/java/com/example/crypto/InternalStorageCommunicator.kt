package com.example.crypto

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.crypto.model.constans.PROFILE_PHOTO_NAME
import com.example.crypto.utils.Avatar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException


class InternalStorageCommunicator(private val context: Context) {

    fun deleteAvatarFromInternalStorage() = context.deleteFile(PROFILE_PHOTO_NAME)

    suspend fun getAvatarFromStorage(): List<Avatar> {
        return withContext(Dispatchers.IO) {
            val files = context.filesDir.listFiles()
            files?.filter { it.name == PROFILE_PHOTO_NAME }?.map {
                val bytes = it.readBytes()
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                Avatar(it.name, bitmap)
            } ?: listOf()
        }
    }

    fun saveAvatarToStorage(bitmap: Bitmap): Boolean {
        return try {
            context.openFileOutput(PROFILE_PHOTO_NAME, Context.MODE_PRIVATE)
                .use { stream ->
                    if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                        throw IOException("Couldn't save bitmap")
                    }
                }
            true
        } catch (e: IOException) {
            false
        }
    }
}