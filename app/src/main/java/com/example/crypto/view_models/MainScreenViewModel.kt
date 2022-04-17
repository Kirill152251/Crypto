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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
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
            try {
                when (sortBy) {
                    SortBy.PRICE -> {
                        repository.getCoinsByPrice().cachedIn(viewModelScope).distinctUntilChanged()
                            .stateIn(
                                viewModelScope,
                                SharingStarted.WhileSubscribed(5000),
                                PagingData.empty()
                            ).collectLatest {
                                setState {
                                    copy(
                                        recycleViewState = RecycleViewState.SortingByPrice(
                                            it
                                        )
                                    )
                                }
                            }
                    }
                    SortBy.MARKET_CAP -> {
                        repository.getCoinsByCap().cachedIn(viewModelScope).distinctUntilChanged()
                            .stateIn(
                                viewModelScope,
                                SharingStarted.WhileSubscribed(5000),
                                PagingData.empty()
                            ).collectLatest {
                                setState {
                                    copy(
                                        recycleViewState = RecycleViewState.SortingByMarketCap(
                                            it
                                        )
                                    )
                                }
                            }
                    }
                    SortBy.VOLATILITY -> {
                        repository.getCoinsByVol().cachedIn(viewModelScope).distinctUntilChanged()
                            .stateIn(
                                viewModelScope,
                                SharingStarted.WhileSubscribed(5000),
                                PagingData.empty()
                            ).collectLatest {
                                setState {
                                    copy(
                                        recycleViewState = RecycleViewState.SortingByVolatility(
                                            it
                                        )
                                    )
                                }
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
            repository.getCoinsFromDB().cachedIn(viewModelScope).distinctUntilChanged()
                .stateIn(
                    viewModelScope,
                    SharingStarted.WhileSubscribed(5000),
                    PagingData.empty()
                ).collectLatest {
                    setState {
                        copy(
                            recycleViewState = RecycleViewState.ItemsFromDb(
                                it
                            )
                        )
                    }
                }
        }
    }

    private fun saveSortingTypeIntoDataStore(sortBy: String) {
        viewModelScope.launch(Dispatchers.IO) {
            sortPreferencesRepository.saveOrder(sortBy)
        }
        setState { copy(recycleViewState = RecycleViewState.IdleState) }
    }
}

