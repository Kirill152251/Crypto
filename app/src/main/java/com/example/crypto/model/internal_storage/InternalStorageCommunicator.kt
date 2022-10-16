package com.example.crypto.model.internal_storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.crypto.model.constans.PROFILE_PHOTO_NAME
import com.example.crypto.utils.Avatar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException


class InternalStorageCommunicator(private val context: Context) : StorageCommunicator {

    override fun deleteContentFromStorage() {
        context.deleteFile(PROFILE_PHOTO_NAME)
    }

    override suspend fun getContentFromStorage(): List<Avatar> {
        return withContext(Dispatchers.IO) {
            val files = context.filesDir.listFiles()
            files?.filter { it.name == PROFILE_PHOTO_NAME }?.map {
                val bytes = it.readBytes()
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                Avatar(it.name, bitmap)
            } ?: listOf()
        }
    }

    override suspend fun saveContentToStorage(content: Bitmap): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                context.openFileOutput(PROFILE_PHOTO_NAME, Context.MODE_PRIVATE)
                    .use { stream ->
                        if (!content.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                            throw IOException("Couldn't save bitmap")
                        }
                    }
                true
            } catch (e: IOException) {
                false
            }
        }
    }
}
