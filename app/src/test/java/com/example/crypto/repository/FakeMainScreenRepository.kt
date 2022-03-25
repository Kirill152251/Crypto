package com.example.crypto.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.crypto.model.api.responses.coinsList.Coin
import com.example.crypto.repository.interfaces.MainScreenRepInterface
import com.example.crypto.utils.TestPagingSource
import com.example.crypto.utils.testFlow
import com.example.crypto.views.fragments.mainScreen.CoinsPagingSourceByPrice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList

class FakeMainScreenRepository: MainScreenRepInterface {

    override fun getCoinsByPrice(): Flow<PagingData<Coin>> {
//        return Pager(
//            config = PagingConfig(
//                pageSize = NETWORK_PAGE_SIZE,
//                initialLoadSize = NETWORK_PAGE_SIZE,
//                enablePlaceholders = false
//            ),
//            pagingSourceFactory = { TestPagingSource() }
//        ).flow
        return testFlow
    }

    override fun getCoinsByCap(): Flow<PagingData<Coin>> {
//        return Pager(
//            config = PagingConfig(
//                pageSize = NETWORK_PAGE_SIZE,
//                initialLoadSize = NETWORK_PAGE_SIZE,
//                enablePlaceholders = false
//            ),
//            pagingSourceFactory = { TestPagingSource() }
//        ).flow
        return testFlow
    }

    override fun getCoinsByVol(): Flow<PagingData<Coin>> {
//        return Pager(
//            config = PagingConfig(
//                pageSize = NETWORK_PAGE_SIZE,
//                initialLoadSize = NETWORK_PAGE_SIZE,
//                enablePlaceholders = false
//            ),
//            pagingSourceFactory = { TestPagingSource() }
//        ).flow
        return testFlow
    }

    override fun getCoinsFromDB(): Flow<PagingData<Coin>> {
//        return Pager(
//            config = PagingConfig(
//                pageSize = NETWORK_PAGE_SIZE,
//                initialLoadSize = NETWORK_PAGE_SIZE,
//                enablePlaceholders = false
//            ),
//            pagingSourceFactory = { TestPagingSource() }
//        ).flow
        return testFlow
    }
}