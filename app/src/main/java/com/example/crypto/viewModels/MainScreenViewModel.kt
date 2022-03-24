package com.example.crypto.viewModels

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.crypto.repository.SortPreferencesRepositoryImpl
import com.example.crypto.repository.interfaces.MainScreenRepInterface
import com.example.crypto.repository.interfaces.SortPreferencesRepInterface
import com.example.crypto.views.fragments.mainScreen.MainScreenContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val repository: MainScreenRepInterface,
    private val sortPreferencesRepository: SortPreferencesRepInterface
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
            is MainScreenContract.Event.FetchFromDb -> {
                fetchCoinFromDb()
            }
        }
    }

    private fun fetchCoinFromDb() {
        viewModelScope.launch {
            //setState { copy(recycleViewState = MainScreenContract.RecycleViewState.Loading) }
            val coins = repository.getCoinsFromDB()
            setState {
                copy(
                    recycleViewState = MainScreenContract.RecycleViewState.ItemsFromDb(
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

