package com.example.crypto.repository

import androidx.paging.PagingData
import com.example.crypto.model.api.responses.coins_list.Coin
import com.example.crypto.repository.interfaces.MainScreenRepInterface
import com.example.crypto.utils.testFlow
import kotlinx.coroutines.flow.Flow

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