package com.example.crypto

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.example.crypto.model.constans.ACTION_STOP_SERVICE
import com.example.crypto.model.constans.BROADCAST_STRING_FOR_ACTION
import com.example.crypto.model.constans.ONLINE_STATUS
import com.example.crypto.utils.isOnline
import java.lang.Exception
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate


class InternetCheckService : Service() {

    override fun onBind(p0: Intent?): IBinder {
        return LocalBinder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        sendIntentForBroadcast()
        if (intent?.action == ACTION_STOP_SERVICE) {
            stopSelf()
        }
        return START_STICKY
    }

    class LocalBinder : Binder() {
        fun getService() = InternetCheckService()
    }

    fun sendIntentForBroadcast(): Boolean {
        return try {
            Timer().scheduleAtFixedRate(1600, 10000){
                val broadcastIntent = Intent()
                broadcastIntent.action = BROADCAST_STRING_FOR_ACTION
                broadcastIntent.putExtra(ONLINE_STATUS, isOnline())
                sendBroadcast(broadcastIntent)
            }
            true
        } catch (e: Exception) {
            return false
        }
    }
}