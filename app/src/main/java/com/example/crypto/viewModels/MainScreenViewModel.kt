package com.example.crypto.viewModels

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.crypto.model.api.responses.coinsList.Coin
import com.example.crypto.model.constans.QUERY_SORT_BY_MARKET_CAP
import com.example.crypto.model.constans.QUERY_SORT_BY_PRICE
import com.example.crypto.model.constans.QUERY_SORT_BY_VOLATILITY
import com.example.crypto.repository.Repository
import com.example.crypto.repository.SortPreferencesRepository
import com.example.crypto.views.fragments.mainScreen.MainScreenContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val repository: Repository,
    private val sortPreferencesRepository: SortPreferencesRepository
) : BaseViewModel<MainScreenContract.State, MainScreenContract.Event, MainScreenContract.Effect>() {

    override fun createInitialState(): MainScreenContract.State {
        return MainScreenContract.State(
            MainScreenContract.RecycleViewState.Loading
        )
    }

    override fun handleEvent(event: MainScreenContract.Event) {
        when (event) {
            is MainScreenContract.Event.ChoseSortingByMarketCap -> {
                sortByMarketCap()
            }
            is MainScreenContract.Event.ChoseSortingByPrice -> {
                sortByPrice()
            }
            is MainScreenContract.Event.ChoseSortingByVolatility -> {
                sortByVolatility()
            }
        }
    }

    suspend fun saveSortingIntoDataStore(sortBy: String) {
        sortPreferencesRepository.saveOrder(sortBy)
    }

    suspend fun getSortingFromDataStore(): String = sortPreferencesRepository.getOrder()

    private fun sortByVolatility() {
        viewModelScope.launch {
            setState { copy(recycleViewState = MainScreenContract.RecycleViewState.Loading) }
            try {
                val coins = repository.getCoinsByVol().cachedIn(viewModelScope)
                setState {
                    copy(
                        recycleViewState = MainScreenContract.RecycleViewState.SortingByVolatility(
                            coins
                        )
                    )
                }
            } catch (exception: Exception) {
                //TODO: error handling
            }
        }
    }

    private fun sortByPrice() {
        viewModelScope.launch {
            setState { copy(recycleViewState = MainScreenContract.RecycleViewState.Loading) }
            try {
                val coins = repository.getCoinsByPrice().cachedIn(viewModelScope)
                setState {
                    copy(
                        recycleViewState = MainScreenContract.RecycleViewState.SortingByPrice(
                            coins
                        )
                    )
                }
            } catch (exception: Exception) {
                //TODO: error handling
            }

        }
    }

    private fun sortByMarketCap() {
        viewModelScope.launch {
            setState { copy(recycleViewState = MainScreenContract.RecycleViewState.Loading) }
            try {
                val coins = repository.getCoinsByCap().cachedIn(viewModelScope)
                setState {
                    copy(
                        recycleViewState = MainScreenContract.RecycleViewState.SortingByMarketCap(
                            coins
                        )
                    )
                }
            } catch (exception: Exception) {
                //TODO: error handling
            }
        }
    }
}

