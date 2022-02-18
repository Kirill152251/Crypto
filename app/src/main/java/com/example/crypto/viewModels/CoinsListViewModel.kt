package com.example.crypto.viewModels

import androidx.lifecycle.*
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.crypto.model.api.responses.coinsList.Coin
import com.example.crypto.repository.CoinsListRepository
import kotlinx.coroutines.flow.*

class CoinsListViewModel(
    private val repository: CoinsListRepository
) : ViewModel() {

    @OptIn(ExperimentalPagingApi::class)
    fun getCoins(): Flow<PagingData<Coin>> =
        repository.getCoinsListFlowFromDb().cachedIn(viewModelScope)
}

