package com.example.crypto.views.fragments.mainScreen

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.crypto.model.api.CoinGeckoService
import com.example.crypto.model.api.responses.coinsList.Coin
import com.example.crypto.repository.STARTING_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException

class CoinsPagingSource(private val coinGeckoService: CoinGeckoService) :
    PagingSource<Int, Coin>() {
    override fun getRefreshKey(state: PagingState<Int, Coin>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Coin> {
        val pageIndex = params.key ?: STARTING_PAGE_INDEX
        return try {
            val coins = coinGeckoService.getTwentyCoins(pageIndex, params.loadSize).toList()
            val nextKey = if (coins.isEmpty()) {
                null
            } else pageIndex + 1
            LoadResult.Page(
                data = coins,
                prevKey = if (pageIndex == STARTING_PAGE_INDEX) null else pageIndex - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}