package com.example.crypto

import android.app.Service
import android.content.Intent
import android.os.*
import com.example.crypto.model.constans.ACTION_STOP_SERVICE
import com.example.crypto.model.constans.BROADCAST_STRING_FOR_ACTION
import com.example.crypto.utils.isOnline


class InternetCheckService : Service() {

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                handler.postDelayed(this, 13000)
                val broadcastIntent = Intent()
                broadcastIntent.action = BROADCAST_STRING_FOR_ACTION
                broadcastIntent.putExtra("online_status", "" + isOnline(this@InternetCheckService.applicationContext))
                sendBroadcast(broadcastIntent)
            }
        })
        if (intent?.action == ACTION_STOP_SERVICE) {
            stopSelf()
        }
        return START_STICKY
    }
}