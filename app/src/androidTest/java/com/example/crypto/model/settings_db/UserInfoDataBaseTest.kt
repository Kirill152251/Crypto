package com.example.crypto.model.settings_db

import android.graphics.Bitmap
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@SmallTest
@RunWith(AndroidJUnit4::class)
class UserInfoDataBaseTest {

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: UserInfoDataBase
    private lateinit var dao: UserInfoDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            UserInfoDataBase::class.java
        ).allowMainThreadQueries().build()
        dao = db.userInfoDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndGetTheSameUserSettingsTest(): Unit = runBlocking {
        val conf = Bitmap.Config.ARGB_8888
        val emptyBitmap = Bitmap.createBitmap(5, 5, conf)
        val firstUser = SettingsUserInfo("kirill", "test", "12/12/12", emptyBitmap)
        dao.insertUserInfo(firstUser)
        val infoFromBd = dao.getUserInfo().asLiveData().getOrAwaitValue()
        assertThat(infoFromBd, `is`(firstUser))
    }

    @Test
    fun gettingLastInsertUserSettingsYest(): Unit = runBlocking {
        val conf = Bitmap.Config.ARGB_8888
        val emptyBitmap = Bitmap.createBitmap(5, 5, conf)
        val firstUser = SettingsUserInfo("kirill", "test", "12/12/12", emptyBitmap)
        val lastInsertUser = SettingsUserInfo("ivan", "test", "13/13/13", emptyBitmap)
        dao.insertUserInfo(firstUser)
        dao.insertUserInfo(lastInsertUser)
        val infoFromBd = dao.getUserInfo().asLiveData().getOrAwaitValue()
        assertThat(infoFromBd, `is`(lastInsertUser))
    }
}
