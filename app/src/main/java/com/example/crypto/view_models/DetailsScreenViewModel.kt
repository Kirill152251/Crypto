package com.example.crypto.view_models

import androidx.lifecycle.viewModelScope
import com.example.crypto.model.constans.*
import com.example.crypto.repository.interfaces.DetailsScreenRepository
import com.example.crypto.views.fragments.details_screen.DetailsScreenContract.State
import com.example.crypto.views.fragments.details_screen.DetailsScreenContract.Event
import com.example.crypto.views.fragments.details_screen.DetailsScreenContract.Effect
import com.example.crypto.views.fragments.details_screen.DetailsScreenContract.ChartState
import kotlinx.coroutines.launch

class DetailsScreenViewModel(
    private val repository: DetailsScreenRepository,
    private val coinId: String
) : BaseViewModel<State, Event, Effect>() {

    override fun createInitialState(): State {
        return State(
            ChartState.Loading
        )
    }

    override fun handleEvent(event: Event) {
        when(event) {
            is Event.ChoseOneDayInterval -> {
                setChart(LABEL_DAY)
            }
            is Event.ChoseOneWeekInterval -> {
                setChart(LABEL_WEEK)
            }
            is Event.ChoseOneMonthInterval -> {
                setChart(LABEL_MONTH)
            }
            is Event.ChoseOneYearInterval -> {
                setChart(LABEL_YEAR)
            }
            is Event.ChoseAllTimeInterval -> {
                setChart(LABEL_ALL_TIME)
            }
        }
    }

    private fun setChart(period: String) {
        when(period) {
            LABEL_DAY -> {
                viewModelScope.launch {
                    setState { copy(chartState = ChartState.Loading) }
                    val data = repository.fetchPriceChange(coinId, period)
                    setState { copy(chartState = ChartState.PerDay(data)) }
                }
            }
            LABEL_WEEK -> {
                viewModelScope.launch {
                    setState { copy(chartState = ChartState.Loading) }
                    val data = repository.fetchPriceChange(coinId, period)
                    setState { copy(chartState = ChartState.PerWeek(data)) }
                }
            }
            LABEL_MONTH -> {
                viewModelScope.launch {
                    setState { copy(chartState = ChartState.Loading) }
                    val data = repository.fetchPriceChange(coinId, period)
                    setState { copy(chartState = ChartState.PerMonth(data)) }
                }
            }
            LABEL_YEAR -> {
                viewModelScope.launch {
                    setState { copy(chartState = ChartState.Loading) }
                    val data = repository.fetchPriceChange(coinId, period)
                    setState { copy(chartState = ChartState.PerYear(data)) }
                }
            }
            else -> {
                viewModelScope.launch {
                    setState { copy(chartState = ChartState.Loading) }
                    val data = repository.fetchPriceChange(coinId, period)
                    setState { copy(chartState = ChartState.AllTime(data)) }
                }
            }
        }
    }

    suspend fun getMinAndMaxPriceForDetailsScreen(coinId: String, interval: String) =
        repository.getMinAndMaxPrice(coinId, interval)
}