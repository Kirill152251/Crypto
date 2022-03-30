package com.example.crypto.views.fragments.main_screen

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.crypto.model.api.responses.coins_list.Coin
import com.example.crypto.model.constans.STARTING_PAGE_INDEX
import com.example.crypto.model.db.CoinsListDataBase
import retrofit2.HttpException
import java.io.IOException

class CoinsPagingSourceFromDb (
    private val database: CoinsListDataBase
) :
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
            val response = database.coinsListDao().getCoinsList()
            LoadResult.Page(
                data = response,
                prevKey = if (pageIndex == STARTING_PAGE_INDEX) null else pageIndex - 1,
                nextKey = if (response.size == params.loadSize) pageIndex + 1 else null
            )

        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}