package com.example.crypto.model.settings_db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.crypto.model.db.CoinsListDataBase
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

    private lateinit var db: CoinsListDataBase
    private lateinit var dao: UserInfoDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CoinsListDataBase::class.java
        ).allowMainThreadQueries().build()
        dao = db.userInfoDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndGetTheSameUserSettingsTest(): Unit = runBlocking {
        val firstUser = SettingsUserInfo("kirill", "test", "12/12/12")
        dao.insertUserInfo(firstUser)
        val infoFromBd = dao.getUserInfo()
        assertThat(infoFromBd, `is`(firstUser))
    }

    @Test
    fun gettingLastInsertUserSettingsYest(): Unit = runBlocking {
        val firstUser = SettingsUserInfo("kirill", "test", "12/12/12")
        val lastInsertUser = SettingsUserInfo("ivan", "test", "13/13/13")
        dao.insertUserInfo(firstUser)
        dao.insertUserInfo(lastInsertUser)
        val infoFromBd = dao.getUserInfo()
        assertThat(infoFromBd, `is`(lastInsertUser))
    }
}
