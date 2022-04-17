package com.example

import androidx.paging.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.crypto.databinding.CoinsListItemBinding
import com.example.crypto.model.api.responses.coins_list.Coin
import com.example.crypto.model.constans.*
import com.example.crypto.views.fragments.main_screen.CoinsListAdapter
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class PaginationTest {

    @Test
    fun shouldConsistOfPageSizeElement() = runBlocking {

        val adapter = CoinsListAdapter() { coinItem, binding ->
            itemClickListener(coinItem, binding)
        }

        val test = getTestPager()
        val job = launch {
            adapter.submitData(test.first())
        }
        delay(100)
        job.cancel()
        val actualSize = adapter.itemCount
        assertEquals(NETWORK_PAGE_SIZE, actualSize)
    }

    private fun itemClickListener(coinItem: Coin, binding: CoinsListItemBinding) {}

    private fun getTestPager(): Flow<PagingData<Coin>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                initialLoadSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MockCoinsPagingSource()
            }
        ).flow
    }

    class MockCoinsPagingSource : PagingSource<Int, Coin>() {

        override fun getRefreshKey(state: PagingState<Int, Coin>): Int? {
            return state.anchorPosition?.let { anchorPosition ->
                state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                    ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
            }
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Coin> {
            val pageIndex = params.key ?: STARTING_PAGE_INDEX
            return try {
                val response = getMockPage(params.loadSize)
                LoadResult.Page(
                    data = response,
                    prevKey = if (pageIndex == 1) null else pageIndex - 1,
                    nextKey = if (response.size == params.loadSize) pageIndex + 1 else null
                )
            } catch (e: Exception) {
                return LoadResult.Error(e)
            }
        }

        private fun getMockPage(pageSize: Int): List<Coin> {
            val list = mutableListOf<Coin>()
            repeat(pageSize) {
                list.add(Coin(0.0, "1", "777", "777", "777", 777, 777.0, 0f))
            }
            return list
        }
    }
}