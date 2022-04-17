package com.example.crypto

import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ServiceTestRule
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class InternetCheckServiceTest{

    @get:Rule
    val serviceRule = ServiceTestRule()

    @Test
    fun testService() {
        val intentService = Intent(ApplicationProvider.getApplicationContext<Context>(), InternetCheckService::class.java)

        val binder: IBinder = serviceRule.bindService(intentService)
        val service: InternetCheckService = (binder as InternetCheckService.LocalBinder).getService()

        assertThat(service.sendIntentForBroadcast(), `is`(true))
    }
}