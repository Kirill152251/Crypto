package com.example.crypto.views.fragments.main_screen

import androidx.paging.PagingData
import com.example.crypto.model.api.responses.coins_list.Coin
import com.example.crypto.model.interfaces_mvi.UiEffect
import com.example.crypto.model.interfaces_mvi.UiEvent
import com.example.crypto.model.interfaces_mvi.UiState

class MainScreenContract {

    sealed class Event : UiEvent {
        object ChoseSortingByPrice : Event()
        object ChoseSortingByVolatility : Event()
        object ChoseSortingByMarketCap : Event()
        object FetchFromDb : Event()
        data class SaveSortingType(val sortType: String) : Event()
    }

    data class State(
        val recycleViewState: RecycleViewState,
    ) : UiState

    sealed class RecycleViewState {
        object Loading : RecycleViewState()
        object IdleState : RecycleViewState()
        data class SortingByPrice(val coins: PagingData<Coin>) : RecycleViewState()
        data class SortingByMarketCap(val coins: PagingData<Coin>) : RecycleViewState()
        data class SortingByVolatility(val coins: PagingData<Coin>) : RecycleViewState()
        data class ItemsFromDb(val coins: PagingData<Coin>) : RecycleViewState()
    }

    sealed class Effect : UiEffect {}
}