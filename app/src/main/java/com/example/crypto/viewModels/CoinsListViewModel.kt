package com.example.crypto.viewModels

import androidx.lifecycle.*
import com.example.crypto.model.CoinsResponseResult
import com.example.crypto.repository.CoinsListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CoinsListViewModel(
    private val repository: CoinsListRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val state: LiveData<UiState>
    val accept: (UiAction) -> Unit


    init {
        val orderLiveData =
            MutableLiveData(savedStateHandle.get(LAST_ORDER) ?: DEFAULT_ORDER)

        state = orderLiveData
            .distinctUntilChanged()
            .switchMap { orderString ->
                liveData {
                    val uiState = repository.getCoinsListResultStream(orderString)
                        .map {
                            UiState(
                                order = orderString,
                                responseResult = it
                            )
                        }
                        .asLiveData(Dispatchers.Main)
                    emitSource(uiState)
                }
            }

        accept = { action ->
            when (action) {
                is UiAction.ChangeOrder -> orderLiveData.postValue(action.order)
                is UiAction.Scroll -> if (action.shouldFetchMore) {
                    val immutableOrder = orderLiveData.value
                    if (immutableOrder != null) {
                        viewModelScope.launch {
                            repository.requestMore(immutableOrder)
                        }
                    }
                }
            }
        }
    }

    override fun onCleared() {
        savedStateHandle[LAST_ORDER] = state.value?.order
        super.onCleared()
    }

    private val UiAction.Scroll.shouldFetchMore
        get() = visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount


    sealed class UiAction {
        data class ChangeOrder(val order: String) : UiAction()
        data class Scroll(
            val visibleItemCount: Int,
            val lastVisibleItemPosition: Int,
            val totalItemCount: Int
        ) : UiAction()
    }

    data class UiState(
        val order: String,
        val responseResult: CoinsResponseResult
    )
}
private const val VISIBLE_THRESHOLD = 5
private const val LAST_ORDER: String = "last_order"
private const val DEFAULT_ORDER = "market_cap_desc"