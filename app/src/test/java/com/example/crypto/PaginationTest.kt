package com.example.crypto

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.crypto.repository.NETWORK_PAGE_SIZE
import com.example.crypto.utils.TestPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test


@ExperimentalCoroutinesApi
class PaginationTest {

    private val testDispatcher = UnconfinedTestDispatcher()


    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }

    @Test
    fun testPagination(): Unit = runTest {
        val testSize = 10
        val pager = Pager(
            config = PagingConfig(
                pageSize = testSize,
                initialLoadSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { TestPagingSource() }
        ).flow
        //TODO: finish test
    }
}