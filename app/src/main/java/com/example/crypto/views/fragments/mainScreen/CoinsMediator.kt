package com.example.crypto.views.fragments.mainScreen

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.crypto.model.api.CoinGeckoService
import com.example.crypto.model.api.responses.coinsList.Coin
import com.example.crypto.model.db.CoinsListDataBase
import com.example.crypto.model.db.RemoteKeys
import com.example.crypto.repository.STARTING_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException


@ExperimentalPagingApi
class CoinsMediator(
    private val service: CoinGeckoService,
    private val coinsListDataBase: CoinsListDataBase
) : RemoteMediator<Int, Coin>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Coin>): MediatorResult {

        val pageKeyData = getKeyPageData(loadType, state)
        val page = when (pageKeyData) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }
        try {
            val response = service.getTwentyCoins(page, state.config.pageSize)
            val isEndOfList = response.isEmpty()
            coinsListDataBase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    coinsListDataBase.remoteKeysDao().clearRemoteKeys()
                    coinsListDataBase.coinsListDao().clearDb()
                }
                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = response.map {
                    RemoteKeys(repoId = it.coinId, prevKey = prevKey, nextKey = nextKey)
                }
                coinsListDataBase.remoteKeysDao().insertAll(keys)
                coinsListDataBase.coinsListDao().insertCoins(response)
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, Coin>) : Any? {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
        }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, Coin>): RemoteKeys? {
        return state.pages
            .firstOrNull() { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { coin ->
                coinsListDataBase.remoteKeysDao().remoteKeysCoinId(coin.coinId)
            }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, Coin>): RemoteKeys? {
        return state.pages
            .lastOrNull() { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { coin ->
                coinsListDataBase.remoteKeysDao().remoteKeysCoinId(coin.coinId)
            }
    }

    private suspend fun getClosestRemoteKey(state: PagingState<Int, Coin>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.coinId?.let { repoId ->
                coinsListDataBase.remoteKeysDao().remoteKeysCoinId(repoId)
            }
        }
    }

}