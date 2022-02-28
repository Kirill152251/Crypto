package com.example.crypto.viewModels

import androidx.lifecycle.*
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.crypto.model.api.responses.coinsList.Coin
import com.example.crypto.model.constans.QUERY_SORT_BY_MARKET_CAP
import com.example.crypto.model.constans.QUERY_SORT_BY_PRICE
import com.example.crypto.model.constans.QUERY_SORT_BY_VOLATILITY
import com.example.crypto.repository.Repository
import kotlinx.coroutines.flow.*

class MainScreenViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _currentSorting = MutableStateFlow(QUERY_SORT_BY_MARKET_CAP)
    val currentSorting: StateFlow<String> = _currentSorting

    @OptIn(ExperimentalPagingApi::class)
    fun getCoins(order: String): Flow<PagingData<Coin>> =
        repository.getCoinsListFlow(order).cachedIn(viewModelScope)

    fun getCoinsByMarketCap(): Flow<PagingData<Coin>> {
        _currentSorting.value = QUERY_SORT_BY_MARKET_CAP
        return  repository.getCoinsByCap().cachedIn(viewModelScope)
    }

    fun getCoinsByPrice(): Flow<PagingData<Coin>> {
        _currentSorting.value = QUERY_SORT_BY_PRICE
        return  repository.getCoinsByPrice().cachedIn(viewModelScope)
    }

    fun getCoinsByVolatility(): Flow<PagingData<Coin>> {
        _currentSorting.value = QUERY_SORT_BY_VOLATILITY
        return  repository.getCoinsByVol().cachedIn(viewModelScope)
    }
}

