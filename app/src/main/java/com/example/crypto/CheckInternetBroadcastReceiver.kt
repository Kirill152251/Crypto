package com.example.crypto

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.View
import com.example.crypto.databinding.ActivityMainBinding
import com.example.crypto.model.constans.ONLINE_STATUS

class CheckInternetBroadcastReceiver(private val binding: ActivityMainBinding) :
    BroadcastReceiver() {
    @SuppressLint("InflateParams")
    override fun onReceive(context: Context?, intent: Intent?) {
        val isNetworkAvailable = intent?.getBooleanExtra(ONLINE_STATUS, true) ?: true
        if (!isNetworkAvailable) {
            binding.apply {
                groupNoInternetScreen.visibility = View.VISIBLE
                imageDarkOverlayBottomMenu.visibility = View.VISIBLE
            }
        }
    }
}