package com.example.crypto.view_models

import androidx.lifecycle.viewModelScope
import com.example.crypto.model.constans.*
import com.example.crypto.repository.interfaces.DetailsScreenRepInterface
import com.example.crypto.views.fragments.details_screen.DetailsScreenContract
import kotlinx.coroutines.launch

class DetailsScreenViewModel(
    private val repository: DetailsScreenRepInterface,
    private val coinId: String
) : BaseViewModel<DetailsScreenContract.State, DetailsScreenContract.Event, DetailsScreenContract.Effect>() {

    override fun createInitialState(): DetailsScreenContract.State {
        return DetailsScreenContract.State(
            DetailsScreenContract.ChartState.Loading
        )
    }

    override fun handleEvent(event: DetailsScreenContract.Event) {
        when(event) {
            is DetailsScreenContract.Event.ChoseOneDayInterval -> {
                setChartForOneDay()
            }
            is DetailsScreenContract.Event.ChoseOneWeekInterval -> {
                setChartForOneWeek()
            }
            is DetailsScreenContract.Event.ChoseOneMonthInterval -> {
                setChartForOneMonth()
            }
            is DetailsScreenContract.Event.ChoseOneYearInterval -> {
                setChartForOneYear()
            }
            is DetailsScreenContract.Event.ChoseAllTimeInterval -> {
                setChartForAllTime()
            }
        }
    }

    private fun setChartForAllTime() {
        viewModelScope.launch {
            setState { copy(chartState = DetailsScreenContract.ChartState.Loading) }
            val data = repository.fetchPriceChange(coinId, LABEL_ALL_TIME)
            setState { copy(chartState = DetailsScreenContract.ChartState.AllTime(data)) }
        }
    }

    private fun setChartForOneYear() {
        viewModelScope.launch {
            setState { copy(chartState = DetailsScreenContract.ChartState.Loading) }
            val data = repository.fetchPriceChange(coinId, LABEL_YEAR)
            setState { copy(chartState = DetailsScreenContract.ChartState.PerYear(data)) }
        }
    }

    private fun setChartForOneMonth() {
        viewModelScope.launch {
            setState { copy(chartState = DetailsScreenContract.ChartState.Loading) }
            val data = repository.fetchPriceChange(coinId, LABEL_MONTH)
            setState { copy(chartState = DetailsScreenContract.ChartState.PerMonth(data)) }
        }
    }

    private fun setChartForOneWeek() {
        viewModelScope.launch {
            setState { copy(chartState = DetailsScreenContract.ChartState.Loading) }
            val data = repository.fetchPriceChange(coinId, LABEL_WEEK)
            setState { copy(chartState = DetailsScreenContract.ChartState.PerWeek(data)) }
        }
    }

    private fun setChartForOneDay() {
        viewModelScope.launch {
            setState { copy(chartState = DetailsScreenContract.ChartState.Loading) }
            val data = repository.fetchPriceChange(coinId, LABEL_DAY)
            setState { copy(chartState = DetailsScreenContract.ChartState.PerDay(data)) }
        }
    }

    suspend fun getMinAndMaxPriceForDetailsScreen(coinId: String, interval: String) =
        repository.getMinAndMaxPrice(coinId, interval)
}