package com.example.crypto.views.fragments.main_screen

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.crypto.model.api.CoinGeckoService
import com.example.crypto.model.api.responses.coins_list.Coin
import com.example.crypto.model.constans.*
import com.example.crypto.model.db.CoinsListDataBase

class CoinsPagingSource(
    private val coinsGeckoService: CoinGeckoService,
    private val database: CoinsListDataBase,
    private val sourceType: PagingSourceType
) : PagingSource<Int, Coin>() {

    override fun getRefreshKey(state: PagingState<Int, Coin>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Coin> {
        val pageIndex = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = when (sourceType) {
                PagingSourceType.FROM_DB -> database.coinsListDao().getCoinsList()
                PagingSourceType.SORT_BY_MARKET_CAP -> coinsGeckoService.getCoinsSortedByMarketCap(
                    pageIndex,
                    params.loadSize,
                    QUERY_SORT_BY_MARKET_CAP
                )
                PagingSourceType.SORT_BY_PRICE -> coinsGeckoService.getCoinsSortedByPrice(
                    pageIndex,
                    params.loadSize,
                    QUERY_SORT_BY_PRICE
                )
                PagingSourceType.SORT_BY_VOLATILITY -> coinsGeckoService.getCoinsSortedByVolatility(
                    pageIndex,
                    params.loadSize,
                    QUERY_SORT_BY_VOLATILITY
                )
            }
            LoadResult.Page(
                data = response,
                prevKey = if (pageIndex == STARTING_PAGE_INDEX) null else pageIndex - 1,
                nextKey = if (response.size == params.loadSize) pageIndex + 1 else null
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}