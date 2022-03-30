package com.example.crypto.views

import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.crypto.CheckInternetBroadcastReceiver
import com.example.crypto.InternetCheckService
import com.example.crypto.R
import com.example.crypto.databinding.ActivityMainBinding
import com.example.crypto.model.constans.ACTION_STOP_SERVICE
import com.example.crypto.model.constans.BROADCAST_STRING_FOR_ACTION
import com.example.crypto.utils.isOnline
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.processphoenix.ProcessPhoenix
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity() {

    private val intentFilter: IntentFilter by inject()
    private lateinit var binding: ActivityMainBinding
    private lateinit var internetBroadcastReceiver: CheckInternetBroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomMenu()
        internetBroadcastReceiver = CheckInternetBroadcastReceiver(binding)
        intentFilter.addAction(BROADCAST_STRING_FOR_ACTION)
        val intentService = Intent(this, InternetCheckService::class.java)
        startService(intentService)

        //set listener for button on error screen
        binding.buttonTryAgain.setOnClickListener {
            if (isOnline(this)) {
                binding.groupNoInternetScreen.visibility = View.GONE
                binding.imageDarkOverlayBottomMenu.visibility = View.GONE
                ProcessPhoenix.triggerRebirth(this)
            } else {
                Snackbar.make(
                    it,
                    this.resources.getString(R.string.still_no_connection),
                    BaseTransientBottomBar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setBottomMenu() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.menu_bottom_nav)
        bottomNavigationView.setupWithNavController(navController)
    }


    override fun onRestart() {
        super.onRestart()
        registerReceiver(internetBroadcastReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(internetBroadcastReceiver)
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(internetBroadcastReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        val stopServiceIntent = Intent(this, InternetCheckService::class.java)
        stopServiceIntent.action = ACTION_STOP_SERVICE
        startService(stopServiceIntent)
    }

}