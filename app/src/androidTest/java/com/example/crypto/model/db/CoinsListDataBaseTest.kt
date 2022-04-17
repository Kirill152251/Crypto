package com.example.crypto.model.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
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

    private lateinit var db: AppDataBase
    private lateinit var dao: CoinsListDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDataBase::class.java
        ).allowMainThreadQueries().build()
        dao = db.coinsListDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testReplacement(): Unit = runBlocking {
        val testCoins = listOf<CoinEntity>(
            CoinEntity(777.0,"1","777", "777", "777", 777, 777.0,777.0f),
            CoinEntity(777.0,"1","777", "777", "777", 777, 777.0,777.0f),
            CoinEntity(777.0,"1","777", "777", "777", 777, 777.0,777.0f),
            CoinEntity(777.0,"1","777", "777", "777", 777, 777.0,777.0f),
        )
        dao.insertCoins(testCoins)
        val coinsNumberAfterFirstInsert = dao.getCoinsList().size
        dao.insertCoins(testCoins)
        val coinsNumberAfterSecondInsert = dao.getCoinsList().size
        assertThat(coinsNumberAfterFirstInsert, `is`(coinsNumberAfterSecondInsert))
    }
}