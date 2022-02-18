package com.example.crypto.viewModels

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.crypto.model.api.responses.coinsList.Coin
import com.example.crypto.repository.CoinsListRepository
import kotlinx.coroutines.flow.*

class CoinsListViewModel(
    private val repository: CoinsListRepository
) : ViewModel() {

    fun getCoins(): Flow<PagingData<Coin>> =
        repository.getCoinsListResultStream().cachedIn(viewModelScope)

}

