package com.example.crypto.view_models

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.crypto.repository.interfaces.MainScreenRepository
import com.example.crypto.repository.interfaces.SortPreferencesRepository
import com.example.crypto.views.fragments.main_screen.MainScreenContract.*
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
                sortByMarketCap()
            }
            is Event.ChoseSortingByPrice -> {
                sortByPrice()
            }
            is Event.ChoseSortingByVolatility -> {
                sortByVolatility()
            }
            is Event.FetchFromDb -> {
                fetchCoinFromDb()
            }
        }
    }

    private fun fetchCoinFromDb() {
        viewModelScope.launch {
            setState { copy(recycleViewState = RecycleViewState.Loading) }
            val coins = repository.getCoinsFromDB()
            setState {
                copy(
                    recycleViewState = RecycleViewState.ItemsFromDb(
                        coins
                    )
                )
            }
        }
    }

    suspend fun saveSortingIntoDataStore(sortBy: String) {
        sortPreferencesRepository.saveOrder(sortBy)
    }

    suspend fun getSortingFromDataStore(): String = sortPreferencesRepository.getOrder()

    private fun sortByVolatility() {
        viewModelScope.launch {
            setState { copy(recycleViewState =RecycleViewState.Loading) }
            try {
                val coins = repository.getCoinsByVol().cachedIn(viewModelScope)
                setState {
                    copy(
                        recycleViewState = RecycleViewState.SortingByVolatility(
                            coins
                        )
                    )
                }
            } catch (exception: Exception) {
                Log.e("mainViewModel", exception.toString())
            }
        }
    }

    private fun sortByPrice() {
        viewModelScope.launch {
            setState { copy(recycleViewState = RecycleViewState.Loading) }
            try {
                val coins = repository.getCoinsByPrice().cachedIn(viewModelScope)
                setState {
                    copy(
                        recycleViewState = RecycleViewState.SortingByPrice(
                            coins
                        )
                    )
                }
            } catch (exception: Exception) {
                Log.e("mainViewModel", exception.toString())
            }

        }
    }

    private fun sortByMarketCap() {
        viewModelScope.launch {
            setState { copy(recycleViewState = RecycleViewState.Loading) }
            try {
                val coins = repository.getCoinsByCap().cachedIn(viewModelScope)
                setState {
                    copy(
                        recycleViewState = RecycleViewState.SortingByMarketCap(
                            coins
                        )
                    )
                }
            } catch (exception: Exception) {
                Log.e("mainViewModel", exception.toString())
            }
        }
    }
}

