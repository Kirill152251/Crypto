package com.example.utils

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.crypto.model.api.responses.coins_list.Coin
import com.example.crypto.model.constans.STARTING_PAGE_INDEX


class TestPagingSource : PagingSource<Int, Coin>() {

    override fun getRefreshKey(state: PagingState<Int, Coin>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Coin> {
        val pageIndex = params.key ?: STARTING_PAGE_INDEX
        val response = mockResponse(params.loadSize)
        return LoadResult.Page(
            data = response,
            prevKey = if (pageIndex == STARTING_PAGE_INDEX) null else pageIndex - 1,
            nextKey = if (response.size == params.loadSize) pageIndex + 1 else null
        )
    }

    private fun mockResponse(itemPerPage: Int): ArrayList<Coin> {
        val response = ArrayList<Coin>()
        for (i in 0 until itemPerPage) {
            response.add(createTestCoin())
        }
        return response
    }

    private fun createTestCoin(): Coin {
        return Coin(777.0, "1", "777", "777", "777", 777, 777.0, 777.0f)
    }
}