package com.example.crypto.views.fragments.main_screen

import androidx.paging.PagingData
import com.example.crypto.model.api.responses.coins_list.Coin
import com.example.crypto.model.interfaces_mvi.UiEffect
import com.example.crypto.model.interfaces_mvi.UiEvent
import com.example.crypto.model.interfaces_mvi.UiState
import kotlinx.coroutines.flow.Flow

class MainScreenContract {

    sealed class Event: UiEvent {
        object ChoseSortingByPrice: Event()
        object ChoseSortingByVolatility: Event()
        object ChoseSortingByMarketCap: Event()
        object FetchFromDb: Event()
    }

    data class State(
        val recycleViewState: RecycleViewState
    ): UiState

    sealed class RecycleViewState {
        object Loading: RecycleViewState()
        data class SortingByPrice(val coins: Flow<PagingData<Coin>>): RecycleViewState()
        data class SortingByMarketCap(val coins: Flow<PagingData<Coin>>): RecycleViewState()
        data class SortingByVolatility(val coins: Flow<PagingData<Coin>>): RecycleViewState()
        data class ItemsFromDb(val coins: Flow<PagingData<Coin>>): RecycleViewState()
    }

    sealed class Effect: UiEffect {}
}