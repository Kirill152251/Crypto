package com.example.crypto

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.View
import com.example.crypto.databinding.ActivityMainBinding
import com.example.crypto.utils.isOnline
import com.example.crypto.views.MainActivity
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.processphoenix.ProcessPhoenix

class CheckInternetBroadcastReceiver(private val binding: ActivityMainBinding) :
    BroadcastReceiver() {
    @SuppressLint("InflateParams")
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.getStringExtra("online_status").equals("false")) {
            binding.apply {
                noInternetErrorScreen.visibility = View.VISIBLE
                darkOverLayContentBottomMenu.visibility = View.VISIBLE
                tryAgainBtn.setOnClickListener {
                    if (isOnline(context!!)) {
                        noInternetErrorScreen.visibility = View.GONE
                        darkOverLayContentBottomMenu.visibility = View.GONE
                        ProcessPhoenix.triggerRebirth(context)
                    } else {
                        Snackbar.make(
                            it,
                            context.resources.getString(R.string.still_no_connection),
                            LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}