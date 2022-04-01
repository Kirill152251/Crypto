package com.example.crypto.view_models

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.crypto.model.constans.SortBy
import com.example.crypto.repository.interfaces.MainScreenRepository
import com.example.crypto.repository.interfaces.SortPreferencesRepository
import com.example.crypto.views.fragments.main_screen.MainScreenContract.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val repository: MainScreenRepository,
    private val sortPreferencesRepository: SortPreferencesRepository
) : BaseViewModel<State, Event, Effect>() {

    override fun createInitialState(): State {
        return State(
            RecycleViewState.Loading
        )
    }

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.ChoseSortingByMarketCap -> {
                sortingCoins(SortBy.MARKET_CAP)
            }
            is Event.ChoseSortingByPrice -> {
                sortingCoins(SortBy.PRICE)
            }
            is Event.ChoseSortingByVolatility -> {
                sortingCoins(SortBy.VOLATILITY)
            }
            is Event.FetchFromDb -> {
                fetchCoinFromDb()
            }
            is Event.SaveSortingType -> {
                saveSortingTypeIntoDataStore(event.sortType)
            }
        }
    }

    suspend fun getSortingTypeFromDataStore(): String = sortPreferencesRepository.getOrder()

    private fun sortingCoins(sortBy: SortBy) {
        viewModelScope.launch {
            setState { copy(recycleViewState = RecycleViewState.Loading) }
            try {
                when (sortBy) {
                    SortBy.PRICE -> {
                        val coins = repository.getCoinsByPrice().cachedIn(viewModelScope).stateIn(
                            viewModelScope,
                            SharingStarted.WhileSubscribed(5000),
                            PagingData.empty()
                        )
                        setState {
                            copy(
                                recycleViewState = RecycleViewState.SortingByPrice(
                                    coins
                                )
                            )
                        }
                    }
                    SortBy.MARKET_CAP -> {
                        val coins = repository.getCoinsByCap().cachedIn(viewModelScope).stateIn(
                            viewModelScope,
                            SharingStarted.WhileSubscribed(5000),
                            PagingData.empty()
                        )
                        setState {
                            copy(
                                recycleViewState = RecycleViewState.SortingByMarketCap(
                                    coins
                                )
                            )
                        }
                    }
                    SortBy.VOLATILITY -> {
                        val coins = repository.getCoinsByVol().cachedIn(viewModelScope).stateIn(
                            viewModelScope,
                            SharingStarted.WhileSubscribed(5000),
                            PagingData.empty()
                        )
                        setState {
                            copy(
                                recycleViewState = RecycleViewState.SortingByVolatility(
                                    coins
                                )
                            )
                        }
                    }
                }
            } catch (exception: Exception) {
                Log.e("mainViewModel", exception.toString())
            }
        }
    }

    private fun fetchCoinFromDb() {
        viewModelScope.launch {
            setState { copy(recycleViewState = RecycleViewState.Loading) }
            val coins = repository.getCoinsFromDB().stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                PagingData.empty()
            )
            setState {
                copy(
                    recycleViewState = RecycleViewState.ItemsFromDb(
                        coins
                    )
                )
            }
        }
    }

    private fun saveSortingTypeIntoDataStore(sortBy: String) {
        viewModelScope.launch(Dispatchers.IO) {
            sortPreferencesRepository.saveOrder(sortBy)
        }
    }
}

