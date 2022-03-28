package com.example.crypto.model.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.crypto.model.api.responses.coinsList.Coin
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class CoinsListDataBaseTest {

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: CoinsListDataBase
    private lateinit var dao: CoinsListDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CoinsListDataBase::class.java
        ).allowMainThreadQueries().build()
        dao = db.coinsListDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testReplacement(): Unit = runBlocking {
        val testCoins = listOf<Coin>(
            Coin("777","1","777", "777", "777", 777, "777",777.0),
            Coin("777","2","777", "777", "777", 777, "777",777.0),
            Coin("777","3","777", "777", "777", 777, "777",777.0),
            Coin("777","4","777", "777", "777", 777, "777",777.0),
            Coin("777","5","777", "777", "777", 777, "777",777.0),
            Coin("777","6","777", "777", "777", 777, "777",777.0)
        )
        dao.insertCoins(testCoins)
        val coinsNumberAfterFirstInsert = dao.getCoinsList().size
        dao.insertCoins(testCoins)
        val coinsNumberAfterSecondInsert = dao.getCoinsList().size
        assertThat(coinsNumberAfterFirstInsert, `is`(coinsNumberAfterSecondInsert))
    }
}